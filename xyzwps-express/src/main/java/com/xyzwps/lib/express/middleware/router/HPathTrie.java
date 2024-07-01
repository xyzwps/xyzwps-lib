package com.xyzwps.lib.express.middleware.router;

import java.util.HashMap;
import java.util.Map;

public class HPathTrie<T> {
    Node root;

    public HPathTrie() {
        root = new Node(null);
    }

    public T find(String path) {
        var segments = HPath.pathToSegmentStrings(path);
        var len = segments.length;
        if (len == 0) {
            return root.middleware;
        }

        Node curr = root;
        T result = null;
        for (int i = 0; i < len; i++) {
            var segment = segments[i];
            if (curr.exactChildren.containsKey(segment)) {
                curr = curr.exactChildren.get(segment);
                if (i == len - 1) {
                    result = curr.middleware;
                    break;
                }
            } else if (curr.starChild != null) {
                curr = curr.starChild;
                if (i == len - 1) {
                    result = curr.middleware;
                    break;
                }
            }
        }
        return result;
    }

    public void insert(HPath path, T middleware) {
        var len = path.length();
        if (len == 0) {
            root.middleware = middleware;
            return;
        }

        var segments = path.segments;
        Node curr = root;
        for (int i = 0; i < len; i++) {
            switch (segments[i]) {
                case PathSegment.PlainSegment s -> {
                    var node = curr.exactChildren.get(s.value());
                    if (node == null) {
                        node = new Node(s);
                    }
                    curr.exactChildren.put(s.value(), node);
                    curr = node;
                    if (i == len - 1) {
                        if (curr.middleware != null) {
                            throw new IllegalArgumentException("Duplicate path: " + path);
                        }
                        curr.middleware = middleware;
                    }
                }
                case PathSegment.StarSegment s -> {
                    if (curr.starChild == null) {
                        curr.starChild = new Node(s);
                    }
                    curr = curr.starChild;
                    if (i == len - 1) {
                        if (curr.middleware != null) {
                            throw new IllegalArgumentException("Duplicate path: " + path);
                        }
                        curr.middleware = middleware;
                    }
                }
                case PathSegment.VariableSegment s -> {
                    if (curr.starChild == null) {
                        curr.starChild = new Node(s);
                    }
                    curr = curr.starChild;
                    if (i == len - 1) {
                        if (curr.middleware != null) {
                            throw new IllegalArgumentException("Duplicate path: " + path);
                        }
                        curr.middleware = middleware;
                    }
                }
            }
        }
    }


    private class Node {
        final PathSegment segment;
        T middleware;
        final Map<String, Node> exactChildren;
        Node starChild;

        Node(PathSegment segment) {
            this.segment = segment;
            this.exactChildren = new HashMap<>();
        }
    }
}
