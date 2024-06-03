package com.xyzwps.lib.express.server.nio;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.server.commons.KeepAliveConfig;
import com.xyzwps.lib.express.server.commons.KeepAliveInfo;
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

    private KeepAliveInfo keepAlive;


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
        if (requestHeaders.connectionKeepAlive()) {
            this.keepAlive = new KeepAliveInfo(new KeepAliveConfig(10 * 1000, 100)); // TODO: 配置化
        }
    }

    public void setStartLine(StartLine startLine) {
        this.startLine = startLine;
    }

    public boolean isKeepAlive() {
        return keepAlive != null && keepAlive.shouldKeepAlive();
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
