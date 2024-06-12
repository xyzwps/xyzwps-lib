package com.xyzwps.lib.json.element;

public final class ToJsonStringVisitor implements JsonElementVisitor<String> {

    private ToJsonStringVisitor() {
    }

    public static final ToJsonStringVisitor INSTANCE = new ToJsonStringVisitor();

    @Override
    public String visit(JsonArray ja) {
        var sb = new StringBuilder().append('[');
        if (!ja.isEmpty()) {
            sb.append(ja.getFirst().toString());
        }
        for (int i = 1; i < ja.size(); i++) {
            sb.append(',').append(ja.get(i).toString());
        }
        return sb.append(']').toString();
    }

    @Override
    public String visit(JsonBoolean jb) {
        return jb.stringValue;
    }

    @Override
    public String visit(JsonDecimal jd) {
        return jd.value().toString();
    }

    @Override
    public String visit(JsonInteger ji) {
        return ji.value().toString();
    }

    @Override
    public String visit(JsonNull jn) {
        return "null";
    }

    @Override
    public String visit(JsonObject jo) {
        var sb = new StringBuilder().append('{');
        var env = new Env();
        jo.forEach((key, value) -> {
            if (env.first) env.first = false;
            else sb.append(',');

            sb.append('"');
            appendToHandleEscapeChars(sb, key);
            sb.append('"').append(':').append(value.toString());
        });

        return sb.append('}').toString();
    }

    private static class Env {
        boolean first = true;
    }

    @Override
    public String visit(JsonString js) {
        var sb = new StringBuilder().append('"');
        appendToHandleEscapeChars(sb, js.value());
        return sb.append('"').toString();
    }

    public static void appendToHandleEscapeChars(StringBuilder sb, String value) {
        value.chars().forEachOrdered(it -> {
            char c = (char) it;
            switch (c) {
                case '\\':
                    sb.append("\\\\");
                    break;
                case '"':
                    sb.append("\\\"");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        });
    }
}
