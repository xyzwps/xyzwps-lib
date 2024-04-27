package com.xyzwps.lib.express;

/**
 * Web server.
 */
public interface Server {

    /**
     * Use a {@link HttpMiddleware}. Should be invoked before {@link #start()}.
     *
     * @param mw cannot be null
     * @return this
     */
    Server use(HttpMiddleware mw);

    /**
     * Listen a port on localhost. Should be invoked before {@link #start()}
     *
     * @param port server port
     * @return this
     */
    Server listen(int port);

    /**
     * Start a server. Nothing can be modified after server started.
     */
    void start();

    // TODO: server state
}
