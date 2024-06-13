package com.xyzwps.lib.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Bean utilities.
 */
public final class BeanUtils {

    private static final ConcurrentMap<Class<?>, BeanInfo<?>> beanInfoCache = new ConcurrentHashMap<>();

    /**
     * Get bean info from class.
     *
     * @param beanClass the class of the bean
     * @param <T>       the type of the bean
     * @return the bean info
     */
    @SuppressWarnings("unchecked")
    public static <T> BeanInfo<T> getBeanInfoFromClass(Class<T> beanClass) {
        var beanInfo = (BeanInfo<T>) beanInfoCache.get(beanClass);
        if (beanInfo != null) {
            return beanInfo;
        }
        beanInfo = BeanInfoAnalyser.create(beanClass).analyse();
        beanInfoCache.put(beanClass, beanInfo);
        return beanInfo;
    }

    /**
     * Get bean info from object.
     *
     * @param bean the object of the bean
     * @param <T>  the type of the bean
     * @return the bean info
     */
    @SuppressWarnings("unchecked")
    public static <T> BeanInfo<T> getBeanInfoFromObject(T bean) {
        return getBeanInfoFromClass((Class<T>) bean.getClass());
    }


    /**
     * Get property value or null.
     *
     * @param object       the object to get property value from
     * @param propertyName the name of the property
     * @param <T>          the type of the property
     * @return the property value or null
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPropertyOrNull(Object object, String propertyName) {
        return (T) getBeanInfoFromObject(object).getPropertyOrNull(object, propertyName);
    }

    /**
     * Set property or ignore.
     *
     * @param object       the object to set property value to
     * @param propertyName the name of the property
     * @param value        the value to set
     */
    public static void setPropertyOrIgnore(Object object, String propertyName, Object value) {
        getBeanInfoFromObject(object).setPropertyOrIgnore(object, propertyName, value);
    }

    /**
     * Set property.
     *
     * @param object       the object to set property value to
     * @param propertyName the name of the property
     * @param value        the value to set
     * @return the result of setting property
     */
    public static SetResult setProperty(Object object, String propertyName, Object value) {
        return getBeanInfoFromObject(object).setProperty(object, propertyName, value);
    }

    /**
     * Get property.
     *
     * @param object       the object to get property value from
     * @param propertyName the name of the property
     * @return the result of getting property value
     */
    public static GetResult getProperty(Object object, String propertyName) {
        return getBeanInfoFromObject(object).getProperty(object, propertyName);
    }

    /**
     * Get properties.
     *
     * @param object the object to get properties from
     * @return the properties
     */
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
