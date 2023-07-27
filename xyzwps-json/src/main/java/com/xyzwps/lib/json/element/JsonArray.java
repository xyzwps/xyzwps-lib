package com.xyzwps.lib.json.element;

import java.util.ArrayList;

public final class JsonArray implements JsonElement {
    private final ArrayList<JsonElement> elements = new ArrayList<>();

    public void add(JsonElement element) {
        this.elements.add(element);
    }

    @Override
    public String toString() {
        var sb = new StringBuilder().append('[');
        if (elements.size() > 0) {
            sb.append(elements.get(0).toString());
        }
        for (int i = 1; i < elements.size(); i++) {
            sb.append(',').append(elements.get(i).toString());
        }
        return sb.append(']').toString();
    }
}
