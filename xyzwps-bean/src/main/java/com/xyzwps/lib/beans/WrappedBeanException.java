package com.xyzwps.lib.beans;

public class WrappedBeanException extends RuntimeException {

    public WrappedBeanException(Exception cause) {
        super(cause);
    }
}
