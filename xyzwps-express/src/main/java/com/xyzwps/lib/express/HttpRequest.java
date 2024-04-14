package com.xyzwps.lib.express;

import java.util.Optional;

public interface HttpRequest<BODY> {

    /**
     * Never return null.
     */
    HttpMethod method();

    /**
     * Never return null.
     */
    String url();

    /**
     * Never return null.
     */
    String protocol();

    Optional<HttpHeader> header(String name);

    /**
     * May return null.
     */
    BODY body();
}
