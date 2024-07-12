package com.xyzwps.lib.jdbc.methodinvocation;

import com.xyzwps.lib.bedrock.lang.DefaultValues;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

enum QueryResultType implements BiFunction<List<?>, Class<?>, Object> {
    // TODO: 支持数组
    SINGLE {
        @Override
        public Object apply(List<?> list, Class<?> elementType) {
            return list.isEmpty() ? DefaultValues.get(elementType) : list.getFirst();
        }
    },
    LIST {
        @Override
        public Object apply(List<?> list, Class<?> elementType) {
            return list;
        }
    },
    LINKED_LIST {
        @Override
        public Object apply(List<?> list, Class<?> elementType) {
            return new LinkedList<>(list);
        }
    },
    OPTIONAL {
        @Override
        public Object apply(List<?> list, Class<?> elementType) {
            return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
        }
    };
}