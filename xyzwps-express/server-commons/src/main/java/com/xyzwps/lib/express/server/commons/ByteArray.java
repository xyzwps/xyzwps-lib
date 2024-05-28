package com.xyzwps.lib.express.server.commons;

import java.nio.charset.Charset;

public final class ByteArray {

    private int size;
    private byte[] arr;

    private static final int MIN_CAP = 128;

    public ByteArray(int cap) {
        if (cap < MIN_CAP) {
            throw new IllegalArgumentException("Capacity cannot be less than " + MIN_CAP);
        }

        this.size = 0;
        this.arr = new byte[cap];
    }

    public void add(byte b) {
        if (isFull()) {
            expand();
        }
        this.arr[this.size++] = b;
    }

    public void clear() {
        this.size = 0;
    }

    public String toString(Charset charset) {
        if (size == 0) {
            return "";
        }

        return new String(this.arr, 0, size, charset);
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private boolean isFull() {
        return this.size >= this.arr.length;
    }

    private void expand() {
        byte[] result = new byte[this.arr.length * 2];
        System.arraycopy(this.arr, 0, result, 0, this.arr.length);
        this.arr = result;
    }

}
