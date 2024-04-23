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

    public List<String> getAll(String name) {
        Args.notEmpty(name, "Header name cannot be empty");

        var values = headers.get(name.toLowerCase());
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return List.copyOf(values);
    }

    public Optional<String> getFirst(String name) {
        Args.notEmpty(name, "Header name cannot be empty");

        var values = headers.get(name.toLowerCase());
        if (values == null || values.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(values.getFirst());
    }


    public int contentLength() {
        long length = getFirst(CONTENT_LENGTH)
                .map(value -> {
                    try {
                        return Long.parseLong(value);
                    } catch (NumberFormatException e) {
                        throw HttpException.badRequest("Invalid content length of %s", value);
                    }
                })
                .orElse(0L);
        if (length > CONTENT_LENGTH_LIMIT) {
            throw HttpException.payloadTooLarge("Payload too large",
                    Map.of("Content-Length", length, "contentLengthLimit", CONTENT_LENGTH_LIMIT));
        }
        return (int) length;
    }

    public Optional<String> contentType() {
        return getFirst(CONTENT_TYPE);
    }

    private static final int CONTENT_LENGTH_LIMIT = 1024 * 1024 * 50;

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_TYPE = "Content-Type";
}
