package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerTests {

    @Test
    void example1() {
        var s = new Server("https://development.gigantic-server.com/v1")
                .description("Development server");

        var toYaml = new ToYamlVisitor();
        s.accept(toYaml);
        assertEquals("""
                url: https://development.gigantic-server.com/v1
                description: Development server
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        s.accept(toJson);
        assertEquals("""
                {
                  "url": "https://development.gigantic-server.com/v1",
                  "description": "Development server"
                }""", toJson.toPrettyString());
        assertEquals("""
                {"url":"https://development.gigantic-server.com/v1","description":"Development server"}""", toJson.toCompactString());
    }

    @Test
    void example2() {
        var s = new Server("https://{username}.gigantic-server.com:{port}/{basePath}")
                .description("The production API server")
                .addVariable("username", new ServerVariable("demo")
                        .description("this value is assigned by the service provider, in this example `gigantic-server.com`"))
                .addVariable("port", new ServerVariable("8443")
                        .enums(List.of("8443", "443")))
                .addVariable("basePath", new ServerVariable("v2"));

        var toYaml = new ToYamlVisitor();
        s.accept(toYaml);
        assertEquals("""
                url: https://{username}.gigantic-server.com:{port}/{basePath}
                description: The production API server
                variables:
                  basePath:
                    default: v2
                  port:
                    default: '8443'
                    enum:
                      - '8443'
                      - '443'
                  username:
                    default: demo
                    description: this value is assigned by the service provider, in this example `gigantic-server.com`
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        s.accept(toJson);
        assertEquals("""
               {
                 "url": "https://{username}.gigantic-server.com:{port}/{basePath}",
                 "description": "The production API server",
                 "variables": {
                   "basePath": {
                     "default": "v2"
                   },
                   "port": {
                     "default": "8443",
                     "enum": [
                       "8443",
                       "443"
                     ]
                   },
                   "username": {
                     "default": "demo",
                     "description": "this value is assigned by the service provider, in this example `gigantic-server.com`"
                   }
                 }
               }""", toJson.toPrettyString());
    }
}
