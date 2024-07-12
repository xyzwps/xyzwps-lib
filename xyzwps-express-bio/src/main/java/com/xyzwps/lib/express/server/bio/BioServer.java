package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;

public final class BioServer implements Server {

    private static final Logger log = LoggerFactory.getLogger(BioServer.class);

    @Override
    public void start(ServerConfig config) {
        Args.notNull(config, "ServerConfig cannot be null");

        try (var serverSocket = createServerSocket(config.port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    var socket = serverSocket.accept();
                    this.handleSocket(socket, config.filter);
                } catch (IOException e) {
                    log.error("Socket handling failed with uncaught error", e);
                    throw new UncheckedIOException("Accept server socket failed", e);
                }
            }
        } catch (IOException e) {
            log.error("Server socket closed", e);
            throw new UncheckedIOException("Server socket closed", e);
        }
    }

    private static ServerSocket createServerSocket(int port) throws IOException {
        try {
            var server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress((InetAddress) null, port), 50);
            return server;
        } catch (IOException e) {
            log.error("Create server socket failed", e);
            throw e;
        }
    }

    void handleSocket(Socket socket, Filter filter) {
        RequestExecutors.runOnVirtualThread(new BioConnection(socket, filter));
    }

}
