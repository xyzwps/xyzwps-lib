package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MediaTypeTests {

    @Test
    void example1() {
        var resp = new Response("test")
                .addContent("application/json", new MediaType()
                        .schema(new Schema.RefSchema("#/components/schemas/Pet"))
                        .addToExamples("cat", new Example()
                                .summary("An example of a cat")
                                .value(Map.of(
                                        "name", "Fluffy",
                                        "petType", "Cat",
                                        "color", "White",
                                        "gender", "male",
                                        "breed", "Persian"
                                )))
                        .addToExamples("dog", new Example()
                                .summary("An example of a dog with a cat's name")
                                .value(Map.of(
                                        "name", "Puma",
                                        "petType", "Dog",
                                        "color", "Black",
                                        "gender", "Female",
                                        "breed", "Mixed"
                                )))
                        .addToExamples("frog", new Reference("#/components/examples/frog-example")));

        var toYaml = new ToYamlVisitor();
        resp.accept(toYaml);
        assertEquals("""
                description: test
                content:
                  application/json:
                    schema:
                      $ref: '#/components/schemas/Pet'
                    examples:
                      cat:
                        summary: An example of a cat
                        value:
                          breed: Persian
                          color: White
                          gender: male
                          name: Fluffy
                          petType: Cat
                      dog:
                        summary: An example of a dog with a cat's name
                        value:
                          breed: Mixed
                          color: Black
                          gender: Female
                          name: Puma
                          petType: Dog
                      frog:
                        $ref: '#/components/examples/frog-example'
                """, toYaml.toYaml());

        var toJson = new ToJsonVisitor();
        resp.accept(toJson);
        assertEquals("""
                {
                  "description": "test",
                  "content": {
                    "application/json": {
                      "schema": {
                        "$ref": "#/components/schemas/Pet"
                      },
                      "examples": {
                        "cat": {
                          "summary": "An example of a cat",
                          "value": {
                            "breed": "Persian",
                            "color": "White",
                            "gender": "male",
                            "name": "Fluffy",
                            "petType": "Cat"
                          }
                        },
                        "dog": {
                          "summary": "An example of a dog with a cat's name",
                          "value": {
                            "breed": "Mixed",
                            "color": "Black",
                            "gender": "Female",
                            "name": "Puma",
                            "petType": "Dog"
                          }
                        },
                        "frog": {
                          "$ref": "#/components/examples/frog-example"
                        }
                      }
                    }
                  }
                }""", toJson.toPrettyString());
    }
}
