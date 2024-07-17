package com.xyzwps.lib.openapi;

import com.xyzwps.lib.bedrock.Args;

import java.util.Objects;

public class Parameter implements OASElement {

    private final String name;
    private final In in;
    private String description;
    private boolean required = false;
    private boolean deprecated = false;
    private Schema schema;
    private Style style;

    public Parameter(String name, In in) {
        this.name = Args.notNull(name, "name must not be null");
        this.in = Args.notNull(in, "in must not be null");
        if (in == In.PATH) {
            this.required = true;
        }
    }

    public Style style() {
        return style;
    }

    public Parameter style(Style style) {
        this.style = style;
        return this;
    }

    public Schema schema() {
        return schema;
    }

    public Parameter schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public boolean deprecated() {
        return deprecated;
    }

    public Parameter deprecated(boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public boolean required() {
        return required;
    }

    public Parameter required(boolean required) {
        if (in == In.PATH) {
            this.required = true;
        } else {
            this.required = required;
        }
        return this;
    }

    public String name() {
        return name;
    }

    public In in() {
        return in;
    }

    public String description() {
        return description;
    }

    public Parameter description(String description) {
        this.description = description;
        return this;
    }


    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
