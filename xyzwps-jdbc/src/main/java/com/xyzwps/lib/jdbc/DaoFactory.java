package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.bedrock.lang.DefaultValues;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        QueryResultType resultType;

        Class<?> elementType;
        if (returnType instanceof Class<?> clazz) {
            if (clazz.isAssignableFrom(Void.class)) {
                throw new IllegalArgumentException("The return type of method " + method.getName() + " must not be void.");
            }
            elementType = clazz;
            resultType = QueryResultType.SINGLE;
        } else if (returnType instanceof ParameterizedType pt) {
            var rawType = pt.getRawType();
            if (rawType.equals(Iterable.class) || rawType.equals(Collection.class) || rawType.equals(List.class) || rawType.equals(ArrayList.class)) {
                resultType = QueryResultType.LIST;
            } else if (rawType.equals(LinkedList.class)) {
                resultType = QueryResultType.LINKED_LIST;
            } else if (rawType.equals(Optional.class)) {
                resultType = QueryResultType.OPTIONAL;
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

        // TODO: support count
        ResultSet resultSet;
        if (args == null || args.length == 0) {
            resultSet = conn.createStatement().executeQuery(sql);
        } else {
            var ps = conn.prepareStatement(sql);
            setPreparedStatementArgs(ps, args);
            resultSet = ps.executeQuery();
        }

        var list = rs2b.toList(resultSet, elementType);
        return switch (resultType) {
            case SINGLE -> list.isEmpty() ? DefaultValues.get(elementType) : list.getFirst();
            case OPTIONAL -> list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
            case LIST -> list;
            case LINKED_LIST -> new LinkedList<>(list);
        };
    }

    private static void setPreparedStatementArgs(PreparedStatement ps, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; i++) {
            final int index = i + 1;
            var it = args[i];
            switch (it) {
                case null -> ps.setObject(index, null);
                case String v -> ps.setString(index, v);
                case Short v -> ps.setShort(index, v);
                case Integer v -> ps.setInt(index, v);
                case Long v -> ps.setLong(index, v);
                case Float v -> ps.setFloat(index, v);
                case Double v -> ps.setDouble(index, v);
                case Boolean v -> ps.setBoolean(index, v);
                // TODO: 支持更多类型
                default -> ps.setObject(index, it);
            }
        }
    }


    enum QueryResultType {
        SINGLE,
        LIST,
        LINKED_LIST,
        OPTIONAL
    }

    private static Object handleExecute(Method method, Object[] args, Connection conn, String sql) {
        throw new IllegalArgumentException("TODO: unfinished implementation");
    }
}
