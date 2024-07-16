package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagTests {

    @Test
    void example1() {
        var t = new Tag("pet")
                .description("Pets operations");

        var toYaml = new ToYamlVisitor();
        t.accept(toYaml);
        assertEquals("""
                name: pet
                description: Pets operations
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        t.accept(toJson);
        assertEquals("""
                {
                  "name": "pet",
                  "description": "Pets operations"
                }""", toJson.toPrettyString());
    }

    @Test
    void example2() {
        var t = new Tag("pet")
                .externalDocs(new ExternalDocumentation("https://example.com/petstore-spec.html"));

        var toYaml = new ToYamlVisitor();
        t.accept(toYaml);
        assertEquals("""
                name: pet
                externalDocs:
                  url: https://example.com/petstore-spec.html
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        t.accept(toJson);
        assertEquals("""
                {
                  "name": "pet",
                  "externalDocs": {
                    "url": "https://example.com/petstore-spec.html"
                  }
                }""", toJson.toPrettyString());
    }
}
