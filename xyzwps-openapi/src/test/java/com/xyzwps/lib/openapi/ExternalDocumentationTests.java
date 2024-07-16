package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExternalDocumentationTests {

    @Test
    void example1() {
        var d = new ExternalDocumentation("https://example.com")
                .description("Find more info here");

        var toYaml = new ToYamlVisitor();
        d.accept(toYaml);
        assertEquals("""
                description: Find more info here
                url: https://example.com
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        d.accept(toJson);
        assertEquals("""
                {
                  "description": "Find more info here",
                  "url": "https://example.com"
                }""", toJson.toPrettyString());
        assertEquals("""
                {"description":"Find more info here","url":"https://example.com"}""", toJson.toCompactString());
    }
}
