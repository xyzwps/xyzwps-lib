package com.xyzwps.lib.jdbc.methodinvocation;

import com.xyzwps.lib.jdbc.DbException;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

 record QueryResultInfo(QueryResultType resultType, Class<?> elementType) {

     static QueryResultInfo from(Method method) {
        var returnType = method.getGenericReturnType();

        if (returnType instanceof Class<?> clazz) {
            if (clazz.getName().equals("void")) {
                throw new DbException("The return type of query method " + method.getName() + " cannot be void.");
            }
            return new QueryResultInfo(QueryResultType.SINGLE, clazz);
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
                return new QueryResultInfo(resultType, clazz);
            } else {
                throw new DbException("Unsupported return type of query method " + method.getName() + ".");
            }
        }

        throw new DbException("Unsupported return type of query method " + method.getName() + ".");
    }
}
