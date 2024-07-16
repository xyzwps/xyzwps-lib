package com.xyzwps.lib.openapi;

import lombok.Getter;

@Getter
public class Tag implements OASElement {
    private final String name;
    private String description;
    private ExternalDocumentation externalDocs;

    public Tag(String name) {
        this.name = name;
    }

    public Tag setDescription(String description) {
        this.description = description;
        return this;
    }

    public Tag setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
