package com.xyzwps.lib.openapi;

import com.xyzwps.lib.bedrock.Args;
import lombok.Getter;

import java.util.List;

@Getter
public class ServerVariable implements OASElement {
    private final List<String> enums;
    private final String defaultValue;
    private String description;

    public ServerVariable(List<String> enums, String defaultValue) {
        Args.notEmpty(enums, "enums must not be empty");
        this.enums = enums;
        this.defaultValue = Args.notEmpty(defaultValue, "defaultValue must not be empty");
    }

    public ServerVariable setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
