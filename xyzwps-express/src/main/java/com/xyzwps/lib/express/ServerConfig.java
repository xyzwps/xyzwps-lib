package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

public final class ServerConfig {

    public final HttpMiddleware middleware;
    public final int port;

    public static ServerConfig create() {
        return new ServerConfig(HttpMiddleware.DO_NOTHING, 3000);
    }

    private ServerConfig(HttpMiddleware middleware, int port) {
        this.middleware = Args.notNull(middleware, "Middleware cannot be null");
        this.port = port;
    }

    /**
     * Use a {@link HttpMiddleware}.
     *
     * @param mw cannot be null
     * @return a new config
     */
    public ServerConfig use(HttpMiddleware mw) {
        Args.notNull(mw, "Middleware cannot be null");
        return new ServerConfig(HttpMiddleware.compose2(middleware, mw), this.port);
    }

    /**
     * Set port.
     *
     * @param port should be between 0 and 0xFFFF
     * @return a new config
     */
    public ServerConfig port(int port) {
        return new ServerConfig(this.middleware, checkPort(port));
    }

    private static int checkPort(int port) {
        if (port < 0 || port > 0xFFFF)
            throw new IllegalArgumentException("port out of range:" + port);
        return port;
    }
}
