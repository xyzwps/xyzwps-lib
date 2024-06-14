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
        return this.stringify();
    }

    @Override
    public <R> R visit(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R, C> R visit(C ctx, JsonElementVisitor2<R, C> visitor) {
        return visitor.visit(ctx, this);
    }

}
