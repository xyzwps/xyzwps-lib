package com.xyzwps.lib.express;

public interface Next {
    void call();

    Next EMPTY = () -> {
    };
}
