package com.xyzwps.lib.json.util;

public final class CharSet {
    private static final int MAX_LEN = 128;
    private final boolean[] data = new boolean[MAX_LEN];

    public CharSet(char... chars) {
        for (var c : chars) {
            var index = (int) c;
            if (index < MAX_LEN) {
                data[index] = true;
            } else {
                throw new IllegalArgumentException("Only ascii char supported");
            }
        }
    }

    public boolean has(char c) {
        var index = (int) c;
        return index < MAX_LEN && this.data[index];
    }
}
