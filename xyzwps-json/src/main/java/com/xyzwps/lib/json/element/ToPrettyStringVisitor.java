package com.xyzwps.lib.json.element;

import static com.xyzwps.lib.json.element.ToJsonStringVisitor.appendToHandleEscapeChars;

public final class ToPrettyStringVisitor implements JsonElementVisitor<StringBuilder> {

    private int indent = 0;

    private static final String INDENT = "    ";

    private final StringBuilder sb = new StringBuilder();

    @Override
    public StringBuilder visit(JsonArray ja) {
        if (ja.isEmpty()) {
            sb.append("[]");
            return sb;
        }

        sb.append("[\n");
        indent++;
        int size = ja.length();
        for (int i = 0; i < size; i++) {
            var it = ja.get(i);
            appendIndent();
            it.acceptVisitor(this);
            if (i == size - 1) {
                sb.append("\n");
            } else {
                sb.append(",\n");
            }
        }
        indent--;
        appendIndent();
        sb.append("]");
        return sb;
    }

    private void appendIndent() {
        for (int i = 0; i < indent; i++) {
            sb.append(INDENT);
        }
    }

    @Override
    public StringBuilder visit(JsonBoolean jb) {
        sb.append(jb.stringValue);
        return sb;
    }

    @Override
    public StringBuilder visit(JsonDecimal jd) {
        sb.append(jd.value().toString());
        return sb;
    }

    @Override
    public StringBuilder visit(JsonInteger ji) {
        sb.append(ji.value().toString());
        return sb;
    }

    @Override
    public StringBuilder visit(JsonNull jn) {
        sb.append("null");
        return sb;
    }

    @Override
    public StringBuilder visit(JsonObject jo) {
        if (jo.isEmpty()) {
            sb.append("{}");
            return sb;
        }

        sb.append("{");
        indent++;

        var env = new Env();
        jo.forEach((key, value) -> {
            if (env.isFirst) {
                env.isFirst = false;
                sb.append('\n');
            } else {
                sb.append(",\n");
            }
            appendIndent();
            sb.append('"');
            appendToHandleEscapeChars(sb, key);
            sb.append('"').append(": ");
            value.acceptVisitor(this);
        });
        sb.append('\n');

        indent--;
        appendIndent();
        sb.append('}');
        return sb;
    }

    private static class Env {
        boolean isFirst = true;
    }

    @Override
    public StringBuilder visit(JsonString js) {
        sb.append('"');
        appendToHandleEscapeChars(sb, js.value());
        sb.append('"');
        return sb;
    }
}
