package com.xyzwps.lib.jdbc.methodinvocation;

import com.xyzwps.lib.jdbc.DbException;

import java.lang.reflect.InvocationTargetException;

public class InstanceUtils {

    public static <T> T createInstanceFromDefaultConstructor(Class<T> clazz) {
        try {
            var constructor = clazz.getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new DbException("No default constructor defined for Class " + clazz.getCanonicalName(), e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DbException("Cannot create instance from default constructor of " + clazz.getCanonicalName(), e);
        }
    }
}
