package com.xyzwps.lib.json.element;

public enum JsonBoolean implements JsonElement {
    TRUE(true), FALSE(false);

    public final boolean value;

    JsonBoolean(boolean value) {
        this.value = value;
    }

    public static JsonBoolean of(boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
