package com.xyzwps.lib.json.token;

public enum BooleanToken implements JsonToken {
    TRUE(true),
    FALSE(false);

    public final boolean value;

    BooleanToken(boolean value) {
        this.value = value;
    }

    public boolean value() {
        return value;
    }
}
