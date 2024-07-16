package com.xyzwps.lib.openapi.json;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class JsonBuilder {

    private final List<Token> tokens = new ArrayList<>();

    public String toCompactString() {
        var sb = new StringBuilder();
        for (var token : tokens) {
            switch (token) {
                case Token.Keyword keyword -> sb.append(keyword.value);
                case Token.StringToken stringToken -> sb.append('"').append(stringToken.unescaped()).append('"');
                case Token.IntToken intToken -> sb.append(intToken.value);
                case Token.DecimalToken decimalToken -> sb.append(decimalToken.value);
            }
        }
        return sb.toString();
    }

    public String toPrettyString() {
        var sb = new StringBuilder();
        int indent = 0;
        for (var token : tokens) {
            switch (token) {
                case Token.Keyword kw -> {
                    switch (kw) {
                        case NULL -> sb.append("null");
                        case TRUE -> sb.append("true");
                        case FALSE -> sb.append("false");
                        case COLON -> sb.append(": ");
                        case COMMA -> {
                            sb.append(",\n");
                            addIndent(sb, indent);
                        }
                        case ARRAY_OPEN -> {
                            sb.append("[\n");
                            indent++;
                            addIndent(sb, indent);
                        }
                        case ARRAY_CLOSE -> {
                            sb.append("\n");
                            indent--;
                            addIndent(sb, indent);
                            sb.append("]");
                        }
                        case OBJECT_OPEN -> {
                            sb.append("{\n");
                            indent++;
                            addIndent(sb, indent);
                        }
                        case OBJECT_CLOSE -> {
                            sb.append("\n");
                            indent--;
                            addIndent(sb, indent);
                            sb.append("}");
                        }
                    }
                }
                case Token.StringToken stringToken -> sb.append('"').append(stringToken.unescaped()).append('"');
                case Token.IntToken intToken -> sb.append(intToken.value);
                case Token.DecimalToken decimalToken -> sb.append(decimalToken.value);
            }
        }
        return sb.toString();
    }

    private static void addIndent(StringBuilder sb, int indent) {
        if (indent <= 0) return;

        sb.append("  ".repeat(indent));
    }

    public JsonBuilder arrayOpen() {
        tokens.add(Token.Keyword.ARRAY_OPEN);
        return this;
    }

    public JsonBuilder arrayClose() {
        tokens.add(Token.Keyword.ARRAY_CLOSE);
        return this;
    }

    public JsonBuilder objectOpen() {
        tokens.add(Token.Keyword.OBJECT_OPEN);
        return this;
    }

    public JsonBuilder objectClose() {
        tokens.add(Token.Keyword.OBJECT_CLOSE);
        return this;
    }

    public JsonBuilder nullValue() {
        tokens.add(Token.Keyword.NULL);
        return this;
    }

    public JsonBuilder comma() {
        tokens.add(Token.Keyword.COMMA);
        return this;
    }

    public JsonBuilder colon() {
        tokens.add(Token.Keyword.COLON);
        return this;
    }

    public JsonBuilder value(boolean value) {
        tokens.add(value ? Token.Keyword.TRUE : Token.Keyword.FALSE);
        return this;
    }

    public JsonBuilder value(String value) {
        tokens.add(new Token.StringToken(value));
        return this;
    }


    sealed interface Token {
        enum Keyword implements Token {
            NULL("null"),
            TRUE("true"),
            FALSE("false"),
            ARRAY_OPEN("["),
            ARRAY_CLOSE("]"),
            OBJECT_OPEN("{"),
            OBJECT_CLOSE("}"),
            COLON(":"),
            COMMA(",");

            final String value;

            Keyword(String value) {
                this.value = value;
            }
        }

        record StringToken(String value) implements Token {

            public String unescaped() {
                // TODO: 处理这个东西
                return value.replace("\\", "\\\\").replace("\"", "\\\"");
            }
        }

        record IntToken(BigInteger value) implements Token {
        }

        record DecimalToken(BigDecimal value) implements Token {
        }
    }

}
