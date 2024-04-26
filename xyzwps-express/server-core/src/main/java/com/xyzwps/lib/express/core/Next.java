package com.xyzwps.lib.express.core;

public interface Next {
    void call();

    Next EMPTY = () -> {
    };
}
