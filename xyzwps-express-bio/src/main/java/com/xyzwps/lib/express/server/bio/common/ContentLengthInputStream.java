package com.xyzwps.lib.express.server.bio.common;

import com.xyzwps.lib.bedrock.Args;

import java.io.*;
import java.util.Objects;

/**
 * An {@link InputStream} that reads a fixed number of bytes from the wrapped {@link InputStream}.
 * Not thread-safe.
 *
 * @see <a href="https://www.rfc-editor.org/rfc/rfc9110#name-content-length">RFC-9110</a>
 */
public class ContentLengthInputStream extends InputStream {

    private final InputStream in;
    private final int length;
    private int pos;
    private boolean closed;

    public ContentLengthInputStream(InputStream in, int length) {
        this.in = Args.notNull(in, "InputStream cannot be null");
        this.length = Args.ge(length, 0, "Content length cannot be negative");
        this.closed = false;
        this.pos = 0;
    }


    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        ensureOpen();

        if (len == 0) {
            return 0;
        }
        if (isEndOfFile()) {
            return -1;
        }

        int targetLength = pos + len <= length ? len : length - pos;
        var read = this.in.read(b, off, targetLength);
        if (read != targetLength) {
            throw new EOFException("Wrapped input stream EOF before reading enough bytes.");
        }
        this.pos += targetLength;
        return read;
    }

    @Override
    public int read() throws IOException {
        ensureOpen();

        if (isEndOfFile()) {
            return -1;
        }

        pos += 1;
        return this.in.read();
    }

    private boolean isEndOfFile() {
        return pos >= length;
    }

    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed.");
        }
    }

    @Override
    public void close() throws IOException {
        ensureOpen();
        this.closed = true;
    }

    @Override
    public int available() {
        return length - pos;
    }

    private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    @Override
    public byte[] readAllBytes() throws IOException {
        return this.readNBytes(available());
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        ensureOpen();
        if (len == 0) {
            return EMPTY_BYTE_ARRAY;
        }

        if (isEndOfFile()) {
            return EMPTY_BYTE_ARRAY;
        }

        int targetLength = pos + len <= length ? len : length - pos;
        var bytes = this.in.readNBytes(targetLength);
        if (bytes.length != targetLength) {
            throw new EOFException("Wrapped input stream EOF before reading enough bytes.");
        }
        this.pos += targetLength;
        return bytes;
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);
        ensureOpen();
        if (isEndOfFile()) {
            return 0;
        }

        if (len == 0) {
            return 0;
        }

        int targetLength = pos + len <= length ? len : length - pos;
        var read = this.in.readNBytes(b, off, targetLength);
        if (read != targetLength) {
            throw new EOFException("Wrapped input stream EOF before reading enough bytes.");
        }
        this.pos += targetLength;
        return targetLength;
    }

    @Override
    public long skip(long n) throws IOException {
        ensureOpen();
        if (n <= 0) {
            return 0;
        }

        long actualSkip = Math.min(n, available());
        long skipped = this.in.skip(actualSkip);
        if (skipped != actualSkip) {
            throw new EOFException("Wrapped input stream EOF before reading enough bytes.");
        }
        this.pos += (int) skipped;
        return skipped;
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        ensureOpen();
        long transferred = 0;
        byte[] buffer = new byte[8192];
        int read;
        while ((read = this.read(buffer, 0, buffer.length)) >= 0) {
            out.write(buffer, 0, read);
            if (transferred < Long.MAX_VALUE) {
                try {
                    transferred = Math.addExact(transferred, read);
                } catch (ArithmeticException ignore) {
                    transferred = Long.MAX_VALUE;
                }
            }
        }
        return transferred;
    }
}
