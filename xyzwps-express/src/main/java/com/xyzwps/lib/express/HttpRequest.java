package com.xyzwps.lib.express;

import lib.jsdom.mimetype.MimeType;

import java.util.List;

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

    // TODO: path variable

//    /**
//     * Get path variable value by specified name.
//     *
//     * @param name cannot be null
//     * @return null if not matched
//     */
//    String pathVariable(String name);
//
//    /**
//     * Get all path variables.
//     *
//     * @return all path variables
//     */
//    Map<String, String> pathVariables();
}
