package com.xyzwps.lib.openapi;

public class Style implements OASElement {

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
