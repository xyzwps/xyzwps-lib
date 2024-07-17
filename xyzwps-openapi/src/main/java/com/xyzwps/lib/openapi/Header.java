package com.xyzwps.lib.openapi;

public class Header implements OASElement {

    private String description;
    private boolean required = false;
    private boolean deprecated = false;
    private Schema schema;
    private Style style;

    public String description() {
        return description;
    }

    public Header description(String description) {
        this.description = description;
        return this;
    }

    public Style style() {
        return style;
    }

    public Header style(Style style) {
        this.style = style;
        return this;
    }

    public Schema schema() {
        return schema;
    }

    public Header schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public boolean deprecated() {
        return deprecated;
    }

    public Header deprecated(boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public boolean required() {
        return required;
    }

    public Header required(boolean required) {
        this.required = required;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}

