package com.xyzwps.lib.openapi;

public class Schema implements OASElement {

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
