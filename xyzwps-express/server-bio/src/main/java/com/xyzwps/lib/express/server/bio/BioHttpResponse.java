package com.xyzwps.lib.express.server.bio;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpProtocol;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;
import com.xyzwps.lib.express.server.commons.SimpleHttpHeaders;
import com.xyzwps.lib.express.server.commons.header.HeaderDateValue;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

public final class BioHttpResponse implements HttpResponse {
    private final OutputStream out;
    private final HttpProtocol protocol;
    private final HttpHeaders headers;

    private HttpStatus status = HttpStatus.OK;

    public BioHttpResponse(OutputStream out, HttpProtocol protocol) {
        this.out = Objects.requireNonNull(out);
        this.protocol = Objects.requireNonNull(protocol);
        this.headers = new SimpleHttpHeaders();
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
        this.headers.set(HttpHeaders.DATE, HeaderDateValue.get());

        try {
            out.write(protocol.value.getBytes());
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
