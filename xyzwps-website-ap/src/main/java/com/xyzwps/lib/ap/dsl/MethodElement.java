package com.xyzwps.lib.ap.dsl;

public final class MethodElement implements Element {
    @Override
    public void visit(ElementVisitor visitor) {
        visitor.visit(this);
    }
}
