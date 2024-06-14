package com.xyzwps.lib.json.element;

public interface JsonElementVisitor2<R, C> {

    R visit(C ctx, JsonArray ja);

    R visit(C ctx, JsonBoolean jb);

    R visit(C ctx, JsonDecimal jd);

    R visit(C ctx, JsonInteger ji);

    R visit(C ctx, JsonNull jn);

    R visit(C ctx, JsonObject jo);

    R visit(C ctx, JsonString js);
}
