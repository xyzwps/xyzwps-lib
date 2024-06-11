package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.ElementParser;
import com.xyzwps.lib.json.element.StackParser;
import com.xyzwps.lib.json.util.CharGenerator;

/**
 * Utility class for JSON.
 */
public final class JsonUtils {

    private static final ElementParser PARSER = new StackParser();

    /**
     * Compares two JSON strings.
     *
     * @param json1 the first JSON string
     * @param json2 the second JSON string
     * @return {@code true} if the two JSON strings are equal, {@code false} otherwise
     */
    public static boolean jsonEquals(String json1, String json2) {
        if (json1 == null) {
            return json2 == null;
        }
        if (json2 == null) {
            return false;
        }
        var j1 = PARSER.parse(CharGenerator.from(json1));
        var j2 = PARSER.parse(CharGenerator.from(json2));
        return j1.equals(j2);
    }

    private JsonUtils() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
