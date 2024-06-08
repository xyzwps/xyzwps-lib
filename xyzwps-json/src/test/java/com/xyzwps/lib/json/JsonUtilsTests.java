package com.xyzwps.lib.json;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static com.xyzwps.lib.json.JsonUtils.*;

class JsonUtilsTests {

    @Test
    void testJsonEquals() {
        assertTrue(jsonEquals("null", "null"));
        assertTrue(jsonEquals("true", "true"));
        assertTrue(jsonEquals("false", "false"));
        assertTrue(jsonEquals("1", "1"));
        assertTrue(jsonEquals("\"a\"", "\"a\""));
        assertTrue(jsonEquals("[]", "[ ]"));
        assertTrue(jsonEquals("{}", "{ }"));
        assertTrue(jsonEquals("[1,2,3]", "[ 1, 2, 3 ]"));
        assertTrue(jsonEquals("{\"a\":1,\"b\":2}", """
        {
          "a": 1,
          "b": 2
        }"""));
        assertTrue(jsonEquals("[{\"a\":1,\"b\":2},1,true]", """
        [
            { "a": 1, "b": 2 },
            1,
            true
        ]
        """));

        assertFalse(jsonEquals("null", null));
        assertFalse(jsonEquals(null, "null"));
        assertFalse(jsonEquals("true", "1"));
        assertFalse(jsonEquals("true", "false"));
        assertFalse(jsonEquals("1", "\"1\""));
        assertFalse(jsonEquals("\"a\"", "\"A\""));
        assertFalse(jsonEquals("[]", "[ [] ]"));
        assertFalse(jsonEquals("{}", "[{ }]"));
        assertFalse(jsonEquals("[1,2,3]", "[ 1, 2, \"3\", 4 ]"));
        assertFalse(jsonEquals("{\"a\":1,\"b\":2}", """
        {
          "a": 1,
          "b": 2,
          "c": 3
        }"""));
        assertFalse(jsonEquals("[{\"a\":1,\"b\":2},1,true]", """
        [
            { "a": 1, "b": 2 },
            true
        ]
        """));
    }
}
