package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.lang.DefaultValues;
import com.xyzwps.lib.http.HttpMethod;
import com.xyzwps.lib.http.MediaType;
import com.xyzwps.lib.json.JsonException;
import com.xyzwps.lib.json.JsonMapper;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * An http request.
 */
public interface HttpRequest {

    /**
     * Get request method.
     *
     * @return request method. Never be null.
     */
    HttpMethod method();

    /**
     * Get request path.
     *
     * @return request path. Never be null.
     */
    String path();

    /**
     * Get http protocol.
     *
     * @return http protocol. Never be null.
     */
    HttpProtocol protocol();

    /**
     * Get the first header value by name.
     *
     * @param name cannot be null
     * @return null if header does not exist
     */
    String header(String name);

    @SuppressWarnings("unchecked")
    default <T> T header(String name, Class<T> type) {
        var value = header(name);
        if (value == null) {
            return (T) DefaultValues.get(type);
        }

        if (type == String.class) {
            return (T) value;
        }
        throw new UnsupportedOperationException("Unsupported header type: " + type.getCanonicalName());
    }

    /**
     * Get all header values by name.
     *
     * @param name cannot be null
     * @return empty list if header does not exist
     */
    List<String> headers(String name);

    /**
     * Get all headers.
     *
     * @return all headers
     */
    HttpHeaders headers();

    /**
     * Get request content type.
     *
     * @return null if <code>Content-Type</code> header does not exist
     */
    MediaType contentType();

    /**
     * Get the current request body representation.
     *
     * @return request body representation in current stage. Null maybe returned.
     */
    Object body();

    default <T> T json(Class<T> clazz, JsonMapper jm) {
        var contentType = this.contentType();
        if (contentType == null) {
            throw HttpException.badRequest("Content-Type is not json");
        }
        if (!contentType.isApplicationJson()) {
            throw HttpException.badRequest("Content-Type is not json");
        }
        if (!(this.body() instanceof InputStream is)) {
            throw HttpException.internalServerError("Request body has been parsed");
        }

        try {
            var charset = contentType.parameters.get("charset").map(Charset::forName).orElse(StandardCharsets.UTF_8);
            var reader = new InputStreamReader(is, charset);
            return jm.parse(reader, clazz);
        } catch (JsonException e) {
            throw HttpException.badRequest("Invalid json format");
        }
    }

    /**
     * Replace body by specified object, for example, parsed body.
     *
     * @param body new or parsed body. Null value is allowed.
     */
    void body(Object body);

    /**
     * Get all search params.
     *
     * @return never be null
     */
    HttpSearchParams searchParams();

    Map<String, Object> attributes();

    Object attribute(String name);

    void attribute(String name, Object value);

    /**
     * Get all path variables.
     *
     * @return all path variables
     */
    HttpPathVariables pathVariables();

    Cookies cookies();
}
