package com.xyzwps.lib.json.element;

import com.xyzwps.lib.bedrock.lang.Equals;

import java.util.ArrayList;
import java.util.function.ObjIntConsumer;

public final class JsonArray extends ArrayList<JsonElement> implements JsonElement {

    @Override
    public <R> R acceptVisitor(JsonElementVisitor<R> visitor) {
        return visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.acceptVisitor(ToJsonStringVisitor.INSTANCE);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof JsonArray that) {
            return Equals.listItemEquals(this, that);
        }
        return false;
    }

    public void forEach(ObjIntConsumer<JsonElement> consumer) {
        for (int i = 0; i < this.size(); i++) {
            consumer.accept(this.get(i), i);
        }
    }

    public int length() {
        return this.size();
    }
}
