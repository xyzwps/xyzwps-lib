package com.xyzwps.lib.json.element;

public enum JsonBoolean implements JsonElement {
    TRUE(true), FALSE(false);

    public final boolean value;

    public final String stringValue;

    JsonBoolean(boolean value) {
        this.value = value;
        this.stringValue = value ? "true" : "false";
    }

    public static JsonBoolean of(boolean b) {
        return b ? TRUE : FALSE;
    }

    @Override
    public String toString() {
        return this.acceptVisitor(ToJsonStringVisitor.INSTANCE);
    }

    @Override
    public <R> R acceptVisitor(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

}
