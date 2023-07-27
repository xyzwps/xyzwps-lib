package com.xyzwps.lib.json.element;

import com.xyzwps.lib.json.SyntaxException;
import com.xyzwps.lib.json.token.*;
import com.xyzwps.lib.json.util.CharGenerator;

import java.util.Objects;

/**
 * Simple recursive parser.
 */
public class SimpleParser implements ElementParser {

    private final TokenizerFactory tokenizerFactory;

    public SimpleParser(TokenizerFactory tokenizerFactory) {
        this.tokenizerFactory = Objects.requireNonNull(tokenizerFactory);
    }

    public SimpleParser() {
        this(TokenizerFactory.DEFAULT);
    }

    @Override
    public JsonElement parse(CharGenerator chars) {
        var tokenizer = this.tokenizerFactory.create(chars);
        var element = parseValue(tokenizer);
        if (tokenizer.nextToken() != null) {
            throw new SyntaxException("Invalid json");
        }
        return element;
    }

    private static JsonElement parseValue(Tokenizer tokenizer) {
        var token = tokenizer.nextToken();
        return switch (token) {
            case null -> throw new SyntaxException("EOF");
            case StringToken stringToken -> new JsonString(stringToken.value());
            case NullToken ignored -> JsonNull.INSTANCE;
            case DecimalToken decimalToken -> new JsonDecimal(decimalToken.value());
            case IntegerToken integerToken -> new JsonInteger(integerToken.value());
            case BooleanToken booleanToken -> JsonBoolean.of(booleanToken.value());
            case ArrayOpenToken ignored -> parseArray(tokenizer);
            case ObjectOpenToken ignored -> parseObject(tokenizer);
            case ArrayCloseToken ignored -> throw new SyntaxException("Unexpected token: array close");
            case ObjectCloseToken ignored -> throw new SyntaxException("Unexpected token: object close");
            case SemiToken ignored -> throw new SyntaxException("Unexpected token: semi");
            case CommaToken ignored -> throw new SyntaxException("Unexpected token: comma");
        };
    }

    private static JsonElement parseArray(Tokenizer tokenizer) {
        var array = new JsonArray();
        while (true) {
            switch (tokenizer.nextToken()) {
                case StringToken stringToken -> array.add(new JsonString(stringToken.value()));
                case NullToken ignored -> array.add(JsonNull.INSTANCE);
                case DecimalToken decimalToken -> array.add(new JsonDecimal(decimalToken.value()));
                case IntegerToken integerToken -> array.add(new JsonInteger(integerToken.value()));
                case BooleanToken booleanToken -> array.add(JsonBoolean.of(booleanToken.value()));
                case ArrayOpenToken ignored -> array.add(parseArray(tokenizer));
                case ObjectOpenToken ignored -> array.add(parseObject(tokenizer));
                case ArrayCloseToken ignored -> {
                    return array;
                }
                case null -> throw new SyntaxException("EOF");
                case ObjectCloseToken ignored -> throw new SyntaxException("Unexpected token: object close");
                case SemiToken ignored -> throw new SyntaxException("Unexpected token: semi");
                case CommaToken ignored -> throw new SyntaxException("Unexpected token: comma");
            }

            switch (tokenizer.nextToken()) {
                case CommaToken ignored -> {
                    /* skip comma */
                }
                case ArrayCloseToken ignored -> {
                    return array;
                }
                case null -> throw new SyntaxException("EOF");
                default -> throw new SyntaxException("Unexpected token");
            }
        }
    }

    private static JsonElement parseObject(Tokenizer tokenizer) {
        var obj = new JsonObject();

        var propertyToken = tokenizer.nextToken();
        String property;
        switch (propertyToken) {
            case StringToken token -> property = token.value();
            case ObjectCloseToken ignored -> {
                return obj;
            }
            case null -> throw new SyntaxException("EOF");
            default -> throw new SyntaxException("Unexpected token");
        }

        while (true) {
            switch (tokenizer.nextToken()) {
                case SemiToken ignored -> {/* skip semi */}
                case null -> throw new SyntaxException("EOF");
                default -> throw new SyntaxException("Unexpected token");
            }

            switch (tokenizer.nextToken()) {
                case StringToken stringToken -> obj.put(property, new JsonString(stringToken.value()));
                case NullToken ignored -> obj.put(property, JsonNull.INSTANCE);
                case DecimalToken decimalToken -> obj.put(property, new JsonDecimal(decimalToken.value()));
                case IntegerToken integerToken -> obj.put(property, new JsonInteger(integerToken.value()));
                case BooleanToken booleanToken -> obj.put(property, JsonBoolean.of(booleanToken.value()));
                case ArrayOpenToken ignored -> obj.put(property, parseArray(tokenizer));
                case ObjectOpenToken ignored -> obj.put(property, parseObject(tokenizer));
                case null -> throw new SyntaxException("EOF");
                case ObjectCloseToken ignored -> throw new SyntaxException("Unexpected token: object close");
                case ArrayCloseToken ignored -> throw new SyntaxException("Unexpected token: array close");
                case SemiToken ignored -> throw new SyntaxException("Unexpected token: semi");
                case CommaToken ignored -> throw new SyntaxException("Unexpected token: comma");
            }


            switch (tokenizer.nextToken()) {
                case CommaToken ignored -> {
                    switch (tokenizer.nextToken()) {
                        case StringToken token -> property = token.value();
                        case null -> throw new SyntaxException("EOF");
                        default -> throw new SyntaxException("Unexpected token");
                    }
                }
                case ObjectCloseToken ignored -> {
                    return obj;
                }
                case null -> throw new SyntaxException("EOF");
                default -> throw new SyntaxException("Unexpected token");
            }
        }
    }
}
