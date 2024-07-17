package com.xyzwps.lib.openapi;

public class Example implements OASElement {

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
