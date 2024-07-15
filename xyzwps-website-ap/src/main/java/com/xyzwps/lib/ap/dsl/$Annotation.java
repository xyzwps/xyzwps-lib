package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public final class $Annotation implements Element {

    public final $Type type;

    private final Map<String, String> values = new HashMap<>();

    $Annotation($Type type) {
        this.type = type;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public $Annotation set(String name, String value) {
        values.put(name, value);
        return this;
    }
}
