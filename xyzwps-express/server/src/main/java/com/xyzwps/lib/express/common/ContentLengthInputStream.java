package com.xyzwps.lib.express.common;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.bedrock.UnimplementedException;

import java.io.*;
import java.util.Objects;

public class ContentLengthInputStream extends InputStream {

    private final InputStream in;
    private final int length;

    private int pos;

    private final byte[] buffer;
    private int bufferStart;
    private int bufferEnd;
    private int bufferReadPos;

    private boolean closed;

    public ContentLengthInputStream(InputStream in, int bufferSize, int length) {
        this.in = Args.notNull(in, "InputStream cannot be null");
        this.length = Args.ge(length, 0, "Content length cannot be negative");
        this.closed = false;
        this.pos = 0;

        this.buffer = new byte[Args.gt(bufferSize, 0, "Buffer size should be positive")];
        this.bufferStart = 0;
        this.bufferEnd = 0;
        this.bufferReadPos = 0;
    }


    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        Objects.checkFromIndexSize(off, len, b.length);

        if (len == 0) {
            return 0;
        }
        if (pos >= length) {
            return -1;
        }

        int count = 0;
        int offset = off;
        int leftLen = len;
        while (count < len) {
            if (isEndOfFile()) {
                break;
            }

            if (bufferIsEmpty()) {
                preReadIntoBuffer();
            }

            int bufferLeft = bufferLeft();
            if (bufferLeft >= leftLen) {
                readFromBuffer(b, offset, leftLen);
                pos += leftLen;
                count += leftLen;
                break;
            } else {
                readFromBuffer(b, offset, bufferLeft);
                pos += bufferLeft;
                count += bufferLeft;
                offset += bufferLeft;
                leftLen -= bufferLeft;
            }
        }
        return count;
    }

    private void readFromBuffer(byte[] b, int offset, int len) {
        System.arraycopy(buffer, bufferStart, b, offset, len);
        this.bufferStart += len;
    }

    @Override
    public int read() throws IOException {
        shouldNotBeClosed();

        if (isEndOfFile()) {
            return -1;
        }

        if (bufferIsEmpty()) {
            preReadIntoBuffer();
        }

        pos += 1;
        return buffer[bufferStart++] & 0xFF;
    }


    private boolean isEndOfFile() {
        return pos >= length;
    }

    private boolean bufferIsEmpty() {
        return bufferEnd - bufferStart == 0;
    }

    private int bufferLeft() {
        return bufferEnd - bufferStart;
    }

    private void preReadIntoBuffer() throws IOException {
        this.bufferStart = 0;
        this.bufferEnd = 0;

        int readLen = buffer.length;
        if (bufferReadPos + buffer.length <= length) {
            // use init value
        } else if (bufferReadPos < length) {
            // the last batch
            readLen = length - bufferReadPos;
        } else {
            throw new EOFException();
        }

        int result = in.read(this.buffer, 0, readLen);
        if (result == -1) {
            throw new EOFException("TODO: 不够了");
        }
        this.bufferEnd = readLen;
        this.bufferReadPos += readLen;
    }

    private void shouldNotBeClosed() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed.");
        }
    }

    @Override
    public void close() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed.");
        }
        this.closed = true;
    }

    @Override
    public int available() {
        return length - pos;
    }


    // ----- unimplemented -----


    @Override
    public byte[] readAllBytes() throws IOException {
        throw UnimplementedException.wontDo();
    }

    @Override
    public byte[] readNBytes(int len) throws IOException {
        throw UnimplementedException.wontDo();
    }

    @Override
    public int readNBytes(byte[] b, int off, int len) throws IOException {
        throw UnimplementedException.wontDo();
    }

    @Override
    public long skip(long n) throws IOException {
        throw UnimplementedException.wontDo();
    }

    @Override
    public void skipNBytes(long n) throws IOException {
        throw UnimplementedException.wontDo();
    }


    @Override
    public void mark(int readlimit) {
        throw UnimplementedException.wontDo();
    }

    @Override
    public void reset() throws IOException {
        throw UnimplementedException.wontDo();
    }

    @Override
    public boolean markSupported() {
        throw UnimplementedException.wontDo();
    }

    @Override
    public long transferTo(OutputStream out) throws IOException {
        throw UnimplementedException.wontDo();
    }
}
