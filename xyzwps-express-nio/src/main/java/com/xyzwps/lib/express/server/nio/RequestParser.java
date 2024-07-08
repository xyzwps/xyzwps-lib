package com.xyzwps.lib.express.server.nio;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.server.commons.*;

import java.nio.charset.StandardCharsets;

public final class RequestParser {

    private enum Stage {START_LINE, HEADER_LINE, PAYLOAD}

    private Stage stage = Stage.START_LINE;

    private final LineReader lineReader = new LineReader();

    StartLine startLine = null;

    final HttpHeaders headers = new SimpleHttpHeaders();

    boolean socketEOF = false;

    public void put(boolean done, byte b) throws InvalidHttpMessageException {
        if (done) {
            return;
        }

        switch (stage) {
            case START_LINE -> {
                var endLine = lineReader.put(b);
                if (endLine) {
                    var line = lineReader.toLineString();
                    this.startLine = StartLineParser.parse(line).rightOrThrow(InvalidHttpMessageException::new);
                    lineReader.reset();
                    this.stage = Stage.HEADER_LINE;
                }
            }
            case HEADER_LINE -> {
                var endLine = lineReader.put(b);
                if (endLine) {
                    var line = lineReader.toLineString();
                    if (line.isEmpty()) {
                        this.stage = Stage.PAYLOAD;
                    } else {
                        var headerLine = HeaderLineParser.parse(line).rightOrThrow(InvalidHttpMessageException::new);
                        headers.append(headerLine.name(), headerLine.value());
                        lineReader.reset();
                    }
                }
            }
            case PAYLOAD -> {
                // TODO: handle payload
                System.out.print((char) b);
            }
        }
    }

    public void eof() {
        this.socketEOF = true;
    }


    private static final class LineReader {
        private final ByteArray byteArray = new ByteArray(128);
        private boolean cr = false;

        private static final byte CR = '\r';
        private static final byte LF = '\n';

        boolean put(byte b) {
            if (cr) {
                switch (b) {
                    case CR -> byteArray.add(CR);
                    case LF -> {
                        return true;
                    }
                    default -> {
                        byteArray.add(CR);
                        byteArray.add(b);
                        cr = false;
                    }
                }
            } else {
                if (b == CR) {
                    cr = true;
                } else {
                    byteArray.add(b);
                }
            }
            return false;
        }

        String toLineString() {
            return byteArray.toString(StandardCharsets.ISO_8859_1);
        }

        void reset() {
            cr = false;
            byteArray.clear();
        }
    }
}
