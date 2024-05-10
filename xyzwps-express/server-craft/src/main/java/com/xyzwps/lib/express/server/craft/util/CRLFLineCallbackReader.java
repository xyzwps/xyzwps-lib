package com.xyzwps.lib.express.server.craft.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.function.IntConsumer;

/**
 *
 */
public final class CRLFLineCallbackReader {
    private final InputStream in;
    private int state = 0;

    public CRLFLineCallbackReader(InputStream in) {
        this.in = in;
    }

    static final int CR = '\r';
    static final int LF = '\n';

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
     *
     * @return true if EOF
     */
    private boolean read(IntConsumer consumer) throws IOException {
        while (true) {
            int b = in.read();
            if (b < 0) return true;

            switch (state) {
                case 0 -> {
                    if (b == CR) {
                        this.state = 1;
                    } else {
                        consumer.accept(b);
                    }
                }
                case 1 -> {
                    switch (b) {
                        case CR -> consumer.accept(CR);
                        case LF -> {
                            return false;
                        }
                        default -> {
                            consumer.accept(CR);
                            consumer.accept(b);
                            this.state = 0;
                        }
                    }
                }
                default -> throw new IllegalStateException("Unexpected state " + state);
            }
        }
    }

    private ByteArray ba = null;

    /**
     * @param charset line charset
     * @return null if nothing to read
     */
    public String readLine(Charset charset) {
        if (ba == null) {
            ba = new ByteArray(2048);
        }
        ba.clear();
        this.state = 0;

        try {
            var eof = this.read(b -> ba.add((byte) b));
            if (eof) {
                return ba.isEmpty() ? null : ba.toString(charset);
            } else {
                return ba.toString(charset);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }


    }
}
