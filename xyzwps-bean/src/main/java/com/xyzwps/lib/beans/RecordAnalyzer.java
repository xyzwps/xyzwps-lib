package com.xyzwps.lib.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.RecordComponent;
import java.util.ArrayList;
import java.util.Arrays;

record RecordAnalyzer(Class<?> beanClass) implements BeanInfoAnalyser {

    @Override
    public BeanInfo analyse() {
        var rcs = beanClass.getRecordComponents();
        var props = new ArrayList<PropertyInfo>(rcs.length);
        for (var rc : rcs) {
            var getMethod = new PropertyMethod.GetPropertyMethod(beanClass,
                    rc.getAccessor(), AccessLevel.PUBLIC, rc.getGenericType(), rc.getName());
            var getter = PropertyGetter.create(getMethod, null);
            var prop = new PropertyInfo(rc.getName(), rc.getGenericType(), getter, null, beanClass);
            props.add(prop);
        }
        return new BeanInfo(beanClass, getConstructor(), props, true);
    }

    private Constructor<?> getConstructor() {
        var rcs = beanClass.getRecordComponents();
        Class<?>[] paramTypes = Arrays.stream(rcs).map(RecordComponent::getType).toArray(Class[]::new);
        try {
            return beanClass.getConstructor(paramTypes);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("No corresponding constructor in record " + beanClass.getCanonicalName(), e);
        }
    }
}
