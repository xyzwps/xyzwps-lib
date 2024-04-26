package com.xyzwps.lib.express.core;

public interface Server {

    Server use(HttpMiddleware mw);

    void listen(int port);
}
