package com.xyzwps.lib.jdbc.methodinvocation;

import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.jdbc.*;
import com.xyzwps.lib.jdbc.method2sql.MethodName2Sql;
import com.xyzwps.lib.jdbc.method2sql.SqlInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

// TODO: try to use cache to improve performance
public
record DaoMethodInvocationHandler(Class<?> daoInterface,
                                  Supplier<TransactionContext> ctxGetter) implements InvocationHandler {

    private static final Object[] EMPTY_ARGS = new Object[0];

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try (var ctx = ctxGetter.get()) {
            if (args == null) {
                args = EMPTY_ARGS;
            }

            var $query = method.getAnnotation(Query.class);
            if ($query != null) {
                return handleQuery(ctx, $query.value(), method, args);
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

        return switch (sqlInfo.sqlType()) {
            case SELECT, COUNT -> handleQuery(ctx, sqlInfo.sql(), method, args);
            case UPDATE, DELETE -> handleExecute(ctx, sqlInfo.sql(), method, args);
        };
    }

    private static Object handleQuery(TransactionContext ctx, String sql, Method method, Object[] args) throws SQLException {
        var returnType = QueryResultInfo.from(method);
        var resultType = returnType.resultType();
        var elementType = returnType.elementType();

        try (var statement = execute(ctx, sql, method, args, false); var rs = statement.getResultSet()) {
            var list = ctx.rs2b().toList(rs, elementType);
            return resultType.apply(list, elementType);
        }
    }

    private static Object handleExecute(TransactionContext ctx, String sql, Method method, Object[] args) throws SQLException {
        var returnType = ExecuteResultInfo.from(method);
        var resultType = returnType.resultType();
        var elementType = returnType.elementType();

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

    private static Statement execute(TransactionContext ctx, String sql, Method method, Object[] args, boolean returnAutoGeneratedKeys) throws SQLException {
        if (args == null || args.length == 0) {
            var s = ctx.createStatement();
            s.execute(sql);
            return s;
        }

        var result = SqlMatchArguments.match(sql, args, method);
        var ps = ctx.prepareStatement(result.left(), returnAutoGeneratedKeys);
        setPreparedStatementArgs(ps, result.right().toArray());
        ps.execute();
        return ps;
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

}
