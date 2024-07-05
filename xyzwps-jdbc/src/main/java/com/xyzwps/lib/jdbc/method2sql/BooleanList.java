package com.xyzwps.lib.jdbc.method2sql;

import com.xyzwps.lib.bedrock.Args;

public final class BooleanList {

    private boolean[] list;

    private int size;

    private static final int DEFAULT_CAPACITY = 8;

    public BooleanList() {
        this(DEFAULT_CAPACITY);
    }

    public BooleanList(int cap) {
        Args.gt(cap, 0, "Argument cap must be greater than 0");
        list = new boolean[Math.max(cap, DEFAULT_CAPACITY)];
        size = 0;
    }

    public BooleanList(boolean... list) {
        this.list = list;
        size = list.length;
    }

    public void add(boolean b) {
        if (size == list.length) {
            boolean[] newList = new boolean[size * 2];
            System.arraycopy(list, 0, newList, 0, size);
            list = newList;
        }
        list[size++] = b;
    }

    public boolean get(int index) {
        Args.ge(index, 0, "Argument index must be greater than or equal to 0");
        Args.lt(index, size, "Argument index must be less than size");
        return list[index];
    }

    public boolean hasTrue() {
        for (int i = 0; i < size; i++) {
            if (list[i]) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFalse() {
        for (int i = 0; i < size; i++) {
            if (!list[i]) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof BooleanList bl) {
            if (this.size != bl.size) return false;
            for (int i = 0; i < size; i++) {
                if (this.list[i] != bl.list[i]) return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            var a = list[i];
            result = 31 * result + (a ? 1231 : 1237);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(list[i]);
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
