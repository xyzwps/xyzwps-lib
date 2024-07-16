package com.xyzwps.lib.openapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server implements OASElement {
    private final String url;
    private String description;
    private final Map<String, ServerVariable> variables = new HashMap<>();

    public Server(String url) {
        this.url = Objects.requireNonNull(url);
    }

    public String url() {
        return url;
    }

    public String description() {
        return description;
    }

    public Server description(String description) {
        this.description = description;
        return this;
    }

    public Map<String, ServerVariable> variables() {
        return variables;
    }

    public Server addVariable(String name, ServerVariable variable) {
        if (name != null && variable != null) {
            variables.put(name, variable);
        }
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
