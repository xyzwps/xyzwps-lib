package com.xyzwps.lib.openapi;

public interface OASElement {

    void accept(OAEVisitor visitor);
}
