package com.xyzwps.lib.express.server.helidon;

import com.xyzwps.lib.express.Server;
import com.xyzwps.lib.express.ServerConfig;
import io.helidon.webserver.WebServer;
import io.helidon.webserver.WebServerConfig;

import java.util.function.Consumer;

public class HelidonServer implements Server {

    private final WebServerConfig.Builder serverBuilder;
    private final Consumer<WebServerConfig.Builder> config;

    public HelidonServer(Consumer<WebServerConfig.Builder> config) {
        this.serverBuilder = WebServer.builder();
        this.config = config;
    }

    public HelidonServer() {
        this(null);
    }

    @Override
    public void start(ServerConfig config) {
        if (this.config != null) {
            this.config.accept(serverBuilder);
            serverBuilder.clearRouting();
        }
        serverBuilder
                .port(config.port)
                .routing(routing -> routing.any(new AnyHandler(config)))
                .build()
                .start();
    }
}
