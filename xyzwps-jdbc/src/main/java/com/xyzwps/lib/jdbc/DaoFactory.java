package com.xyzwps.lib.jdbc;

import java.lang.reflect.Proxy;
import java.util.*;

public final class DaoFactory {

    public static <T> T createDao(Class<T> daoInterface, TX tx) {
        Objects.requireNonNull(daoInterface);

        if (!daoInterface.isInterface()) {
            throw new DbException("The daoInterface " + daoInterface.getCanonicalName() + " must be an interface.");
        }

        // noinspection unchecked
        return (T) Proxy.newProxyInstance(
                daoInterface.getClassLoader(),
                new Class<?>[]{daoInterface},
                new DaoMethodInvocationHandler(daoInterface, tx)
        );
    }

    private DaoFactory() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate DaoFactory.");
    }
}
