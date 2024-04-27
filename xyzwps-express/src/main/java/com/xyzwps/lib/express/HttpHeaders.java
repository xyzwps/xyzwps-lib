package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

import java.util.*;

public final class HttpHeaders {

    private final Map<String, List<String>> headers = new HashMap<>();

    // TODO: test
    public void set(String name, String value) {
        Args.notEmpty(name, "Header name cannot be empty");
        Args.notEmpty(value, "Header value cannot be empty");

        name = name.toLowerCase();

        if (!headers.containsKey(name)) {
            headers.put(name, new LinkedList<>());
        }
        headers.get(name).add(value);
    }

    /**
     * Get all header values by name.
     * <p>
     * An immutable list should be returned.
     *
     * @param name cannot be null
     * @return empty list if header does not exist
     */
    public List<String> getAll(String name) {
        Args.notNull(name, "Header name cannot be null");

        var values = headers.get(name.toLowerCase());
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return List.copyOf(values);
    }

    /**
     * Get the first header value by name.
     *
     * @param name cannot be null
     * @return null if header does not exist
     */
    public String getFirst(String name) {
        Args.notNull(name, "Header name cannot be empty");

        var values = headers.get(name.toLowerCase());
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.getFirst();
    }


    public int contentLength() {
        var lengthStr = getFirst(CONTENT_LENGTH);
        if (lengthStr == null || lengthStr.isEmpty()) {
            return 0;
        }


        try {
            var length = Long.parseLong(lengthStr);
            if (length > CONTENT_LENGTH_LIMIT) {
                throw HttpException.payloadTooLarge("Payload too large",
                        Map.of("Content-Length", length, "contentLengthLimit", CONTENT_LENGTH_LIMIT));
            }
            return (int) length;
        } catch (NumberFormatException e) {
            throw HttpException.badRequest("Invalid content length of %s", lengthStr);
        }


    }

    public String contentType() {
        return getFirst(CONTENT_TYPE);
    }

    private static final int CONTENT_LENGTH_LIMIT = 1024 * 1024 * 50;

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_TYPE = "Content-Type";
}
