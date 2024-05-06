package com.xyzwps.lib.express.server.craft;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public final class CraftHttpResponse implements HttpResponse {
    private final OutputStream out;
    private final CraftHttpRequest request;
    private final HttpHeaders headers;

    private HttpStatus status = HttpStatus.OK;

    public CraftHttpResponse(OutputStream out, CraftHttpRequest request) {
        this.out = Objects.requireNonNull(out);
        this.request = Objects.requireNonNull(request);
        this.headers = new CraftHttpHeaders();
    }

    @Override
    public void status(HttpStatus status) {
        this.status = Args.notNull(status, "HttpStatus cannot be null");
    }

    @Override
    public HttpHeaders headers() {
        return headers;
    }

    public void send(byte[] bytes) {
        this.headers.set("Content-Length", Integer.toString(bytes == null ? 0 : bytes.length));
        this.headers.set("Connection", "keep-alive");

        try {
            out.write(request.protocol().getBytes());
            out.write(' ');
            out.write(Integer.toString(status.code).getBytes());
            out.write('\r');
            out.write('\n');

            var headerNames = headers.names();
            for (var name : headerNames) {
                var values = headers.getAll(name);
                for (var value : values) {
                    if (value != null) {
                        out.write(name.getBytes());
                        out.write(':');
                        out.write(' ');
                        out.write(value.getBytes());
                        out.write('\r');
                        out.write('\n');
                    }
                }
            }

            out.write('\r');
            out.write('\n');

            if (bytes != null) {
                out.write(bytes);
            }

            out.flush();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }


    }
}
