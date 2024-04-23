package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SimpleHttpResponse implements HttpResponse {
    private final OutputStream out;
    private final HttpRequest request;

    private HttpStatus status = HttpStatus.OK;
    private List<HeaderLine> headers = new ArrayList<>(8);

    public SimpleHttpResponse(OutputStream out, HttpRequest request) {
        this.out = Objects.requireNonNull(out);
        this.request = Objects.requireNonNull(request);
    }


    @Override
    public HttpResponse status(HttpStatus status) {
        this.status = Args.notNull(status, "HttpStatus cannot be null");
        return this;
    }

    /**
     * TODO: 如何理解这个语义
     */
    @Override
    public HttpResponse header(String name, String value) {
        this.headers.add(new HeaderLine(name, value));
        return this;
    }

    @Override
    public void send(byte[] bytes) {
        this
                .header("Content-Length", Integer.toString(bytes == null ? 0 : bytes.length))
                .header("Connection", "keep-alive");

        try {
            out.write(request.protocol().getBytes());
            out.write(' ');
            out.write(Integer.toString(status.code).getBytes());
            out.write('\r');
            out.write('\n');

            for (var header : headers) {
                out.write(header.name().getBytes());
                out.write(':');
                out.write(' ');
                out.write(header.value().getBytes());
                out.write('\r');
                out.write('\n');
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
