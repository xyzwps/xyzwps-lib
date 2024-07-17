package com.xyzwps.lib.openapi;

public class Encoding implements OASElement {

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
