package com.xyzwps.lib.bedrock.lang;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class Types {

    public static boolean isParameterizedType(Type type) {
        return type instanceof ParameterizedType;
    }

    public static boolean isClass(Type type) {
        return type instanceof Class;
    }

    public static boolean isGenericArrayType(Type type) {
        return type instanceof GenericArrayType;
    }
}
