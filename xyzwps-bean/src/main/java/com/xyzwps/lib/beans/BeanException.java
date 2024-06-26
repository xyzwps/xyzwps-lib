package com.xyzwps.lib.beans;

/**
 * Exception for beans.
 */
public final class BeanException extends RuntimeException {

    BeanException(String message) {
        super(message);
    }

    BeanException(String message, Exception e) {
        super(message, e);
    }

    BeanException(Exception e) {
        super(e);
    }
}
