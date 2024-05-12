package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;

public final class CraftServer implements Server {

    private static final Logger log = LoggerFactory.getLogger(CraftServer.class);

    @Override
    public void start(ServerConfig config) {
        Args.notNull(config, "ServerConfig cannot be null");

        var serverSocket = createServerSocket(config.port);
        while (true) {
            try {
                var socket = serverSocket.accept();
                this.handleSocket(socket, config.middleware);
            } catch (IOException e) {
                log.error("Socket handling failed with uncaught error", e);
                throw new UncheckedIOException("Accept server socket failed", e);
            }
        }
    }

    private static ServerSocket createServerSocket(int port) {
        try {
            var server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress((InetAddress) null, port), 50);
            return server;
        } catch (IOException e) {
            log.error("Create server socket failed", e);
            throw new UncheckedIOException("Create server socket failed", e);
        }
    }

    void handleSocket(Socket socket, HttpMiddleware middleware) {
        RequestExecutors.runOnVirtualThread(new ConnectionHandler(socket, middleware));
    }

}
