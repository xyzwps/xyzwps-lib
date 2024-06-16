package com.xyzwps.lib.json.element;

import static com.xyzwps.lib.json.element.CompactStringifierVisitor.appendToHandleEscapeChars;

public final class PrettyStringifierVisitor implements JsonElementVisitor2<StringBuilder, Integer> {

    private static final String INDENT = "    ";

    private final StringBuilder sb = new StringBuilder();

    @Override
    public StringBuilder visit(Integer indent, JsonArray ja) {
        if (ja.isEmpty()) {
            sb.append("[]");
            return sb;
        }

        sb.append("[\n");
        int size = ja.length();
        for (int i = 0; i < size; i++) {
            var it = ja.get(i);
            appendIndent(indent + 1);
            it.visit(indent + 1, this);
            if (i == size - 1) {
                sb.append("\n");
            } else {
                sb.append(",\n");
            }
        }
        appendIndent(indent);
        sb.append("]");
        return sb;
    }

    private void appendIndent(int indent) {
        if (indent <= 0) {
            return;
        }
        sb.append(INDENT.repeat(indent));
    }

    @Override
    public StringBuilder visit(Integer indent, JsonBoolean jb) {
        sb.append(jb.stringValue);
        return sb;
    }

    @Override
    public StringBuilder visit(Integer indent, JsonDecimal jd) {
        sb.append(jd.value().toString());
        return sb;
    }

    @Override
    public StringBuilder visit(Integer indent, JsonInteger ji) {
        sb.append(ji.value().toString());
        return sb;
    }

    @Override
    public StringBuilder visit(Integer indent, JsonNull jn) {
        sb.append("null");
        return sb;
    }

    @Override
    public StringBuilder visit(Integer indent, JsonObject jo) {
        if (jo.isEmpty()) {
            sb.append("{}");
            return sb;
        }

        sb.append("{");

        var env = new Env();
        jo.forEach((key, value) -> {
            if (env.isFirst) {
                env.isFirst = false;
                sb.append('\n');
            } else {
                sb.append(",\n");
            }
            appendIndent(indent + 1);
            sb.append('"');
            appendToHandleEscapeChars(sb, key);
            sb.append('"').append(": ");
            value.visit(indent + 1, this);
        });
        sb.append('\n');

        appendIndent(indent);
        sb.append('}');
        return sb;
    }

    private static class Env {
        boolean isFirst = true;
    }

    @Override
    public StringBuilder visit(Integer indent, JsonString js) {
        sb.append('"');
        appendToHandleEscapeChars(sb, js.value());
        sb.append('"');
        return sb;
    }
}
