package com.xyzwps.lib.express;


public final class ServerConfig {

    public Filter filter;
    public int port;

    public static ServerConfig create() {
        return new ServerConfig(null, 3000);
    }

    private ServerConfig(Filter filter, int port) {
        this.filter = filter;
        this.port = port;
    }

    /**
     * Use a {@link Filter}.
     *
     * @param filter to be used
     * @return this
     */
    public ServerConfig use(Filter filter) {
        if (filter != null) {
            if (this.filter == null) {
                this.filter = filter;
            } else {
                this.filter = this.filter.andThen(filter);
            }
        }
        return this;
    }

    /**
     * Set port.
     *
     * @param port should be between 0 and 0xFFFF
     * @return this
     */
    public ServerConfig port(int port) {
        this.port = checkPort(port);
        return this;
    }

    private static int checkPort(int port) {
        if (port < 0 || port > 0xFFFF)
            throw new IllegalArgumentException("port out of range:" + port);
        return port;
    }
}
