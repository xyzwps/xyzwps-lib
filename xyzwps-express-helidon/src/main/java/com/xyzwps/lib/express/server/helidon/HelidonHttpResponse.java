package com.xyzwps.lib.express.server.helidon;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;
import com.xyzwps.lib.express.SetCookies;
import com.xyzwps.lib.express.commons.SimpleHttpHeaders;
import com.xyzwps.lib.express.commons.HeaderDateValue;
import io.helidon.http.HeaderNames;
import io.helidon.webserver.http.ServerResponse;

class HelidonHttpResponse implements HttpResponse {

    private final ServerResponse response;
    private final HttpHeaders headers;
    private final SetCookies cookies;

    private HttpStatus status = HttpStatus.OK;

    HelidonHttpResponse(ServerResponse response) {
        this.response = response;
        this.headers = new SimpleHttpHeaders();
        this.cookies = new SetCookies();
    }

    @Override
    public void status(HttpStatus status) {
        this.status = Args.notNull(status, "HttpStatus cannot be null");
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    @Override
    public SetCookies cookies() {
        return cookies;
    }

    @Override
    public void send(byte[] bytes) {
        response.status(status.code);
        this.headers.set(HttpHeaders.DATE, HeaderDateValue.get());
        var helidonHeaders = response.headers();
        this.headers.forEach((name, values) -> {
            for (var value : values) {
                helidonHeaders.add(HeaderNames.create(name), value);
            }
        });

        for (var cookie : cookies) {
            helidonHeaders.add(HeaderNames.SET_COOKIE, cookie.toString());
        }

        response.contentLength(bytes == null ? 0L : bytes.length);

        response.send(bytes);
    }
}
