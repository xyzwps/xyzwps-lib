package com.xyzwps.lib.openapi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResponsesTests {

    @Test
    void example1() {
        var r = new Responses()
                .responseDefault(new Response("Unexpected error")
                        .addContent("application/json", new MediaType()
                                .schema(new Schema.RefSchema("#/components/schemas/ErrorModel"))))
                .addStatusResponse(200, new Response("a pet to be returned")
                        .addContent("application/json", new MediaType()
                                .schema(new Schema.RefSchema("#/components/schemas/Pet"))));

        var toYaml = new ToYamlVisitor();
        r.accept(toYaml);
        assertEquals("""
                200:
                  description: a pet to be returned
                  content:
                    application/json:
                      schema:
                        $ref: '#/components/schemas/Pet'
                default:
                  description: Unexpected error
                  content:
                    application/json:
                      schema:
                        $ref: '#/components/schemas/ErrorModel'
                """, toYaml.toYaml());


        var toJson = new ToJsonVisitor();
        r.accept(toJson);
        assertEquals("""
                {
                  "200": {
                    "description": "a pet to be returned",
                    "content": {
                      "application/json": {
                        "schema": {
                          "$ref": "#/components/schemas/Pet"
                        }
                      }
                    }
                  },
                  "default": {
                    "description": "Unexpected error",
                    "content": {
                      "application/json": {
                        "schema": {
                          "$ref": "#/components/schemas/ErrorModel"
                        }
                      }
                    }
                  }
                }""", toJson.toPrettyString());
    }

     /*
         {
      "200": {
        "description": "a pet to be returned",
        "content": {
          "application/json": {
            "schema": {
              "$ref": "#/components/schemas/Pet"
            }
          }
        }
      },
      "default": {
        "description": "Unexpected error",
        "content": {
          "application/json": {
            "schema": {
              "$ref": "#/components/schemas/ErrorModel"
            }
          }
        }
      }
    }

    '200':
      description: a pet to be returned
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/Pet'
    default:
      description: Unexpected error
      content:
        application/json:
          schema:
            $ref: '#/components/schemas/ErrorModel'
      */
}
