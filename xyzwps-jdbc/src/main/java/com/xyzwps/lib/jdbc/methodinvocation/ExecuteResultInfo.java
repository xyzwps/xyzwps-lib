package com.xyzwps.lib.jdbc.methodinvocation;

import com.xyzwps.lib.jdbc.Batch;
import com.xyzwps.lib.jdbc.DbException;
import com.xyzwps.lib.jdbc.GeneratedKeys;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

record ExecuteResultInfo(ExecuteResultType resultType, Class<?> elementType) {

    static ExecuteResultInfo from(Method method) {
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
                    return new ExecuteResultInfo(ExecuteResultType.BATCH, null);
                } else if (paramType instanceof ParameterizedType pt) {
                    var rawType = pt.getRawType();
                    if (rawType instanceof Class<?> c && List.class.isAssignableFrom(c)) {
                        return new ExecuteResultInfo(ExecuteResultType.BATCH, null);
                    }
                }
                throw new DbException("The parameter of batch execute method " + method.getName() + " must be a List.");
            }
            throw new DbException("The return type of batch execute method " + method.getName() + " must be void.");
        }

        if (returnType instanceof Class<?> clazz) {
            if (clazz.getName().equals("void")) {
                return new ExecuteResultInfo(ExecuteResultType.VOID, null);
            }
            if (method.getAnnotation(GeneratedKeys.class) != null) {
                return new ExecuteResultInfo(ExecuteResultType.GENERATED_KEYS, clazz);
            }
            if (clazz.equals(int.class) || clazz.equals(Integer.class) || clazz.equals(long.class) || clazz.equals(Long.class)) {
                return new ExecuteResultInfo(ExecuteResultType.AFFECTED_ROWS, clazz);
            }
        }

        throw new DbException("Unsupported return type of execute method " + method.getName() + ".");
    }
}
