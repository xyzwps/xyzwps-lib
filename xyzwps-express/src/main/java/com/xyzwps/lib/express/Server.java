package com.xyzwps.lib.express;

/**
 * Web server.
 */
public interface Server {

    /**
     * Start a server with a specific config.
     *
     * @param config cannot be null
     */
    void start(ServerConfig config);
}
