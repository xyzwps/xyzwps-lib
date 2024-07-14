package com.xyzwps.lib.ap.dsl;

import lombok.Getter;

@Getter
public class FullTypeNameElement implements Element {

    private final String packageName;
    private final String className;

    public FullTypeNameElement(String packageName, String className) {
        this.packageName = packageName;
        this.className = className;
    }

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
