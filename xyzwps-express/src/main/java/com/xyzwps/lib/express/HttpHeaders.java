package com.xyzwps.lib.express;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

public interface HttpHeaders {

    /**
     * Append a value to a specified header name.
     *
     * @param name  cannot be null
     * @param value cannot be null
     */
    void append(String name, String value);

    /**
     * Delete header values associated with a specific name.
     *
     * @param name cannot be null
     */
    void delete(String name);

    /**
     * Iterator over all headers.
     *
     * @param callback cannot be null
     */
    void forEach(BiConsumer<String, List<String>> callback);

    /**
     * Get the first value associated with a specific name.
     *
     * @param name cannot be null
     * @return null if header name does not exist
     */
    String get(String name);

    /**
     * Get all values  associated with a specific name.
     *
     * @param name cannot be null
     * @return null or an empty list if header name does not exist
     */
    List<String> getAll(String name);

    /**
     * Check if the header name would exist or not
     *
     * @param name cannot be null
     * @return false if header name does not exist; or else true
     */
    boolean has(String name);

    /**
     * Get all header names.
     *
     * @return never returns a null
     */
    Set<String> names();

    void set(String name, String value);

    default int contentLength() {
        var lengthStr = get(CONTENT_LENGTH);
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

    default String contentType() {
        return get(CONTENT_TYPE);
    }


    int CONTENT_LENGTH_LIMIT = 1024 * 1024 * 50;

    String AUTHORIZATION = "Authorization";

    String CONTENT_LENGTH = "Content-Length";

    String CONTENT_TYPE = "Content-Type";

    String CONNECTION = "Connection";

    String KEEP_ALIVE = "Keep-Alive";

    String DATE = "Date";
}
