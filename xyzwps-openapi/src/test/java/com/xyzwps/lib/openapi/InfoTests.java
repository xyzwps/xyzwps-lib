package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InfoTests {

    @Test
    void example1() {
        var info = new Info("Sample Pet Store App", "1.0.1")
                .summary("A pet store manager.")
                .description("This is a sample server for a pet store.")
                .termsOfService("https://example.com/terms/")
                .contact(new Contact("API Support")
                        .url("https://www.example.com/support")
                        .email("support@example.com"))
                .license(new License.UrlLicense("Apache 2.0", "https://www.apache.org/licenses/LICENSE-2.0.html"));

        var toYamlVisitor = new ToYamlVisitor();
        info.accept(toYamlVisitor);

        assertEquals("""
                title: Sample Pet Store App
                summary: A pet store manager.
                description: This is a sample server for a pet store.
                termsOfService: https://example.com/terms/
                contact:
                  name: API Support
                  url: https://www.example.com/support
                  email: support@example.com
                license:
                  name: Apache 2.0
                  url: https://www.apache.org/licenses/LICENSE-2.0.html
                version: 1.0.1
                """, toYamlVisitor.toYaml());

        var toJsonVisitor = new ToJsonVisitor();
        info.accept(toJsonVisitor);
        assertEquals("""
                {
                  "title": "Sample Pet Store App",
                  "summary": "A pet store manager.",
                  "description": "This is a sample server for a pet store.",
                  "termsOfService": "https://example.com/terms/",
                  "contact": {
                    "name": "API Support",
                    "url": "https://www.example.com/support",
                    "email": "support@example.com"
                  },
                  "license": {
                    "name": "Apache 2.0",
                    "url": "https://www.apache.org/licenses/LICENSE-2.0.html"
                  },
                  "version": "1.0.1"
                }""", toJsonVisitor.toPrettyString());
        assertEquals("{" +
                "\"title\":\"Sample Pet Store App\"," +
                "\"summary\":\"A pet store manager.\"," +
                "\"description\":\"This is a sample server for a pet store.\"," +
                "\"termsOfService\":\"https://example.com/terms/\"," +
                "\"contact\":{" +
                "\"name\":\"API Support\"," +
                "\"url\":\"https://www.example.com/support\"," +
                "\"email\":\"support@example.com\"" +
                "}," +
                "\"license\":{" +
                "\"name\":\"Apache 2.0\"," +
                "\"url\":\"https://www.apache.org/licenses/LICENSE-2.0.html\"" +
                "}," +
                "\"version\":\"1.0.1\"" +
                "}", toJsonVisitor.toCompactString());


    }
}
