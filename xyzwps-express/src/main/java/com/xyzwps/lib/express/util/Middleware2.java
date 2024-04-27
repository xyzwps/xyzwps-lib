package com.xyzwps.lib.express.util;

import com.xyzwps.lib.express.Next;

// TODO: 挪到 bedrock 里
public interface Middleware2<P1, P2> {
    void call(P1 p1, P2 p2, Next next);
}
