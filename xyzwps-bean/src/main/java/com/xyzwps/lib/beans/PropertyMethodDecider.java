package com.xyzwps.lib.beans;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.regex.Pattern;

import static com.xyzwps.lib.beans.PropertyMethod.*;

public class PropertyMethodDecider {

    public static PropertyMethod decide(Class<?> beanType, Method method) {
        var modifiers = method.getModifiers();
        if (Modifier.isStatic(modifiers)) return NONE;

        var methodName = method.getName();
        if ("getClass".equals(methodName)) return NONE;

        var parameterCount = method.getParameterCount();
        var returnType = method.getGenericReturnType();
        var methodAccessLevel = AccessLevel.fromModifiers(modifiers);
        if (getterPattern.matcher(methodName).matches()) {
            if (parameterCount > 0) return NONE;
            if (returnType == Void.class) return NONE;

            return new GetPropertyMethod(beanType, method, methodAccessLevel, returnType, extractPropertyName(3, methodName));
        }

        if (isserPattern.matcher(methodName).matches()) {
            if (parameterCount > 0) return NONE;
            if (returnType == Void.class) return NONE;
            if (returnType != boolean.class) return NONE;

            return new GetPropertyMethod(beanType, method, methodAccessLevel, returnType, extractPropertyName(2, methodName));
        }

        if (setterPattern.matcher(methodName).matches()) {
            if (parameterCount != 1) return NONE;

            var parameterType = method.getGenericParameterTypes()[0];
            return new SetPropertyMethod(beanType, method, methodAccessLevel, parameterType, extractPropertyName(3, methodName));
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
