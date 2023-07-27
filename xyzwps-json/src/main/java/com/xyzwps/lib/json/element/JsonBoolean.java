package com.xyzwps.lib.json.element;

public enum JsonBoolean implements JsonElement {
    TRUE, FALSE;

    public static JsonBoolean of(boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
