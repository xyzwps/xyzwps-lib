package com.xyzwps.lib.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

import static com.xyzwps.lib.beans.PropertyMethod.*;

class PropertyMethodDecider {

    static PropertyMethod decide(Class<?> beanType, Method method) {
        var modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) return NONE;

        var methodName = method.getName();
        if ("getClass".equals(methodName)) return NONE;

        var methodAccessLevel = AccessLevel.fromModifiers(modifiers);
        if (methodAccessLevel == AccessLevel.PRIVATE) return NONE;
        if (methodAccessLevel == AccessLevel.PROTECTED) return NONE;

        var parameterCount = method.getParameterCount();
        if (setterPattern.matcher(methodName).matches()) {
            if (parameterCount != 1) return NONE;
            var type = method.getGenericParameterTypes()[0];
            return new SetMethod(beanType, type, method, extractPropertyName(3, methodName));
        }

        var returnType = method.getGenericReturnType();

        if (getterPattern.matcher(methodName).matches()) {
            if (parameterCount > 0) return NONE;
            if (returnType == Void.class) return NONE;
            var type = method.getGenericReturnType();
            return new GetMethod(beanType, type, method, extractPropertyName(3, methodName));
        }

        if (isserPattern.matcher(methodName).matches()) {
            if (parameterCount > 0) return NONE;
            if (returnType == Void.class) return NONE;
            if (returnType != boolean.class) return NONE;

            return new GetMethod(beanType, boolean.class, method, extractPropertyName(2, methodName));
        }

        return NONE;
    }

    static Pattern getterPattern = Pattern.compile("^get[A-Z][A-Za-z0-9]*");
    static Pattern setterPattern = Pattern.compile("^set[A-Z][A-Za-z0-9]*");
    static Pattern isserPattern = Pattern.compile("^is[A-Z][A-Za-z0-9]*");

    static String extractPropertyName(int skip, String methodName) {
        return Character.toLowerCase(methodName.charAt(skip)) + methodName.substring(skip + 1);
    }
}
