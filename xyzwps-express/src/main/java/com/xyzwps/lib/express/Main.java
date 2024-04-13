package com.xyzwps.lib.express;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(3000);
        while (true) {
            System.out.println("fuck");
            var socket = server.accept();
            Thread.ofVirtual().start(() -> {
                try {
                    var in = socket.getInputStream();
                    var requestParser = new SimpleRequestParser();
                    var req = requestParser.parse(in);
                    System.out.println(req);

                    var out = socket.getOutputStream();
                    out.write("HTTP/1.1 200\r\nContent-Type: application/json\r\n\r\n[1,2,3]".getBytes());
                    socket.shutdownOutput(); // TODO: keep-alive
                } catch (IOException e) {
                    System.out.println(e);
                }
            });
        }
    }
}
