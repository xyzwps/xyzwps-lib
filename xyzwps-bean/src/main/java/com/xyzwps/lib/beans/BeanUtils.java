package com.xyzwps.lib.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class BeanUtils {

    public static BeanInfo getBeanInfo(Object object) {
        if (object == null) throw new NullPointerException();

        return (object instanceof Class<?>)
                ? getBeanInfoFromClass((Class<?>) object)
                : getBeanInfoFromObject(object);
    }

    private static final ConcurrentMap<Class<?>, BeanInfo> beanInfoCache = new ConcurrentHashMap<>();

    private static BeanInfo getBeanInfoFromClass(Class<?> beanClass) {
        BeanInfo beanInfo = beanInfoCache.get(beanClass);
        if (beanInfo != null) {
            return beanInfo;
        }
        beanInfo = BeanInfoAnalyser.create(beanClass).analyse();
        beanInfoCache.put(beanClass, beanInfo);
        return beanInfo;
    }

    private static BeanInfo getBeanInfoFromObject(Object bean) {
        return getBeanInfoFromClass(bean.getClass());
    }


    public static <T> T getPropertyOrNull(Object object, String propertyName) {
        return getBeanInfo(object).getPropertyOrNull(object, propertyName);
    }

    public static void setPropertyOrIgnore(Object object, String propertyName, Object value) {
        getBeanInfo(object).setPropertyOrIgnore(object, propertyName, value);
    }

    public static SetResult setProperty(Object object, String propertyName, Object value) {
        return getBeanInfo(object).setProperty(object, propertyName, value);
    }

    public static GetResult getProperty(Object object, String propertyName) {
        return getBeanInfo(object).getProperty(object, propertyName);
    }

    public static Map<String, Object> getProperties(Object object) {
        var beanInfo = getBeanInfo(object);
        var result = new HashMap<String, Object>();
        for (var property : beanInfo.getBeanProperties()) {
            if (property.readable()) {
                var getResult = property.getProperty(object);
                if (getResult instanceof GetResult.Ok ok) {
                    result.put(property.propertyName(), ok.value());
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
