package com.xyzwps.lib.beans;

public class BeanException extends RuntimeException {

    BeanException(String message) {
        super(message);
    }

    BeanException(String message, Exception e) {
        super(message, e);
    }
}
