package com.xyzwps.lib.express.commons;

import java.util.concurrent.atomic.AtomicInteger;

public final class KeepAliveInfo {

    private final boolean keepAlive;

    private final AtomicInteger max;

    private final long endTs;

    public KeepAliveInfo(KeepAliveConfig config) {
        if (config == null) {
            keepAlive = false;
            max = null;
            endTs = 0L;
        } else {
            keepAlive = true;
            max = new AtomicInteger(config.max());
            endTs = System.currentTimeMillis() + config.timeout() * 1000L;
        }
    }

    public boolean shouldKeepAlive() {
        return keepAlive && max.get() > 0 && System.currentTimeMillis() < endTs;
    }

    // TODO: 实现自动 timeout

    public String toHeaderValue() {
        if (keepAlive) {
            return "timeout=" + ((endTs - System.currentTimeMillis()) / 1000L) + ", max=" + (max.decrementAndGet());
        }
        throw new IllegalStateException("Cannot keep alive");
    }

}
