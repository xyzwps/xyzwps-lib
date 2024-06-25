package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.bedrock.lang.DefaultValues;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public final class DaoFactory {

    public static <T> T createDao(Class<T> daoInterface, ResultSetToBean rs2b, Connection conn) {
        Objects.requireNonNull(daoInterface);

        if (!daoInterface.isInterface()) {
            throw new IllegalArgumentException("The daoInterface " + daoInterface.getCanonicalName() + " must be an interface.");
        }

        InvocationHandler handler = (proxy, method, args) -> {
            var $query = method.getAnnotation(Query.class);
            if ($query != null) {
                return handleQuery(method, args, rs2b, conn, $query.sql());
            }

            var $execute = method.getAnnotation(Execute.class);
            if ($execute != null) {
                return handleExecute(method, args, conn, $execute.sql());
            }

            throw new IllegalStateException("No annotation found for method " + method.getName() + " in " + daoInterface.getCanonicalName() + ".");
        };

        // noinspection unchecked
        return (T) Proxy.newProxyInstance(daoInterface.getClassLoader(), new Class<?>[]{daoInterface}, handler);
    }

    private static Object handleQuery(Method method, Object[] args, ResultSetToBean rs2b, Connection conn, String sql) throws SQLException {
        var returnType = method.getGenericReturnType();
        int resultType = -1; // 0: single, 1: list, 2: linked list

        Class<?> elementType = null;
        if (returnType instanceof Class<?> clazz) {
            if (clazz.isAssignableFrom(Void.class)) {
                throw new IllegalArgumentException("The return type of method " + method.getName() + " must not be void.");
            }
            elementType = clazz;
            resultType = 0;
        } else if (returnType instanceof ParameterizedType pt) {
            var rawType = pt.getRawType();
            if (rawType.equals(Iterable.class) || rawType.equals(Collection.class) || rawType.equals(List.class) || rawType.equals(ArrayList.class)) {
                resultType = 1;
            } else if (rawType.equals(LinkedList.class)) {
                resultType = 2;
            } else {
                throw new IllegalArgumentException("Unsupported return type of method " + method.getName() + ".");
            }

            var firstArg = pt.getActualTypeArguments()[0];
            if (firstArg instanceof Class<?> clazz) {
                elementType = clazz;
            } else {
                throw new IllegalArgumentException("Unsupported return type of method " + method.getName() + ".");
            }
        } else {
            throw new IllegalArgumentException("Unsupported return type of method " + method.getName() + ".");
        }

        var list = rs2b.toList(conn.createStatement().executeQuery(sql), elementType);
        return switch (resultType) {
            case 0 -> list.isEmpty() ? DefaultValues.get(elementType) : list.getFirst();
            case 1 -> list;
            case 2 -> new LinkedList<>(list);
            default -> throw new IllegalStateException("Maybe a bug");
        };
    }

    private static Object handleExecute(Method method, Object[] args, Connection conn, String sql) {
        throw new IllegalArgumentException("TODO: unfinished implementation");
    }
}
