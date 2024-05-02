package com.xyzwps.lib.express;

public interface HttpResponse {

    /**
     * Set response status.
     *
     * @param status cannot be null
     */
    void status(HttpStatus status);

    default void ok() {
        this.status(HttpStatus.OK);
    }

    /**
     * Get all headers to response.
     *
     * @return all response headers
     */
    HttpHeaders headers();

    /**
     * Send bytes and headers to client and set header <code>Content-Type</code> value with <code>bytes.length.</code>
     *
     * @param bytes cannot be null
     */
    void send(byte[] bytes);
}
