package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.*;

public final class BioServer implements Server {

    @Override
    public void start(ServerConfig config) {
        Args.notNull(config, "ServerConfig cannot be null");

        try (var serverSocket = createServerSocket(config.port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                try {
                    var socket = serverSocket.accept();
                    this.handleSocket(socket, config.middleware);
                } catch (IOException e) {
                    Log.errorf(e, "Socket handling failed with uncaught error");
                    throw new UncheckedIOException("Accept server socket failed", e);
                }
            }
        } catch (IOException e) {
            Log.errorf(e, "Server socket closed");
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
            Log.errorf(e, "Create server socket failed");
            throw e;
        }
    }

    void handleSocket(Socket socket, HttpMiddleware middleware) {
        RequestExecutors.runOnVirtualThread(new BioConnection(socket, middleware));
    }

}
