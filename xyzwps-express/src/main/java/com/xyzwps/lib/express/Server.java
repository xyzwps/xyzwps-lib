package com.xyzwps.lib.express;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new UncheckedIOException("Create server socket failed", e);
        }
    }

    public void run() {
        while (true) {
            try {
                this.handleSocket(this.serverSocket.accept());
            } catch (IOException e) {
                throw new UncheckedIOException("Accept server socket failed", e);
            }
        }
    }

    void handleSocket(Socket socket) {
        Thread.ofVirtual().start(() -> {
            try {
                var in = socket.getInputStream();
                var request = new SimpleRawRequestParser().parse(in).toHttpRequest();
                System.out.printf("-> %s %s", request.method(), request.url());
                var response = new SimpleHttpResponse(socket.getOutputStream(), request)
                                       .status(200)
                                       .header("Content-Type", "application/json");
                response.send("[\"Hello\":\"World\"]");
                socket.shutdownOutput(); // TODO: keep-alive
            } catch (IOException e) {
                System.out.println(e);
            }
        });
    }

}
