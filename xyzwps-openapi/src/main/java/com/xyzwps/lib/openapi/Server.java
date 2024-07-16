package com.xyzwps.lib.openapi;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
public class Server implements OASElement {
    private final String url;
    private String description;
    private final Map<String, ServerVariable> variables = new HashMap<>();

    public Server(String url) {
        this.url = Objects.requireNonNull(url);
    }

    public Server setDescription(String description) {
        this.description = description;
        return this;
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
