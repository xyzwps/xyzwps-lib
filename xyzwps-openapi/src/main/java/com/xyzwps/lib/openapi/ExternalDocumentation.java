package com.xyzwps.lib.openapi;

import lombok.Getter;

@Getter
public class ExternalDocumentation implements OASElement {
    private final String url;
    private String description;

    public ExternalDocumentation(String url) {
        this.url = url;
    }

    public ExternalDocumentation setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
