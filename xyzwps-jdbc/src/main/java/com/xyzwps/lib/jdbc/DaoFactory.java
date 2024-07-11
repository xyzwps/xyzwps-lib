package com.xyzwps.lib.jdbc;

import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Supplier;

public final class DaoFactory {

    public static <T> T createDao(Class<T> daoInterface, Supplier<TransactionContext> transactionContextGetter) {
        Objects.requireNonNull(daoInterface);

        if (!daoInterface.isInterface()) {
            throw new DbException("The daoInterface " + daoInterface.getCanonicalName() + " must be an interface.");
        }

        // noinspection unchecked
        return (T) Proxy.newProxyInstance(
                daoInterface.getClassLoader(),
                new Class<?>[]{daoInterface},
                new DaoMethodInvocationHandler(daoInterface, transactionContextGetter)
        );
    }

    private DaoFactory() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate DaoFactory.");
    }
}
