package com.xyzwps.lib.express.core;

import lib.jsdom.mimetype.MimeType;

import java.util.Optional;

/**
 * TODO: 对参数和返回结果作出规定
 */
public interface HttpRequest {

    HttpMethod method();

    String path();

    String protocol();

    Optional<String> header(String name);

    Optional<MimeType> contentType();

    Object body();

    void body(Object body);
}
