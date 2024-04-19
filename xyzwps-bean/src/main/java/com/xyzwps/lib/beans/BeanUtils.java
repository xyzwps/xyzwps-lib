package com.xyzwps.lib.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * TODO: 搞成可实例化的
 */
public final class BeanUtils {

    private static final ConcurrentMap<Class<?>, BeanInfo<?>> beanInfoCache = new ConcurrentHashMap<>();

    // TODO: 支持泛型类
    public static <T> BeanInfo<T> getBeanInfoFromClass(Class<T> beanClass) {
        var beanInfo = (BeanInfo<T>) beanInfoCache.get(beanClass);
        if (beanInfo != null) {
            return beanInfo;
        }
        beanInfo = BeanInfoAnalyser.create(beanClass).analyse();
        beanInfoCache.put(beanClass, beanInfo);
        return beanInfo;
    }

    public static <T> BeanInfo<T> getBeanInfoFromObject(T bean) {
        return getBeanInfoFromClass((Class<T>) bean.getClass());
    }


    public static <T> T getPropertyOrNull(Object object, String propertyName) {
        return (T) getBeanInfoFromObject(object).getPropertyOrNull(object, propertyName);
    }

    public static void setPropertyOrIgnore(Object object, String propertyName, Object value) {
        getBeanInfoFromObject(object).setPropertyOrIgnore(object, propertyName, value);
    }

    public static SetResult setProperty(Object object, String propertyName, Object value) {
        return getBeanInfoFromObject(object).setProperty(object, propertyName, value);
    }

    public static GetResult getProperty(Object object, String propertyName) {
        return getBeanInfoFromObject(object).getProperty(object, propertyName);
    }

    public static Map<String, Object> getProperties(Object object) {
        var beanInfo = getBeanInfoFromObject(object);
        var result = new HashMap<String, Object>();
        for (var property : beanInfo.getBeanProperties()) {
            if (property.readable()) {
                var getResult = property.get(object);
                if (getResult instanceof GetResult.Ok ok) {
                    result.put(property.name(), ok.value());
                } else {
                    throw new IllegalStateException("Impossible cases: " + getResult);
                }
            }
        }
        return result;
    }

    private BeanUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
