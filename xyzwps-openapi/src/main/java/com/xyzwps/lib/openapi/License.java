package com.xyzwps.lib.openapi;

public sealed interface License extends OASElement {

    String name();

    record IdLicense(String name, String identifier) implements License {
    }

    record UrlLicense(String name, String url) implements License {
    }

    @Override
    default void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
