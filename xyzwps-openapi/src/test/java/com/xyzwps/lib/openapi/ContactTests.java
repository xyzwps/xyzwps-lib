package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ContactTests {

    @Test
    void example1() {
         var contact = new Contact("API Support")
                .url("https://www.example.com/support")
                .email("support@example.com");

         var toYaml = new ToYamlVisitor();
         contact.accept(toYaml);
         assertEquals("""
                name: API Support
                url: https://www.example.com/support
                email: support@example.com
                """, toYaml.toYaml());

         var toJson = new ToJsonVisitor();
         contact.accept(toJson);
         assertEquals("""
                {
                  "name": "API Support",
                  "url": "https://www.example.com/support",
                  "email": "support@example.com"
                }""", toJson.toPrettyString());
        assertEquals("""
                {"name":"API Support","url":"https://www.example.com/support","email":"support@example.com"}""", toJson.toCompactString());
    }
}
