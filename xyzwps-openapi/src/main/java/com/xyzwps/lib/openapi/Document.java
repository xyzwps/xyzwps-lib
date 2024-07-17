package com.xyzwps.lib.openapi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Document implements OASElement {
    private final String openapi = "3.1.0";
    private final Info info;
    private final List<Server> servers = new ArrayList<>();
    private Paths paths;
    private final List<Tag> tags = new ArrayList<>();
    private ExternalDocumentation externalDocs;

    // TODO: webhooks
    // TODO: components
    // TODO: security

    public Document(Info info) {
        this.info = Objects.requireNonNull(info);
    }

    public String openapi() {
        return openapi;
    }

    public Info info() {
        return info;
    }

    public List<Server> servers() {
        return servers;
    }

    public Document addServer(Server server) {
        if (server != null) {
            servers.add(server);
        }
        return this;
    }

    public Paths paths() {
        return paths;
    }

    public Document paths(Paths paths) {
        this.paths = paths;
        return this;
    }

    public List<Tag> tags() {
        return tags;
    }

    public Document addTag(Tag tag) {
        if (tag != null) {
            tags.add(tag);
        }
        return this;
    }

    public ExternalDocumentation externalDocs() {
        return externalDocs;
    }

    public Document externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
