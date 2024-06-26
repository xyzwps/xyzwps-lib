package com.xyzwps.lib.jdbc;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.*;

public final class DaoFactory {

    public static <T> T createDao(Class<T> daoInterface, ResultSetToBean rs2b, Connection conn) {
        Objects.requireNonNull(daoInterface);

        if (!daoInterface.isInterface()) {
            throw new DbException("The daoInterface " + daoInterface.getCanonicalName() + " must be an interface.");
        }

        // noinspection unchecked
        return (T) Proxy.newProxyInstance(
                daoInterface.getClassLoader(),
                new Class<?>[]{daoInterface},
                new DaoMethodInvocationHandler(daoInterface, rs2b, conn)
        );
    }

    private DaoFactory() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate DaoFactory.");
    }
}
