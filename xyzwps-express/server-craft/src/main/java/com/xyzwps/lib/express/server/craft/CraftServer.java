package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.Server;
import com.xyzwps.lib.express.ServerConfig;
import com.xyzwps.lib.express.Next;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                log.error("Socket handling failed with uncatched error", e);
                throw new UncheckedIOException("Accept server socket failed", e);
            }
        }
    }

    private static ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            log.error("Create server socket failed", e);
            throw new UncheckedIOException("Create server socket failed", e);
        }
    }

    void handleSocket(Socket socket, HttpMiddleware middleware) {
        Thread.ofVirtual().start(() -> {
            try {
                var in = socket.getInputStream();
                var out = socket.getOutputStream();

                var request = new RawRequestParser().parse(in).toHttpRequest();
                var response = new CraftHttpResponse(out, request);

                middleware.call(request, response, Next.EMPTY);

                socket.close(); // TODO: keep-alive
            } catch (IOException e) {
                System.out.println(e); // TODO: handle exception
            } catch (Exception e) {
                System.out.println(e); // TODO: handle exception
            }
        });
    }

}
