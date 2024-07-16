package com.xyzwps.lib.openapi;


import java.util.Objects;

public class Info implements OASElement {
    private final String title;
    private String summary;
    private String description;
    private Contact contact;
    private License license;
    private final String version;
    private String termsOfService;

    public Info(String title, String version) {
        this.title = Objects.requireNonNull(title);
        this.version = Objects.requireNonNull(version);
    }

    public String title() {
        return title;
    }

    public String version() {
        return version;
    }

    public String summary() {
        return summary;
    }

    public Info summary(String summary) {
        this.summary = summary;
        return this;
    }

    public String description() {
        return description;
    }

    public Info description(String description) {
        this.description = description;
        return this;
    }

    public Contact contact() {
        return contact;
    }

    public Info contact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public License license() {
        return license;
    }

    public Info license(License license) {
        this.license = license;
        return this;
    }

    public String termsOfService() {
        return termsOfService;
    }

    public Info termsOfService(String termsOfService) {
        // TODO: 必须是 url 的形式
        this.termsOfService = termsOfService;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
