package com.xyzwps.lib.log;

import java.util.Objects;

public final class StandardIOLogger implements Logger {

    public static final StandardIOLogger INSTANCE = new StandardIOLogger();

    private volatile LogLevel level = LogLevel.INFO;

    private StandardIOLogger() {
    }

    @Override
    public void setLevel(LogLevel level) {
        this.level = Objects.requireNonNull(level);
    }

    @Override
    public LogLevel getLevel() {
        return level;
    }

    @Override
    public void tracef(String message, Object... args) {
        if (level.weight >= LogLevel.TRACE.weight) {
            System.out.printf("[TRACE] " + message + "\n", args);
        }
    }

    @Override
    public void debugf(String message, Object... args) {
        if (level.weight >= LogLevel.DEBUG.weight) {
            System.out.printf("[DEBUG] " + message + "\n", args);
        }
    }

    @Override
    public void infof(String message, Object... args) {
        if (level.weight >= LogLevel.INFO.weight) {
            System.out.printf("[INFO] " + message + "\n", args);
        }
    }

    @Override
    public void warnf(String message, Object... args) {
        if (level.weight >= LogLevel.WARN.weight) {
            System.out.printf("[WARN] " + message + "\n", args);
        }
    }

    @Override
    public void errorf(String message, Object... args) {
        if (level.weight >= LogLevel.ERROR.weight) {
            System.err.printf("[ERROR] " + message + "\n", args);
        }
    }

    @Override
    public void errorf(Throwable t, String message, Object... args) {
        if (level.weight >= LogLevel.ERROR.weight) {
            System.err.printf("[ERROR] " + message, args);
            if (t == null) {
                System.err.println();
            } else {
                System.err.printf(" : " + t.getMessage() + "\n");
                t.printStackTrace(System.err);
            }
        }
    }

    @Override
    public void fatalf(String message, Object... args) {
        if (level.weight >= LogLevel.FAULT.weight) {
            System.err.printf("[FATAL] " + message + "\n", args);
        }
    }
}
