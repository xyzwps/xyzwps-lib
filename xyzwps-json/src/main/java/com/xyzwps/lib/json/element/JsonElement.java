package com.xyzwps.lib.json.element;

public sealed interface JsonElement permits JsonArray, JsonBoolean, JsonDecimal, JsonInteger, JsonNull, JsonObject, JsonString {
}
