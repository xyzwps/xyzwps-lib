package com.xyzwps.lib.json.element;

public record JsonString(String value) implements JsonElement {

    @Override
    public String toString() {
        return this.acceptVisitor(ToJsonStringVisitor.INSTANCE);
    }

    @Override
    public <R> R acceptVisitor(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
