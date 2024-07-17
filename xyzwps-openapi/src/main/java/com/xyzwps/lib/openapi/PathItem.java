package com.xyzwps.lib.openapi;

import java.util.ArrayList;
import java.util.List;

public class PathItem implements OASElement {

    private String $ref;
    private String summary;
    private String description;
    private Operation get;
    private Operation post;
    private Operation put;
    private Operation delete;
    private Operation head;
    private Operation patch;
    private Operation trace;
    private List<Server> servers = new ArrayList<>();
    private List<Object> parameters = new ArrayList<>();

    public String $ref() {
        return $ref;
    }

    public PathItem $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    public List<Object> parameters() {
        return parameters;
    }

    public PathItem addParameter(Parameter parameter) {
        if (parameter != null) {
            this.parameters.add(parameter);
        }
        return this;
    }

    public PathItem addParameter(Reference reference) {
        if (reference != null) {
            this.parameters.add(reference);
        }
        return this;
    }

    public String summary() {
        return summary;
    }

    public PathItem summary(String summary) {
        this.summary = summary;
        return this;
    }

    public String description() {
        return this.description;
    }

    public PathItem description(String description) {
        this.description = description;
        return this;
    }

    public List<Server> servers() {
        return servers;
    }

    public PathItem add(Server server) {
        if (server != null) {
            servers.add(server);
        }
        return this;
    }

    public Operation get() {
        return get;
    }

    public PathItem get(Operation get) {
        this.get = get;
        return this;
    }

    public Operation post() {
        return post;
    }

    public PathItem post(Operation post) {
        this.post = post;
        return this;
    }

    public Operation put() {
        return put;
    }

    public PathItem put(Operation put) {
        this.put = put;
        return this;
    }

    public Operation delete() {
        return delete;
    }

    public PathItem delete(Operation delete) {
        this.delete = delete;
        return this;
    }

    public Operation head() {
        return head;
    }

    public PathItem head(Operation head) {
        this.head = head;
        return this;
    }

    public Operation patch() {
        return patch;
    }

    public PathItem patch(Operation patch) {
        this.patch = patch;
        return this;
    }

    public Operation trace() {
        return trace;
    }

    public PathItem trace(Operation trace) {
        this.trace = trace;
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
