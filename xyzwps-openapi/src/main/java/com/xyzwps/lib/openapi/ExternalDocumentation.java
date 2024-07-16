package com.xyzwps.lib.openapi;


public class ExternalDocumentation implements OASElement {
    private final String url;
    private String description;

    public ExternalDocumentation(String url) {
        this.url = url;
    }

    public String url() {
        return url;
    }

    public String description() {
        return description;
    }

    public ExternalDocumentation description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
