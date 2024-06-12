package com.xyzwps.lib.json.element;

import java.util.ArrayList;
import java.util.HashMap;

public final class ToJavaObjectVisitor implements JsonElementVisitor<Object> {

    private ToJavaObjectVisitor() {
    }

    public static final ToJavaObjectVisitor INSTANCE = new ToJavaObjectVisitor();

    @Override
    public Object visit(JsonArray jsonArray) {
        var list = new ArrayList<>(jsonArray.length());
        jsonArray.forEach(it -> list.add(switch (it) {
            case JsonNull jn -> this.visit(jn);
            case JsonBoolean jb -> this.visit(jb);
            case JsonInteger ji -> this.visit(ji);
            case JsonDecimal jd -> this.visit(jd);
            case JsonString js -> this.visit(js);
            case JsonObject jo -> this.visit(jo);
            case JsonArray ja -> this.visit(ja);
        }));
        return list;
    }

    @Override
    public Object visit(JsonBoolean jsonBoolean) {
        return jsonBoolean == JsonBoolean.TRUE;
    }

    @Override
    public Object visit(JsonDecimal jsonDecimal) {
        return jsonDecimal.value();
    }

    @Override
    public Object visit(JsonInteger jsonInteger) {
        return jsonInteger.value();
    }

    @Override
    public Object visit(JsonNull jsonNull) {
        return null;
    }

    @Override
    public Object visit(JsonObject jsonObject) {
        var map = new HashMap<>();
        jsonObject.forEach((key, value) -> map.put(key, switch (value) {
            case JsonNull jn -> this.visit(jn);
            case JsonBoolean jb -> this.visit(jb);
            case JsonInteger ji -> this.visit(ji);
            case JsonDecimal jd -> this.visit(jd);
            case JsonString js -> this.visit(js);
            case JsonObject jo -> this.visit(jo);
            case JsonArray ja -> this.visit(ja);
        }));
        return map;
    }

    @Override
    public Object visit(JsonString jsonString) {
        return jsonString.value();
    }
}
