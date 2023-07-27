package com.xyzwps.lib.beans;

import java.lang.annotation.Annotation;
import java.util.function.BiConsumer;

public class AnnotationsInfo {

    private final PropertyMethod.GetPropertyMethod getMethod;

    private final PropertyMethod.SetPropertyMethod setMethod;

    private final SpringField springField;

    public AnnotationsInfo(GetterInfo getterInfo, SetterInfo setterInfo) {
        if (getterInfo == null && setterInfo == null) {
            throw new IllegalArgumentException();
        }
        this.getMethod = getterInfo == null ? null : getterInfo.getGetMethod();
        this.setMethod = setterInfo == null ? null : setterInfo.getSetMethod();
        this.springField = getterInfo != null ? getterInfo.getField() : setterInfo.getField();
    }

    public void forEach(BiConsumer<Annotation, From> consumer) {
        for (var anno : springField.getField().getAnnotations()) {
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
