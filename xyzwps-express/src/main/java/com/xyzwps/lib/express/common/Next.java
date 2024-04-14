package com.xyzwps.lib.express.common;

public interface Next {
    void call();

    Next EMPTY = () -> {
    };
}
