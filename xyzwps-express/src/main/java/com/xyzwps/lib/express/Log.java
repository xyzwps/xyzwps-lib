package com.xyzwps.lib.express;

import com.xyzwps.lib.log.Logger;
import com.xyzwps.lib.log.StandardIOLogger;

import java.util.Objects;

public final class Log {

    private static Logger logger = StandardIOLogger.INSTANCE;

    public static void setLogger(Logger logger) {
        Log.logger = Objects.requireNonNull(logger);
    }

    public static void tracef(String message, Object... args) {
        logger.tracef(message, args);
    }

    public static void debugf(String message, Object... args) {
        logger.debugf(message, args);
    }

    public static void infof(String message, Object... args) {
        logger.infof(message, args);
    }

    public static void warnf(String message, Object... args) {
        logger.warnf(message, args);
    }

    public static void errorf(String message, Object... args) {
        logger.errorf(message, args);
    }

    public static void errorf(Throwable t, String message, Object... args) {
        logger.errorf(t, message, args);
    }

    public static void fatalf(String message, Object... args) {
        logger.fatalf(message, args);
    }
}
