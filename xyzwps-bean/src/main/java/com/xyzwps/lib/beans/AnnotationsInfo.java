package com.xyzwps.lib.beans;

import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;

public class AnnotationsInfo {

    private final PropertyMethod.GetPropertyMethod getMethod;

    private final PropertyMethod.SetPropertyMethod setMethod;

    private final PropertyField propertyField;

    public AnnotationsInfo(PropertyGetter propertyGetter, PropertySetter propertySetter) {
        if (propertyGetter == null && propertySetter == null) {
            throw new IllegalArgumentException();
        }
        this.getMethod = propertyGetter == null ? null : propertyGetter.getGetMethod();
        this.setMethod = propertySetter == null ? null : propertySetter.getSetMethod();
        this.propertyField = propertyGetter != null ? propertyGetter.getField() : propertySetter.getField();
    }

    public void forEach(BiConsumer<Annotation, From> consumer) {
        for (var anno : propertyField.getField().getAnnotations()) {
            consumer.accept(anno, From.FIELD);
        }
        if (getMethod != null) {
            for (var anno : getMethod.method().getAnnotations()) {
                consumer.accept(anno, From.GET_METHOD);
            }
        }
        if (setMethod != null) {
            for (var anno : setMethod.method().getAnnotations()) {
                consumer.accept(anno, From.SET_METHOD);
            }
        }

    }

    public enum From {
        FIELD,
        GET_METHOD,
        SET_METHOD
    }


}
