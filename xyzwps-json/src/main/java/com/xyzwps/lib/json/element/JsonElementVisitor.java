package com.xyzwps.lib.json.element;

public interface JsonElementVisitor<R> {

    R visit(JsonArray ja);

    R visit(JsonBoolean jb);

    R visit(JsonDecimal jd);

    R visit(JsonInteger ji);

    R visit(JsonNull jn);

    R visit(JsonObject jo);

    R visit(JsonString js);
}
