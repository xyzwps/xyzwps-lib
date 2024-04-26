package com.xyzwps.lib.express.server;

import com.xyzwps.lib.express.core.HttpMethod;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.IntConsumer;

class SimpleRawRequestParser implements RequestParser {

    @Override
    public RawRequest parse(InputStream in) throws IOException {
        var startLine = new StartLineReader(in).read();
        var headerLines = new ArrayList<HeaderLine>();
        while (true) {
            var headerLine = new HeaderReader(in).read();
            if (headerLine.isEmpty()) {
                break;
            }
            headerLines.add(headerLine.get());
        }
        return new RawRequest(startLine, headerLines, in);
    }

    private static class HeaderReader {
        private final CRLFLineCallbackReader reader;
        private int stage;
        private boolean emptyLine = true;

        HeaderReader(InputStream in) {
            this.reader = new CRLFLineCallbackReader(in);
            this.stage = 0;
        }

        Optional<HeaderLine> read() throws IOException {
            var name = new StringBuilder();
            var value = new StringBuilder();
            reader.read(b -> {
                this.emptyLine = false;
                char c = (char) b;
                switch (this.stage) {
                    case 0 /* read name */ -> {
                        if (c == ':') {
                            this.stage = 1;
                        } else {
                            name.append(c);
                        }
                    }
                    case 1 /* read value */ -> value.append(c);
                    default -> throw new IllegalStateException();
                }
            });

            if (this.emptyLine) {
                return Optional.empty();
            } else {
                return Optional.of(new HeaderLine(name.toString(), value.toString().trim()));
            }
        }
    }


    private static class StartLineReader {
        private final CRLFLineCallbackReader reader;
        private int stage;

        StartLineReader(InputStream in) {
            this.reader = new CRLFLineCallbackReader(in);
            this.stage = 0;
        }

        RawRequest.StartLine read() throws IOException {
            var method = new StringBuilder();
            var url = new StringBuilder();
            var protocol = new StringBuilder();
            reader.read(b -> {
                char c = (char) b;
                switch (this.stage) {
                    case 0 /* read method */ -> {
                        if (c == ' ') {
                            this.stage = 1;
                        } else {
                            method.append(c);
                        }
                    }
                    case 1 /* read url */ -> {
                        if (c == ' ') {
                            this.stage = 2;
                        } else {
                            url.append(c);
                        }
                    }
                    case 2 /* read protocol */ -> protocol.append(c);
                    default -> throw new IllegalStateException();
                }
            });
            // TODO: 不必最后再做转换，早点转换可以早点发现错误
            return new RawRequest.StartLine(HttpMethod.from(method.toString()), url.toString(), protocol.toString());
        }
    }

    private static class CRLFLineCallbackReader {
        private final InputStream in;
        private int state = 0;

        CRLFLineCallbackReader(InputStream in) {
            this.in = in;
        }

        static final int CR = 13;
        static final int LF = 10;

        /**
         * <pre>
         *      其他字符
         *     +--->----+
         *     ^       /
         *     |      /
         *     |     v      \r                        \n
         *     wait(0) -----------> maybe end(1) ------------> end(2);
         *       ^                      |
         *       |                      |
         *       |                      v
         *       +----------<-----------+
         *             其他字符
         * </pre>
         */
        void read(IntConsumer consumer) throws IOException {
            while (true) {
                int b = in.read();
                if (b < 0) throw new IOException("Unexpected byte " + b);

                switch (state) {
                    case 0 -> {
                        if (b == CR) {
                            this.state = 1;
                        } else {
                            consumer.accept(b);
                        }
                    }
                    case 1 -> {
                        if (b == CR) {
                            consumer.accept(CR);
                        } else if (b == LF) {
                            return;
                        } else {
                            consumer.accept(CR);
                            consumer.accept(b);
                            this.state = 0;
                        }
                    }
                    default -> throw new IllegalStateException("Unexpected state " + state);
                }
            }
        }
    }
}
