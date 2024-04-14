package com.xyzwps.lib.express.common;

public interface Middleware2<P1, P2> {
    void call(P1 p1, P2 p2, Next next);
}
