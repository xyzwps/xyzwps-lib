package com.xyzwps.lib.openapi;

import lombok.Getter;

@Getter
public class Contact implements OASElement {
    private final String name;
    private String url;
    private String email;

    public Contact(String name) {
        this.name = name;
    }

    public Contact setUrl(String url) {
        this.url = url;
        return this;
    }

    public Contact setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
