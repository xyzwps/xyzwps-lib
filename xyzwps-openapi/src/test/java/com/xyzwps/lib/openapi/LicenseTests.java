package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LicenseTests {

    @Test
    void example1() {
        var it = new License.IdLicense("Apache 2.0", "Apache-2.0");

        var toYaml = new ToYamlVisitor();
        it.accept(toYaml);
        assertEquals("""
                name: Apache 2.0
                identifier: Apache-2.0
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        it.accept(toJson);
        assertEquals("""
                {
                  "name": "Apache 2.0",
                  "identifier": "Apache-2.0"
                }""", toJson.toPrettyString());
        assertEquals("""
                {"name":"Apache 2.0","identifier":"Apache-2.0"}""", toJson.toCompactString());
    }

    @Test
    void example2() {
        var it = new License.UrlLicense("Apache 2.0", "https://www.apache.org/licenses/LICENSE-2.0.html");

        var toYaml = new ToYamlVisitor();
        it.accept(toYaml);
        assertEquals("""
                name: Apache 2.0
                url: https://www.apache.org/licenses/LICENSE-2.0.html
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        it.accept(toJson);
        assertEquals("""
                {
                  "name": "Apache 2.0",
                  "url": "https://www.apache.org/licenses/LICENSE-2.0.html"
                }""", toJson.toPrettyString());
        assertEquals("""
                {"name":"Apache 2.0","url":"https://www.apache.org/licenses/LICENSE-2.0.html"}""", toJson.toCompactString());
    }
}
