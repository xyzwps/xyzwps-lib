package com.xyzwps.lib.log;

/**
 * Logger interface.
 * <p>
 * This interface is intended to be used by applications to log messages.
 * The implementation of this interface should be thread-safe.
 * <p>
 * Every method ends with 'f' means that the message would use {@link String#format java format string}.
 */
public interface Logger {

    /**
     * Set the log level.
     *
     * @param level the log level. Null value is not allowed.
     */
    void setLevel(LogLevel level);

    /**
     * Get the log level.
     * <p>
     * The default log level is {@link LogLevel#INFO}.
     *
     * @return the log level
     */
    LogLevel getLevel();

    /**
     * Log a message at the trace level.
     *
     * @param message the message pattern
     * @param args    the message arguments
     */
    void tracef(String message, Object... args);

    /**
     * Log a message at the debug level.
     *
     * @param message the message pattern
     * @param args    the message arguments
     */
    void debugf(String message, Object... args);

    /**
     * Log a message at the info level.
     *
     * @param message the message pattern
     * @param args    the message arguments
     */
    void infof(String message, Object... args);

    /**
     * Log a message at the warn level.
     *
     * @param message the message pattern
     * @param args    the message arguments
     */
    void warnf(String message, Object... args);

    /**
     * Log a message at the error level.
     *
     * @param message the message pattern
     * @param args    the message arguments
     */
    void errorf(String message, Object... args);

    /**
     * Log a message at the error level.
     *
     * @param t       the throwable
     * @param message the message pattern
     * @param args    the message arguments
     */
    void errorf(Throwable t, String message, Object... args);

    /**
     * Log a message at the fatal level.
     *
     * @param message the message pattern
     * @param args    the message arguments
     */
    void fatalf(String message, Object... args);
}
