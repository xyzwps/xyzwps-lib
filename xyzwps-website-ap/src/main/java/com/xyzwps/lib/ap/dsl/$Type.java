package com.xyzwps.lib.ap.dsl;

public record $Type(String packageName, String className) implements Element {

    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }

    public String getFullName() {
        return packageName + "." + className;
    }
}
