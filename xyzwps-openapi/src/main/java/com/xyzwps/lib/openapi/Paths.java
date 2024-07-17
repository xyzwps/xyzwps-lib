package com.xyzwps.lib.openapi;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Paths implements OASElement {

    private final Map<String, PathItem> items = new TreeMap<>();

    public Paths add(String path, PathItem item) {
        pathMUSTBeginWithAForwardSlash(path);
        items.put(path, item);
        return this;
    }

    public Set<String> pathSet() {
        return items.keySet();
    }

    public PathItem item(String path) {
        pathMUSTBeginWithAForwardSlash(path);
        return items.get(path);
    }

    private static void pathMUSTBeginWithAForwardSlash(String path) {
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("Path mush begin with a forward slash");
        }
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
