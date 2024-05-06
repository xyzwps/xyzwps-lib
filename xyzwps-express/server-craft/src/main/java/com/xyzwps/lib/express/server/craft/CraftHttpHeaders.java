package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.express.HttpException;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.util.SimpleMultiValuesMap;

import java.util.Map;

// TODO: test
// TODO: 不要 implement Map，同时实现 JSONSerializer
final class CraftHttpHeaders extends SimpleMultiValuesMap implements HttpHeaders {

    public CraftHttpHeaders() {
        super(true);
    }

    public int contentLength() {
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

    public String contentType() {
        return get(CONTENT_TYPE);
    }

    private static final int CONTENT_LENGTH_LIMIT = 1024 * 1024 * 50;

    public static final String AUTHORIZATION = "Authorization";

    public static final String CONTENT_LENGTH = "Content-Length";

    public static final String CONTENT_TYPE = "Content-Type";
}