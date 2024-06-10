package com.xyzwps.lib.json.element;

public record JsonNull()  implements JsonElement {
    public static JsonNull INSTANCE = new JsonNull();

    @Override
    public String toString() {
        return "null";
    }

    @Override
    public Object toJavaObject() {
        return null;
    }
}
