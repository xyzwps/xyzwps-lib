package com.xyzwps.lib.express;

import com.xyzwps.lib.express.common.Middleware2Composer;
import com.xyzwps.lib.express.common.Next;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private HttpMiddleware middleware = HttpMiddleware.DO_NOTHING;

    public Server use(HttpMiddleware mw) {
        this.middleware = Middleware2Composer.compose2(middleware, mw)::call;
        return this;
    }

    public void listen(int port) {
        var serverSocket = createServerSocket(port);
        while (true) {
            try {
                var socket = serverSocket.accept();
                this.handleSocket(socket);
            } catch (IOException e) {
                throw new UncheckedIOException("Accept server socket failed", e);
            }
        }
    }

    private static ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new UncheckedIOException("Create server socket failed", e);
        }
    }

    void handleSocket(Socket socket) {
        Thread.ofVirtual().start(() -> {
            try {
                var in = socket.getInputStream();
                var out = socket.getOutputStream();

                var request = new SimpleRawRequestParser().parse(in).toHttpRequest();
                var response = new SimpleHttpResponse(out, request);

                this.middleware.call(request, response, Next.EMPTY);

//                socket.shutdownOutput(); // TODO: keep-alive
            } catch (IOException e) {
                System.out.println(e); // TODO: handle exception
            } catch (Exception e) {
                System.out.println(e); // TODO: handle exception
            }
        });
    }

}
