package com.xyzwps.lib.json.element;

import com.xyzwps.lib.bedrock.lang.Equals;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.ObjIntConsumer;

public final class JsonArray implements JsonElement {
    private final ArrayList<JsonElement> elements = new ArrayList<>();

    public void add(JsonElement element) {
        this.elements.add(element);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder().append('[');
        if (!elements.isEmpty()) {
            sb.append(elements.getFirst().toString());
        }
        for (int i = 1; i < elements.size(); i++) {
            sb.append(',').append(elements.get(i).toString());
        }
        return sb.append(']').toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (obj instanceof JsonArray that) {
            return Equals.listItemEquals(this.elements, that.elements);
        }
        return false;
    }

    public void forEach(Consumer<JsonElement> consumer) {
        this.elements.forEach(consumer);
    }

    public void forEach(ObjIntConsumer<JsonElement> consumer) {
        for (int i = 0; i < elements.size(); i++) {
            consumer.accept(elements.get(i), i);
        }
    }

    public int length() {
        return elements.size();
    }
}
