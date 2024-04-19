package com.xyzwps.lib.bedrock.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeRef<T> {

    public final Type type;

    protected TypeRef() {
        var type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType t) {
            this.type = t.getActualTypeArguments()[0];
        } else {
            throw new IllegalStateException();
        }
    }
}
