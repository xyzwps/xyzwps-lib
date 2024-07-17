package com.xyzwps.lib.openapi;

public class RequestBody implements OASElement {
    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
