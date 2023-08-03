package com.xyzwps.lib.dollar.sub;

public interface Observer<T> {

    void next(T value);

    void error(Exception ex);

    void complete();
}
