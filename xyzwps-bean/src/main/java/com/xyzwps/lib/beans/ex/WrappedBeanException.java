package com.xyzwps.lib.beans.ex;

public class WrappedBeanException extends RuntimeException {

    public WrappedBeanException(Exception cause) {
        super(cause);
    }
}
