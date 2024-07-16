package com.xyzwps.lib.openapi;

public class Paths implements OASElement {
    // TODO:

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
