package com.xyzwps.lib.openapi;

public class Responses implements OASElement {
    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
