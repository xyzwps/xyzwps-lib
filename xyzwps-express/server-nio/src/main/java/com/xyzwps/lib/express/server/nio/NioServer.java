package com.xyzwps.lib.express.server.nio;

import com.xyzwps.lib.express.server.commons.InvalidHttpMessageException;
import org.jboss.logging.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NioServer {

    private static final Logger log = Logger.getLogger(NioServer.class);

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

                    if (!key.isValid()) {
                        continue;
                    }

                    var channel = key.channel();

                    if (key.isAcceptable()) {
                        printSelectionKeyState(key, "accepted");
                        if (channel instanceof ServerSocketChannel) {
                            var socketChannel = ssc.accept();
                            if (socketChannel == null) {
                                continue;
                            }
                            socketChannel.configureBlocking(false);
                            var sChanKey = socketChannel.register(selector, SelectionKey.OP_READ);
                            new Connection(socketChannel, sChanKey);
                        } else {
                            log.errorf("ERROR: unhandled acceptable channel %s", channel.getClass().getCanonicalName());
                        }
                    } else if (key.isReadable()) {
                        printSelectionKeyState(key, "readable");
                        if (channel instanceof SocketChannel socket) {
                            try {
                                var parsed = handleRequest(socket);
                                if (parsed.socketEOF) {
                                    key.cancel();
                                    channel.close();
                                } else {
                                    var channelSocket = (Connection) key.attachment();
                                    channelSocket.setStartLine(parsed.startLine);
                                    channelSocket.setRequestHeaders(parsed.headers);
                                    key.interestOps(SelectionKey.OP_WRITE);
                                }
                            } catch (InvalidHttpMessageException e) {
                                key.cancel();
                                channel.close();
                            }
                        } else {
                            log.errorf("ERROR: unhandled readable channel %s", channel.getClass().getCanonicalName());
                        }
                    } else if (key.isWritable()) {
                        printSelectionKeyState(key, "writable");
                        if (channel instanceof SocketChannel socket) {
                            var connection = (Connection) key.attachment();
                            handleResponse(socket, connection);
                            key.interestOps(SelectionKey.OP_READ);
                        } else {
                            log.errorf("ERROR: unhandled writable channel %s", channel.getClass().getCanonicalName());
                        }
                    } else if (key.isConnectable()) {
                        printSelectionKeyState(key, "connected");
                    }
                }
            }
        }
    }

    private static void printSelectionKeyState(SelectionKey key, String branch) {
        log.infof("\nChannel at branch " + branch + " : " + key.channel().getClass().getCanonicalName() + " " + System.identityHashCode(key));
        log.infof(" > r:" + key.isReadable() + " w:" + key.isWritable() + " a:" + key.isAcceptable() + " c:" + key.isConnectable());
        log.infof("");
    }

    /**
     * @return true if EOF
     */
    private static RequestParser handleRequest(SocketChannel channel) throws IOException, InvalidHttpMessageException {
        // 从通道中读取请求头数据
        ByteBuffer buffer = ByteBuffer.allocate(2048);

        RequestParser bsc = new RequestParser();

        while (true) {
            int count = channel.read(buffer);
            if (count < 0) {
                // EOF
                bsc.put(true, (byte) 0);
                bsc.eof();
                return bsc;
            } else if (count == 0) {
                // do nothing
                return bsc;
            } else {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    byte b = buffer.get();
                    bsc.put(false, b);
                }
            }
            buffer.clear();
        }
    }

    private static void handleResponse(SocketChannel channel, Connection connection) throws IOException {
        var keepAlive = connection.isKeepAlive() ? "Keep-Alive: timeout=60, max=500\r\n" : "";
        var resp = String.format("HTTP/1.1 200\r\nContent-Type: text/plain\r\n%sContent-Length: 3\r\n\r\n123", keepAlive);
        var writeBytes = channel.write(ByteBuffer.wrap(resp.getBytes()));
        log.infof(" > 已写入 " + writeBytes + " 总共 " + resp.getBytes().length);
    }

}
