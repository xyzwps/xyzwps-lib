package com.xyzwps.lib.openapi;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Info implements OASElement {
    private final String title;
    private String summary;
    private String description;
    private Contact contact;
    private License license;
    private final String version;

    public Info(String title, String version) {
        this.title = Objects.requireNonNull(title);
        this.version = Objects.requireNonNull(version);
    }

    public Info setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    public Info setDescription(String description) {
        this.description = description;
        return this;
    }

    public Info setContact(Contact contact) {
        this.contact = contact;
        return this;
    }

    public Info setLicense(License license) {
        this.license = license;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
