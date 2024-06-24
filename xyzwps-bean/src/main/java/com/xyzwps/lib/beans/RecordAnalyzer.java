package com.xyzwps.lib.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;

record RecordAnalyzer<T>(Class<T> beanClass) implements BeanInfoAnalyser<T> {

    @Override
    public BeanInfo<T> analyse() {
        var rcs = beanClass.getRecordComponents();
        var props = new ArrayList<PropertyInfo>(rcs.length);
        for (var rc : rcs) {
            var getter = new ImplGetter(rc.getAccessor(), rc.getName(), beanClass);
            var prop = new ImplPropertyInfo(rc.getName(), rc.getGenericType(), getter, null, true, false, beanClass);
            props.add(prop);
        }
        return new BeanInfo<T>(beanClass, getConstructor(), props, true);
    }

    private Constructor<T> getConstructor() {
        var rcs = beanClass.getRecordComponents();
        Class<?>[] paramTypes = Arrays.stream(rcs).map(RecordComponent::getType).toArray(Class[]::new);
        try {
            var constructor = beanClass.getDeclaredConstructor(paramTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No corresponding constructor in record " + beanClass.getCanonicalName(), e);
        }
    }
}
