package com.xyzwps.lib.express.filter;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.UrlPath;
import com.xyzwps.lib.express.UrlSegment;

import java.util.HashMap;
import java.util.Map;

class UrlPathTrie<D> {

    private final Node root = new Node();

    private D rootData;

    void insert(UrlPath path, D data) {
        Args.notNull(path, "Argument path cannot be null");
        Args.notNull(data, "Argument data cannot be null");

        final int len = path.length();
        if (len == 0) {
            if (rootData == null) {
                rootData = data;
                return;
            } else {
                throw new DuplicatePathException("/");
            }
        }

        Node curr = root;
        for (int i = 0; i < len; i++) {
            var segment = path.get(i);
            var isLast = i == len - 1;
            switch (segment) {
                case UrlSegment.Text textSegment -> {
                    var textChildren = curr.textChildren;
                    var text = textSegment.text();
                    var next = textChildren.get(text);
                    if (next == null) {
                        next = new Node();
                        textChildren.put(text, next);
                    }
                    curr = next;
                }
                case UrlSegment.Param ignored -> {
                    if (curr.placeholderChild == null) {
                        curr.placeholderChild = new Node();
                    }
                    curr = curr.placeholderChild;
                }
                case UrlSegment.Star ignored -> {
                    if (curr.placeholderChild == null) {
                        curr.placeholderChild = new Node();
                    }
                    curr = curr.placeholderChild;
                }
                case UrlSegment.Star2 ignored -> {
                    if (curr.star2Data == null) {
                        curr.star2Data = data;
                        return;
                    }
                    throw new DuplicatePathException(path.rawPath());
                }
                default -> throw new IllegalArgumentException("Unsupported segment type: " + segment.getClass());
            }

            if (isLast) {
                if (curr.data != null) {
                    throw new DuplicatePathException(path.rawPath());
                }
                curr.data = data;
            }
        }
    }

    D match(UrlPath path) {
        Args.notNull(path, "Argument path cannot be null");

        final int len = path.length();
        if (len == 0) {
            if (rootData != null) {
                return rootData;
            } else {
                return root.star2Data;
            }
        }

        return match(root, path, 0);
    }

    private D match(Node curr, UrlPath path, int deep) {
        var segment = path.get(deep);
        var text = segment.text();

        var textChildren = curr.textChildren;

        Node forStar2TextNode = null;

        var next = textChildren.get(text);
        if (next != null) {
            if (deep + 1 == path.length()) {
                if (next.data != null) {
                    return next.data;
                }
                forStar2TextNode = next;
            } else {
                var d = match(next, path, deep + 1);
                if (d != null) {
                    return d;
                }
            }
        }

        Node forStar2PlaceHolderNode = null;

        next = curr.placeholderChild;
        if (next != null) {
            if (deep + 1 == path.length()) {
                if (next.data != null) {
                    return next.data;
                }
                forStar2PlaceHolderNode = next;
            } else {
                var d = match(next, path, deep + 1);
                if (d != null) {
                    return d;
                }
            }
        }

        if (forStar2TextNode != null && forStar2TextNode.star2Data != null) {
            return forStar2TextNode.star2Data;
        }
        if (forStar2PlaceHolderNode != null && forStar2PlaceHolderNode.star2Data != null) {
            return forStar2PlaceHolderNode.star2Data;
        }
        return curr.star2Data;
    }


    class Node {
        final Map<String, Node> textChildren = new HashMap<>();
        D data = null;
        Node placeholderChild = null;
        D star2Data = null;
    }
}
