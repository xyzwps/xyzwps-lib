package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class AnnotationElement implements Element {

    public final String annoPackageName;
    public final String annoClassName;

    private final Map<String, String> values = new HashMap<>();

    public AnnotationElement(String annoPackageName, String annoClassName) {
        this.annoPackageName = annoPackageName;
        this.annoClassName = annoClassName;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public AnnotationElement set(String name, String value) {
        values.put(name, value);
        return this;
    }
}
