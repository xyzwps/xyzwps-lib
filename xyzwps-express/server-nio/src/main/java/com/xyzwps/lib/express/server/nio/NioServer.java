package com.xyzwps.lib.express.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServer {

    public static void main(String[] args) throws IOException {
        try (var ssc = ServerSocketChannel.open()) {
            ssc.socket().bind(new InetSocketAddress(3000));
            ssc.configureBlocking(false);

            var selector = Selector.open();
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int num = selector.select();
                if (num <= 0) {
                    continue; // there are no ready channels to process
                }

                var selectedKeys = selector.selectedKeys();
                var keyItr = selectedKeys.iterator();

                while (keyItr.hasNext()) {
                    var key = keyItr.next();
                    keyItr.remove();

                    if (key.isAcceptable()) {
                        // A connection was accepted by a ServerSocketChannel.
                        var server = (ServerSocketChannel) key.channel();
                        System.out.println(server.hashCode() + " " + ssc.hashCode() + " " + (server == ssc));

                        var client = server.accept();
                        if (client == null) {
                            continue;
                        }
                        client.configureBlocking(false);
                        // register socket channel with selector for read operations
                        client.register(selector, SelectionKey.OP_READ);
                    }
                    //
                    else if (key.isReadable()) {
                        // A socket channel is ready for reading.
                        var client = (SocketChannel) key.channel();
                        handleRequest(client);
                        key.interestOps(SelectionKey.OP_WRITE);
                    }
                    //
                    else if (key.isWritable()) {
                        // A socket channel is ready for writing.
                        var client = (SocketChannel) key.channel();
                        // Perform work on the socket channel.
                        handleResponse(client);
                    }
                }
            }
        }
    }

    private static void handleRequest(SocketChannel channel) throws IOException {
        // 从通道中读取请求头数据
        ByteBuffer buffer = ByteBuffer.allocate(2048);

        while (channel.read(buffer) > 0) {
            buffer.flip();

            while (buffer.hasRemaining()) {
                System.out.print((char) buffer.get());
            }

            buffer.clear();

            System.out.println();
        }
    }

    private static void handleResponse(SocketChannel channel) throws IOException {
        var resp = "HTTP/1.1 200\r\nContent-Type: text/plain\r\nContent-Length: 3\r\n\r\n123";
        channel.write(ByteBuffer.wrap(resp.getBytes()));
        channel.close(); // TODO: 实现 keep-alive
    }


}
