package com.xyzwps.lib.jdbc;

import com.xyzwps.lib.bedrock.BeanParam;
import com.xyzwps.lib.bedrock.Param;
import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.jdbc.method2sql.BooleanList;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

import static com.xyzwps.lib.dollar.Dollar.*;

class SqlMatchArguments {

    private static final Pattern PLACEHOLDER = Pattern.compile("(\\?|(\\(\\s?\\?\\s?\\)))");

    private static final Pattern IN_PLACEHOLDER_FULL = Pattern.compile("^\\(\\s?\\?\\s?\\)$");

    private static final Pattern IN_PLACEHOLDER = Pattern.compile("\\(\\s?\\?\\s?\\)");

    static Pair<String, List<Object>> match(String sql, Object[] args, Method method) {
        boolean isQuestSql = sql.contains("?");
        var npsql = NamedPreparedSql.create(sql);
        if (!npsql.names().isEmpty() && isQuestSql) {
            throw new DbException("You cannot mix ? and named parameters in the same SQL statement on method " + method.getName());
        }

        if (!npsql.names().isEmpty()) {
            args = handleNamedParameters(npsql.names(), method, args);
        }

        var matcher = PLACEHOLDER.matcher(npsql.sql());
        var placeholders = new BooleanList();
        while (matcher.find()) {
            var group = matcher.group();
            if (group.equals("?")) {
                placeholders.add(false);
            } else if (IN_PLACEHOLDER_FULL.matcher(group).matches()) {
                placeholders.add(true);
            } else {
                throw new IllegalStateException("Maybe a bug!");
            }
        }

        if (placeholders.size() != args.length) {
            throw new DbException("The number of arguments in method " + method.getName() + " does not match the number of placeholders in SQL.");
        }

        var formattedSql = $.replaceAll(npsql.sql(), IN_PLACEHOLDER, i -> "{" + i + "}");
        List<Object> newArgs = new ArrayList<>();
        List<String> formatArgs = new ArrayList<>();

        for (int i = 0; i < placeholders.size(); i++) {
            var isIn = placeholders.get(i);
            if (isIn) {
                var arg = args[i];
                if (arg == null) {
                    throw new DbException("The argument of method " + method.getName() + " cannot be null.");
                } else if (arg instanceof Collection<?> collection) {
                    if (collection.isEmpty()) {
                        throw new DbException("The collection argument of method " + method.getName() + " cannot be empty.");
                    } else {
                        newArgs.addAll(collection);
                        formatArgs.add("(" + String.join(", ", Collections.nCopies(collection.size(), "?")) + ")");
                    }
//                    } else if (arg instanceof Object[] array) { TODO: 支持数组
//                        newArgs.addAll(Arrays.asList(array));
                } else {
                    throw new DbException("The argument of method " + method.getName() + " must be a collection or an array.");
                }
            } else {
                newArgs.add(args[i]);
            }
        }

        return Pair.of(new MessageFormat(formattedSql).format(formatArgs.toArray()), newArgs);

    }

    private static final Pattern NAMED_PARAMS = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");

    private static Object[] handleNamedParameters(List<String> names, Method method, Object[] args) {
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
            theArgs[i] = ObjectPaths.getValue(map, path);
        }
        return theArgs;
    }


}
