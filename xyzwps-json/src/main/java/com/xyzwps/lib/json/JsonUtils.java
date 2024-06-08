package com.xyzwps.lib.json;

import com.xyzwps.lib.json.element.ElementParser;
import com.xyzwps.lib.json.element.StackParser;
import com.xyzwps.lib.json.util.StringCharGenerator;

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
        return PARSER.parse(new StringCharGenerator(json1)).equals(PARSER.parse(new StringCharGenerator(json2)));
    }

    private JsonUtils() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
