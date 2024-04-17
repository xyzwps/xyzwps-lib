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

    public void forEach(Consumer<JsonElement> consumer) {
        this.elements.forEach(consumer);
    }
}
