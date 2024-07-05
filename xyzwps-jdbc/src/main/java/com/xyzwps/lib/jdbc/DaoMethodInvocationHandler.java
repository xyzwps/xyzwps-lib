package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.bedrock.BeanParam;
import com.xyzwps.lib.bedrock.Param;
import com.xyzwps.lib.bedrock.UnimplementedException;
import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.jdbc.method2sql.MethodName2Sql;
import com.xyzwps.lib.jdbc.method2sql.SqlInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

// TODO: try to use cache to improve performance
record DaoMethodInvocationHandler(Class<?> daoInterface, TransactionContext ctx) implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var $query = method.getAnnotation(Query.class);
        if ($query != null) {
            return handleQuery(ctx, $query.sql(), method, args);
        }

        var $execute = method.getAnnotation(Execute.class);
        if ($execute != null) {
            return handleExecute(ctx, $execute.value(), method, args);
        }

        var $table = daoInterface.getAnnotation(Table.class);
        if ($table != null) {
            var tableName = $table.value();
            if (tableName == null || tableName.isBlank()) {
                throw new DbException("The table name of " + daoInterface.getCanonicalName() + " must be specified.");
            }
            return handleAutomatedExecution(ctx, tableName, method, args);
        }

        throw new DbException(defaultErrorMessage(method, daoInterface));
    }

    private static String defaultErrorMessage(Method method, Class<?> daoInterface) {
        return String.format("Maybe you should add @Query or @Execute annotation to method %s, or add @Table annotation to type %s.",
                method.getName(), daoInterface.getCanonicalName());
    }

    private static Object handleAutomatedExecution(TransactionContext ctx, String tableName, Method method, Object[] args) throws SQLException {
        var methodName = switch (method.getName()) {
            case "findAll" -> "find";
            case "getAll" -> "get";
            case "countAll" -> "count";
            case "updateAll" -> "update";
            case "deleteAll" -> "delete";
            default -> method.getName();
        };

        SqlInfo sqlInfo;
        try {
            sqlInfo = MethodName2Sql.getSql(methodName, tableName);
        } catch (Exception e) {
            throw new DbException("Cannot generate SQL for method " + methodName + " in " + method.getDeclaringClass().getCanonicalName() + ".", e);
        }

        if (sqlInfo == null) {
            throw new IllegalStateException("Maybe a bug!");
        }


        if (sqlInfo.placeholderIsIn().hasTrue()) {
            throw UnimplementedException.todo(); // TODO: 支持 where in
        } else {
            return switch (sqlInfo.sqlType()) {
                case SELECT, COUNT -> handleQuery(ctx, sqlInfo.sql(), method, args);
                case UPDATE, DELETE -> handleExecute(ctx, sqlInfo.sql(), method, args);
            };
        }
    }

    private static Object handleQuery(TransactionContext ctx, String sql, Method method, Object[] args) throws SQLException {
        var returnType = determineQueryReturnType(method);
        var resultType = returnType.first();
        var elementType = returnType.second();

        try (var statement = execute(ctx, sql, method, args, false); var rs = statement.getResultSet()) {
            var list = ctx.rs2b().toList(rs, elementType);
            return switch (resultType) {
                case SINGLE -> list.isEmpty() ? DefaultValues.get(elementType) : list.getFirst();
                case OPTIONAL -> list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
                case LIST -> list;
                case LINKED_LIST -> new LinkedList<>(list);
            };
        }
    }

    private static Object handleExecute(TransactionContext ctx, String sql, Method method, Object[] args) throws SQLException {
        var returnType = determineExecuteReturnType(method);
        var resultType = returnType.first();
        var elementType = returnType.second();

        if (resultType == ExecuteResultType.BATCH) {
            var list = getListArgument(args);
            var nps = NamedPreparedSql.create(sql);
            try (var s = ctx.prepareStatement(nps.sql())) {
                for (var it : list) {
                    setNamedPreparedStatementArgs(s, nps.names(), it);
                    s.addBatch();
                }
                s.executeBatch();
                return null;
            }
        }

        try (var statement = execute(ctx, sql, method, args, resultType == ExecuteResultType.GENERATED_KEYS)) {
            return switch (resultType) {
                case VOID -> null;
                case AFFECTED_ROWS -> statement.getUpdateCount();
                case GENERATED_KEYS -> {
                    try (var rs = statement.getGeneratedKeys()) {
                        if (rs.next()) {
                            if (int.class.equals(elementType) || Integer.class.equals(elementType)) {
                                yield rs.getInt(1);
                            } else if (long.class.equals(elementType) || Long.class.equals(elementType)) {
                                yield rs.getLong(1);
                            } else {
                                throw new DbException("Unsupported return type of generated keys in execute method " + method.getName() + ".");
                            }
                        } else {
                            yield DefaultValues.get(elementType);
                        }
                    }
                }
                case BATCH -> throw new DbException("Maybe a bug");
            };
        }
    }

    private static List<?> getListArgument(Object[] args) {
        Object arg0 = args[0];
        if (arg0 instanceof List<?> list) {
            return list;
        }
        throw new DbException("The first argument of batch execute method must be a List.");
    }

    private static Pair<ExecuteResultType, Class<?>> determineExecuteReturnType(Method method) {
        var returnType = method.getGenericReturnType();
        if (method.getAnnotation(Batch.class) != null) {
            if (returnType instanceof Class<?> clazz) {
                if (!clazz.getName().equals("void")) {
                    throw new DbException("The return type of batch execute method " + method.getName() + " must be void.");
                }
                var params = method.getParameters();
                if (params.length != 1) {
                    throw new DbException("The batch execute method " + method.getName() + " must have only one parameter.");
                }
                var paramType = params[0].getParameterizedType();
                if (paramType instanceof Class<?> c && List.class.isAssignableFrom(c)) {
                    return Pair.of(ExecuteResultType.BATCH, null);
                } else if (paramType instanceof ParameterizedType pt) {
                    var rawType = pt.getRawType();
                    if (rawType instanceof Class<?> c && List.class.isAssignableFrom(c)) {
                        return Pair.of(ExecuteResultType.BATCH, clazz);
                    }
                }
                throw new DbException("The parameter of batch execute method " + method.getName() + " must be a List.");
            }
            throw new DbException("The return type of batch execute method " + method.getName() + " must be void.");
        }

        if (returnType instanceof Class<?> clazz) {
            if (clazz.getName().equals("void")) {
                return Pair.of(ExecuteResultType.VOID, null);
            }
            if (method.getAnnotation(GeneratedKeys.class) != null) {
                return Pair.of(ExecuteResultType.GENERATED_KEYS, clazz);
            }
            if (clazz.equals(int.class) || clazz.equals(Integer.class) || clazz.equals(long.class) || clazz.equals(Long.class)) {
                return Pair.of(ExecuteResultType.AFFECTED_ROWS, clazz);
            }
        }

        throw new DbException("Unsupported return type of execute method " + method.getName() + ".");
    }

    private static Pair<QueryResultType, Class<?>> determineQueryReturnType(Method method) {
        var returnType = method.getGenericReturnType();

        if (returnType instanceof Class<?> clazz) {
            if (clazz.getName().equals("void")) {
                throw new DbException("The return type of query method " + method.getName() + " cannot be void.");
            }
            return Pair.of(QueryResultType.SINGLE, clazz);
        }

        if (returnType instanceof ParameterizedType pt) {
            //noinspection ExtractMethodRecommender
            var rawType = pt.getRawType();
            QueryResultType resultType;
            //noinspection IfCanBeSwitch
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

    private static Statement execute(TransactionContext ctx, String sql, Method method, Object[] args, boolean returnAutoGeneratedKeys) throws SQLException {
        if (args == null || args.length == 0) {
            var s = ctx.createStatement();
            s.execute(sql);
            return s;
        }

        var nps = NamedPreparedSql.create(sql);
        var ps = ctx.prepareStatement(nps.sql(), returnAutoGeneratedKeys);
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

        setNamedPreparedStatementArgs(ps, names, map);
    }


    private static void setNamedPreparedStatementArgs(PreparedStatement ps, List<String> names, Object pathToValues) throws SQLException {
        Object[] theArgs = new Object[names.size()];
        for (int i = 0; i < names.size(); i++) {
            var path = names.get(i);
            theArgs[i] = ObjectPaths.getValue(pathToValues, path);
        }
        setPreparedStatementArgs(ps, theArgs);
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
                    if (v.arg() == null) ps.setObject(index, null);
                    else v.mapper().fromProperty(ps, index, v.arg());
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

    enum ExecuteResultType {
        VOID,
        BATCH,
        AFFECTED_ROWS,
        GENERATED_KEYS
    }
}
