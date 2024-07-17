package com.xyzwps.lib.openapi;

public record Reference(String $ref) implements OASElement {


    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
