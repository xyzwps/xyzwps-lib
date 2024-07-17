package com.xyzwps.lib.openapi;

public class Parameter implements OASElement {
    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
