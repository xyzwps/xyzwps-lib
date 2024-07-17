package com.xyzwps.lib.openapi;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class Response implements OASElement {

    private final String description;
    private Map<String, Object> headers = new TreeMap<>();
    private Map<String, MediaType> content = new TreeMap<>();
    private Map<String, Object> links = new TreeMap<>();

    public String description() {
        return description;
    }

    public Response(String description) {
        this.description = Objects.requireNonNull(description);
    }

    public Map<String, Object> headers() {
        return headers;
    }

    public Response addHeader(String name, Header header) {
        if (header != null) {
            headers.put(name, header);
        }
        return this;
    }

    public Response addHeader(String name, Reference reference) {
        if (reference != null) {
            headers.put(name, reference);
        }
        return this;
    }

    public Map<String, MediaType> content() {
        return content;
    }

    public Response addContent(String name, MediaType mediaType) {
        if (mediaType != null) {
            content.put(name, mediaType);
        }
        return this;
    }

    public Map<String, Object> links() {
        return links;
    }

    public Response addLink(String name, Link link) {
        if (link != null) {
            links.put(name, link);
        }
        return this;
    }

    public Response addLink(String name, Reference reference) {
        if (reference != null) {
            links.put(name, reference);
        }
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
