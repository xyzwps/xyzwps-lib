package com.xyzwps.lib.json.element;

import com.xyzwps.lib.bedrock.lang.Equals;

import java.util.Objects;
import java.util.TreeMap;

public final class JsonObject extends TreeMap<String, JsonElement> implements JsonElement {

    public JsonObject put(String property, String str) {
        this.put(Objects.requireNonNull(property), str == null ? JsonNull.INSTANCE : new JsonString(str));
        return this;
    }

    @Override
    public String toString() {
        return this.acceptVisitor(ToJsonStringVisitor.INSTANCE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof JsonObject that) {
            return Equals.mapEntryEquals(this, that);
        }
        return false;
    }

    @Override
    public <R> R acceptVisitor(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }
}
