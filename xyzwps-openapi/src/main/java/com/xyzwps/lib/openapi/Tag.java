package com.xyzwps.lib.openapi;


public class Tag implements OASElement {
    private final String name;
    private String description;
    private ExternalDocumentation externalDocs;

    public Tag(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Tag description(String description) {
        this.description = description;
        return this;
    }

    public ExternalDocumentation externalDocs() {
        return externalDocs;
    }

    public Tag externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
