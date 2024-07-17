package com.xyzwps.lib.openapi;

public class Reference implements OASElement {
    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
