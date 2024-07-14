package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class AnnotationElement implements Element {

    public final FullTypeNameElement type;

    private final Map<String, String> values = new HashMap<>();

    public AnnotationElement(FullTypeNameElement type) {
        this.type = type;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this, false);
    }

    public AnnotationElement set(String name, String value) {
        values.put(name, value);
        return this;
    }
}
