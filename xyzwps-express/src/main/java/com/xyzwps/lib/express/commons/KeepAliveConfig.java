package com.xyzwps.lib.express.commons;

import com.xyzwps.lib.bedrock.Args;

/**
 * @param timeout in seconds
 * @param max
 * @see <a href="https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Keep-Alive">Keep-Alive: MDN</a>
 */
public record KeepAliveConfig(int timeout, int max) {
    public KeepAliveConfig {
        Args.ge(timeout, 0, "Parameter timeout should be greater than or equals to 0");
        Args.ge(max, 0, "Parameter max should be greater than or equals to 0");
    }
}
