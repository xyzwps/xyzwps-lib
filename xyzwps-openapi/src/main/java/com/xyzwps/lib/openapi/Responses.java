package com.xyzwps.lib.openapi;

import java.util.Map;
import java.util.TreeMap;

public class Responses implements OASElement {

    private OASElement responseDefault;

    private Map<Integer, OASElement> statusResponses = new TreeMap<>();

    public OASElement responseDefault() {
        return responseDefault;
    }

    public Responses responseDefault(Response responseDefault) {
        this.responseDefault = responseDefault;
        return this;
    }

    public Responses responseDefault(Reference reference) {
        this.responseDefault = reference;
        return this;
    }

    public Map<Integer, OASElement> statusResponses() {
        return statusResponses;
    }

    public Responses addStatusResponse(int status, Response response) {
        if (response != null) {
            statusResponses.put(status, response);
        }
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
