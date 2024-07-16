package com.xyzwps.lib.openapi;

public class License implements OASElement {
    // TODO:

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
