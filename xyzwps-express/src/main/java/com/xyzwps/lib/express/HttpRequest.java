package com.xyzwps.lib.express;

import lib.jsdom.mimetype.MimeType;

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
    String protocol();

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
    MimeType contentType();

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

    /**
     * Get all attributes.
     *
     * @return never be null
     */
    Map<String, Object> attributes();

    /**
     * Get an attribute value associated with a specified name.
     *
     * @param name cannot be null
     * @return attribute value
     */
    Object attribute(String name);

    /**
     * Add or change the attribute value associated with a specified name.
     *
     * @param name  cannot be null
     * @param value could bu null
     */
    void attribute(String name, Object value);

    // TODO: path variables
}
