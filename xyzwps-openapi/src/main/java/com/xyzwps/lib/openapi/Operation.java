package com.xyzwps.lib.openapi;

import java.util.ArrayList;
import java.util.List;

public class Operation implements OASElement {

    private List<String> tags = new ArrayList<>();
    private String summary;
    private String description;
    private ExternalDocumentation externalDocs;
    private String operationId;
    private List<Object> parameters = new ArrayList<>();
    private Object requestBody;
    private Responses responses;
    // TODO: callbacks
    private boolean deprecated = false;
    // TODO: security
    private List<Server> servers;

    public List<Server> servers() {
        return servers;
    }

    public Operation addServer(Server server) {
        if (server != null) {
            servers.add(server);
        }
        return this;
    }

    public boolean deprecated() {
        return deprecated;
    }

    public Operation deprecated(boolean deprecated) {
        this.deprecated = deprecated;
        return this;
    }

    public Responses responses() {
        return responses;
    }

    public Operation responses(Responses responses) {
        this.responses = responses;
        return this;
    }

    public String operationId() {
        return operationId;
    }

    public Operation operationId(String operationId) {
        this.operationId = operationId;
        return this;
    }

    public ExternalDocumentation externalDocs() {
        return externalDocs;
    }

    public Operation externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
    }

    public String description() {
        return description;
    }

    public Operation description(String description) {
        this.description = description;
        return this;
    }

    public String summary() {
        return summary;
    }

    public Operation summary(String summary) {
        this.summary = summary;
        return this;
    }

    public List<String> tags() {
        return tags;
    }

    public Operation addTag(String tag) {
        if (tag != null) {
            tags.add(tag);
        }
        return this;
    }

    public Object requestBody() {
        return requestBody;
    }

    public Operation requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public Operation requestBody(Reference ref) {
        this.requestBody = ref;
        return this;
    }


    public List<Object> parameters() {
        return parameters;
    }

    public Operation addParameter(Parameter parameter) {
        if (parameter != null) {
            this.parameters.add(parameter);
        }
        return this;
    }

    public Operation addParameter(Reference reference) {
        if (reference != null) {
            this.parameters.add(reference);
        }
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
