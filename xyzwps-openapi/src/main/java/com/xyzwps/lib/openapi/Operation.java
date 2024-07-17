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
