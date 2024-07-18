package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentTests {

    @Test
    void example1() {
        var d = new OpenApiDocument(new Info("title", "0.0.1-snapshot"))
                .addServer(new Server("https://development.gigantic-server.com/v1")
                        .description("Development server"))
                .addServer(new Server("https://staging.gigantic-server.com/v1")
                        .description("Staging server"))
                .addServer(new Server("https://api.gigantic-server.com/v1")
                        .description("Production server"));

        var toYaml = new ToYamlVisitor();
        d.accept(toYaml);
        assertEquals("""
                openapi: 3.1.0
                info:
                  title: title
                  version: 0.0.1-snapshot
                servers:
                - url: https://development.gigantic-server.com/v1
                  description: Development server
                - url: https://staging.gigantic-server.com/v1
                  description: Staging server
                - url: https://api.gigantic-server.com/v1
                  description: Production server
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        d.accept(toJson);
        assertEquals("""
                {
                  "openapi": "3.1.0",
                  "info": {
                    "title": "title",
                    "version": "0.0.1-snapshot"
                  },
                  "servers": [
                    {
                      "url": "https://development.gigantic-server.com/v1",
                      "description": "Development server"
                    },
                    {
                      "url": "https://staging.gigantic-server.com/v1",
                      "description": "Staging server"
                    },
                    {
                      "url": "https://api.gigantic-server.com/v1",
                      "description": "Production server"
                    }
                  ]
                }""", toJson.toPrettyString());

    }
}
