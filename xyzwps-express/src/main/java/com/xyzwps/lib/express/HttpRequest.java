package com.xyzwps.lib.express;

import com.xyzwps.lib.http.MediaType;

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
