package com.xyzwps.lib.express.filter;

public class DuplicatePathException extends RuntimeException {
    public DuplicatePathException(String path) {
        super("Duplicate path: " + path);
    }
}
