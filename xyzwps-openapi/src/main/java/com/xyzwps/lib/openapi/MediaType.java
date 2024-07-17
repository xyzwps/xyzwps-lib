package com.xyzwps.lib.openapi;

import java.util.Map;
import java.util.TreeMap;

public class MediaType implements OASElement {

    private Schema schema;
    private String example;
    private Map<String, Object> examples = new TreeMap<>();
    private Map<String, Encoding> encoding = new TreeMap<>();

    public Schema schema() {
        return schema;
    }

    public MediaType schema(Schema schema) {
        this.schema = schema;
        return this;
    }

    public String example() {
        return example;
    }

    public MediaType example(String example) {
        this.example = example;
        return this;
    }

    public Map<String, Object> examples() {
        return examples;
    }

    public MediaType addToExamples(String name, Example example) {
        if (example != null) {
            examples.put(name, example);
        }
        return this;
    }

    public MediaType addToExamples(String name, Reference reference) {
        if (reference != null) {
            examples.put(name, reference);
        }
        return this;
    }

    public Map<String, Encoding> encoding() {
        return encoding;
    }

    public MediaType addEncoding(String name, Encoding encoding) {
        if (encoding != null) {
            this.encoding.put(name, encoding);
        }
        return this;
    }

    @Override
    public void accept(OAEVisitor visitor) {
        visitor.visit(this);
    }
}
