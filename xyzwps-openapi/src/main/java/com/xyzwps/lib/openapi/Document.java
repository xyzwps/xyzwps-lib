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

    // TODO: webhooks
    // TODO: components
    // TODO: security
    // TODO: externalDocs

    public Document(Info info) {
        this.info = Objects.requireNonNull(info);
    }

    public Document addServer(Server server) {
        if (server != null) {
            servers.add(server);
        }
        return this;
    }

    public Document setPaths(Paths paths) {
        this.paths = paths;
        return this;
    }

    public Document addTag(Tag tag) {
        if (tag != null) {
            tags.add(tag);
        }
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
