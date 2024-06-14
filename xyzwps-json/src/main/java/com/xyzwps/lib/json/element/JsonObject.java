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
        return this.stringify();
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
    public <R> R visit(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public <R, C> R visit(C ctx, JsonElementVisitor2<R, C> visitor) {
        return visitor.visit(ctx, this);
    }
}
