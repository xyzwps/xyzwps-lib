package com.xyzwps.lib.jdbc;

public interface SqlFunction<T, R> {

    R apply(T t) throws Exception;
}
