package com.xyzwps.lib.express;

/**
 * TODO: 做规定
 */
public interface HttpResponse {

    /**
     * return this
     */
    HttpResponse status(HttpStatus status);

    default HttpResponse ok() {
        return this.status(HttpStatus.OK);
    }

    /**
     * Set value for specified header. TODO: 多个值怎么搞
     *
     * @return this
     */
    HttpResponse set(String name, String value);

    void send(byte[] bytes);
}
