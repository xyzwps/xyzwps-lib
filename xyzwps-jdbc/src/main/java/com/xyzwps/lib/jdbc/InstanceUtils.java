package com.xyzwps.lib.jdbc;

import java.lang.reflect.InvocationTargetException;

class InstanceUtils {

    static <T> T createInstanceFromDefaultConstructor(Class<T> clazz) {
        try {
            var constructor = clazz.getConstructor();
            return constructor.newInstance(); // TODO: 效率太低
        } catch (NoSuchMethodException e) {
            throw new DbException("No default constructor defined for Class " + clazz.getCanonicalName(), e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new DbException("Cannot create instance from default constructor of " + clazz.getCanonicalName(), e);
        }
    }
}
