package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

@Getter
public class ArgumentElement implements Element {

    private final FullTypeNameElement type;
    private final String name;

    public ArgumentElement(FullTypeNameElement type, String name) {
        this.type = type;
        this.name = name;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
