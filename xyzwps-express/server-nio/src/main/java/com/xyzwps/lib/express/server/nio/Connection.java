package com.xyzwps.lib.express.server.nio;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.server.commons.StartLine;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public final class Connection {

    private static final AtomicInteger ID_GEN = new AtomicInteger(0);

    private final SocketChannel channel;
    private final SelectionKey key;

    private final int id;

    private boolean keepAlive = false;


    private HttpHeaders requestHeaders;
    private StartLine startLine;

    public Connection(SocketChannel channel, SelectionKey key) {
        this.channel = Objects.requireNonNull(channel);
        this.key = Objects.requireNonNull(key);
        key.attach(this);

        this.id = ID_GEN.getAndIncrement();
    }

    public void setRequestHeaders(HttpHeaders requestHeaders) {
        this.requestHeaders = requestHeaders;
        this.keepAlive = requestHeaders.connectionKeepAlive();
    }

    public void setStartLine(StartLine startLine) {
        this.startLine = startLine;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }

    public HttpHeaders getRequestHeaders() {
        return requestHeaders;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public void clean() {
        this.requestHeaders = null;
        this.startLine = null;
    }
}
