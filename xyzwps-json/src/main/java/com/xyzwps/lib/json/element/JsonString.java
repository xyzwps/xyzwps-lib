package com.xyzwps.lib.json.element;

public record JsonString(String value) implements JsonElement {

    @Override
    public String toString() {
        return '"' + value + '"'; // TODO: 处理转义字符
    }
}
