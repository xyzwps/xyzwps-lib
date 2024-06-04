package com.xyzwps.lib.log;

/**
 * Log level.
 */
public enum LogLevel {
    /**
     * Trace level.
     */
    TRACE(10),
    /**
     * Debug level.
     */
    DEBUG(20),
    /**
     * Info level.
     */
    INFO(40),
    /**
     * Warn level.
     */
    WARN(60),
    /**
     * Error level.
     */
    ERROR(80),
    /**
     * Fatal level.
     */
    FAULT(100);

    LogLevel(int weight) {
        this.weight = weight;
    }

    public final int weight;
}
