package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.beans.BeanUtils;
import com.xyzwps.lib.bedrock.BeanParam;
import com.xyzwps.lib.bedrock.Param;
import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.dollar.Pair;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

record DaoMethodInvocationHandler(Class<?> daoInterface, ResultSetToBean rs2b,
                                  Connection conn) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var $query = method.getAnnotation(Query.class);
        if ($query != null) {
            return handleQuery(method, args, rs2b, conn, $query.sql());
        }

        var $execute = method.getAnnotation(Execute.class);
        if ($execute != null) {
            return handleExecute(conn, $execute, method, args);
        }

        throw new DbException("No annotation found for method " + method.getName() + " in " + daoInterface.getCanonicalName() + ".");
    }


    private static Object handleQuery(Method method, Object[] args, ResultSetToBean rs2b, Connection conn, String sql) throws SQLException {
        var returnType = decideReturnType(method);
        var resultType = returnType.first();
        var elementType = returnType.second();

        try (var statement = execute(conn, sql, method, args)) {
            var rs = statement.getResultSet();
            var list = rs2b.toList(rs, elementType);
            return switch (resultType) {
                case SINGLE -> list.isEmpty() ? DefaultValues.get(elementType) : list.getFirst();
                case OPTIONAL -> list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
                case LIST -> list;
                case LINKED_LIST -> new LinkedList<>(list);
            };
        }
    }

    private static Object handleExecute(Connection conn, Execute execute, Method method, Object[] args) throws SQLException {
        // TODO: return affected rows
        // TODO: return generated keys
        try (var statement = execute(conn, execute.sql(), method, args)) {
            return null;
        }
    }

    private static Pair<QueryResultType, Class<?>> decideReturnType(Method method) {
        var returnType = method.getGenericReturnType();

        if (returnType instanceof Class<?> clazz) {
            if (clazz.isAssignableFrom(Void.class)) {
                throw new DbException("The return type of query method " + method.getName() + " cannot be void.");
            }
            return Pair.of(QueryResultType.SINGLE, clazz);
        }

        if (returnType instanceof ParameterizedType pt) {
            var rawType = pt.getRawType();
            QueryResultType resultType;
            if (rawType.equals(Iterable.class) || rawType.equals(Collection.class) || rawType.equals(List.class) || rawType.equals(ArrayList.class)) {
                resultType = QueryResultType.LIST;
            } else if (rawType.equals(LinkedList.class)) {
                resultType = QueryResultType.LINKED_LIST;
            } else if (rawType.equals(Optional.class)) {
                resultType = QueryResultType.OPTIONAL;
            } else {
                throw new DbException("Unsupported return type of query method " + method.getName() + ".");
            }

            var firstArg = pt.getActualTypeArguments()[0];
            if (firstArg instanceof Class<?> clazz) {
                return Pair.of(resultType, clazz);
            } else {
                throw new DbException("Unsupported return type of query method " + method.getName() + ".");
            }
        }

        throw new DbException("Unsupported return type of query method " + method.getName() + ".");
    }

    private static Statement execute(Connection conn, String sql, Method method, Object[] args) throws SQLException {
        if (args == null || args.length == 0) {
            var s = conn.createStatement();
            s.execute(sql);
            return s;
        }

        var nps = NamedPreparedSql.create(sql);
        var ps = conn.prepareStatement(nps.sql());
        if (nps.names().isEmpty()) {
            setPreparedStatementArgs(ps, args);
        } else {
            setNamedPreparedStatementArgs(ps, nps.names(), method, args);
        }
        ps.execute();
        return ps;
    }

    private static final Pattern NAMED_PARAMS = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    private static void setNamedPreparedStatementArgs(PreparedStatement ps, List<String> names, Method method, Object[] args) throws SQLException {
        var params = method.getParameters();
        var map = new HashMap<String, Object>();
        for (int i = 0; i < params.length; i++) {
            var p = params[i];

            var beanParam = p.getAnnotation(BeanParam.class);
            if (beanParam != null) {
                var name = beanParam.value();
                if (name != null && NAMED_PARAMS.matcher(name).matches()) {
                    map.put(name, args[i]);
                } else {
                    throw new DbException("The name of BeanParam must be a valid identifier: " + name + ".");
                }
                continue;
            }

            var param = p.getAnnotation(Param.class);
            if (param != null) {
                var name = param.value();
                if (name != null && NAMED_PARAMS.matcher(name).matches()) {
                    map.put(name, args[i]);
                } else {
                    throw new DbException("The name of Param must be a valid identifier: " + name + ".");
                }
                continue;
            }

            var name = p.getName();
            if (name != null) {
                map.put(name, args[i]);
            } else {
                throw new DbException(String.format("Cannot determine the name of parameter %d in method %s. You can use @BeanParam or @Param annotation, or use -parameters flag during compile time.", i, method.getName()));
            }
            map.put(names.get(i), args[i]);
        }

        Object[] theArgs = new Object[names.size()];
        for (int i = 0; i < names.size(); i++) {
            var path = names.get(i);
            theArgs[i] = getValueFromMap(map, path);
        }

        setPreparedStatementArgs(ps, theArgs);
    }

    private static Object getValueFromMap(Map<String, Object> map, String path) {
        if (map.containsKey(path)) {
            return map.get(path);
        }

        var segments = path.split("\\.");
        if (segments.length != 2) {
            throw new DbException("The named parameter " + path + " is not found in the map.");
        }

        String rootKey = segments[0], pathKey = segments[1];
        if (!map.containsKey(rootKey)) {
            throw new DbException("The named parameter " + path + " is not found in the map.");
        }

        var root = map.get(segments[0]);
        if (root instanceof Map<?, ?> rootMap) {
            return rootMap.get(pathKey);
        } else {
            var prop = BeanUtils.getBeanInfoFromObject(root).getPropertyInfo(pathKey)
                    .orElseThrow(() -> new DbException("The named parameter " + path + " is not found in the map."));
            var anno = prop.getAnnotation(Column.class);
            var mapper = anno == null ? null : anno.mapper();
            if (mapper == null) {
                return BeanUtils.getProperty(root, pathKey).getOrThrow();
            }

            if (ColumnPropertyMapper.class.isAssignableFrom(mapper)) {
                return new MappedArg(BeanUtils.getProperty(root, pathKey).getOrThrow(),
                        (ColumnPropertyMapper<?>) InstanceUtils.createInstanceFromDefaultConstructor(mapper));
            } else {
                throw new DbException("The mapper of the named parameter " + path + " is not a valid ColumnPropertyMapper.");
            }
        }
    }

    private record MappedArg<T>(T arg, ColumnPropertyMapper<T> mapper) {
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
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
                case Enum v -> ps.setString(index, v.name());
                case MappedArg v -> {
                    if (v.arg == null) ps.setObject(index, null);
                    else v.mapper().fromProperty(ps, index, v.arg);
                }
                // TODO: 支持更多类型
                // TODO: 支持 where in
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


}
