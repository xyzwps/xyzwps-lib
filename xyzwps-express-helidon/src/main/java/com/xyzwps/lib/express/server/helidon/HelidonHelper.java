package com.xyzwps.lib.express.server.helidon;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.commons.SimpleHttpHeaders;
import io.helidon.http.Headers;

class HelidonHelper {

    static HttpHeaders createHttpHeader(Headers headers) {
        // TODO: 可以看到，把 helidon 接过来的过程中，做了很多转换，这里可以考虑优化，希望能够把这些需要转换的东西做一些抽象
        var httpHeaders = new SimpleHttpHeaders();
        for (var header : headers) {
            var name = header.headerName().defaultCase();
            header.allValues().forEach(value -> httpHeaders.append(name, value));
        }
        return httpHeaders;
    }

}
