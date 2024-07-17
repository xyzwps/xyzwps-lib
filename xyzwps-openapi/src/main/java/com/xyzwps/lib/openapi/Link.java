package com.xyzwps.lib.openapi;

public class Link implements OASElement {

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
