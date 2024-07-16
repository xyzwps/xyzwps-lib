package com.xyzwps.lib.openapi;

public class Contact implements OASElement {
    private final String name;
    private String url;
    private String email;

    public Contact(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    public String url() {
        return url;
    }

    public Contact url(String url) {
        this.url = url;
        return this;
    }

    public String email() {
        return email;
    }

    public Contact email(String email) {
        this.email = email;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
