package com.xyzwps.lib.openapi;

import java.util.Map;
import java.util.TreeMap;

public class Example implements OASElement {

    private String summary;
    private String description;
    private Object value;
    private String externalValue;


    public String summary() {
        return summary;
    }

    public Example summary(String summary) {
        this.summary = summary;
        return this;
    }

    public String description() {
        return description;
    }

    public Example description(String description) {
        this.description = description;
        return this;
    }

    public Object value() {
        return value;
    }

    public Example value(String value) {
        this.value = value;
        return this;
    }

    // FIXME: 这个东西略微奇怪，想办法表达一下 Any
    public Example value(Map<String, String> value) {
        this.value = value == null ? null : new TreeMap<>(value);
        return this;
    }

    public String externalValue() {
        return externalValue;
    }

    public Example externalValue(String externalValue) {
        this.externalValue = externalValue;
        return this;
    }


    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
