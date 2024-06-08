package com.xyzwps.lib.json.element;

import java.util.ArrayList;
import java.util.function.Consumer;

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
            if (this.elements.size() != that.elements.size()) return false;
            for (int i = 0; i < this.elements.size(); i++) {
                if (!this.elements.get(i).equals(that.elements.get(i))) return false;
            }
            return true;
        }
        return false;
    }

    public void forEach(Consumer<JsonElement> consumer) {
        this.elements.forEach(consumer);
    }

    public int length() {
        return elements.size();
    }
}
