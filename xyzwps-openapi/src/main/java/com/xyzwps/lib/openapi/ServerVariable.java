package com.xyzwps.lib.openapi;

import com.xyzwps.lib.bedrock.Args;

import java.util.List;

public class ServerVariable implements OASElement {
    private List<String> enums;
    private final String defaultValue;
    private String description;

    public ServerVariable(String defaultValue) {
        this.defaultValue = Args.notEmpty(defaultValue, "defaultValue must not be empty");
    }

    public String defaultValue() {
        return defaultValue;
    }

    public List<String> enums() {
        return enums;
    }

    public ServerVariable enums(List<String> enums) {
        if (enums != null) {
            Args.notEmpty(enums, "enums must not be empty");
        }
        this.enums = enums;
        return this;
    }

    public String description() {
        return description;
    }

    public ServerVariable description(String description) {
        this.description = description;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
