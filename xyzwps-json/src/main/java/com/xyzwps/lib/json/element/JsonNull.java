package com.xyzwps.lib.json.element;

public record JsonNull()  implements JsonElement {
    public static JsonNull INSTANCE = new JsonNull();

    @Override
    public String toString() {
        return this.acceptVisitor(ToJsonStringVisitor.INSTANCE);
    }

    @Override
    public <R> R acceptVisitor(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
