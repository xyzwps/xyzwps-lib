package com.xyzwps.lib.json.element;

import com.xyzwps.lib.json.SyntaxException;
import com.xyzwps.lib.json.token.*;
import com.xyzwps.lib.json.util.CharGenerator;

import java.util.Objects;
import java.util.Stack;

/**
 * Stack-based parser.
 */
public class StackParser implements ElementParser {

    private final TokenizerFactory tokenizerFactory;

    public StackParser(TokenizerFactory tokenizerFactory) {
        this.tokenizerFactory = Objects.requireNonNull(tokenizerFactory);
    }

    public StackParser() {
        this(TokenizerFactory.DEFAULT);
    }

    @Override
    public JsonElement parse(CharGenerator chars) {
        var tokenizer = this.tokenizerFactory.create(chars);
        JsonElement element = null;

        var stack = new Stack<ValueParser>();
        stack.push(new ValueParser.SimpleValueParser());
        END_WITH_STACK_EMPTY:
        while (!stack.isEmpty()) {
            var result = stack.peek().parse(tokenizer);
            switch (result) {
                case ParserResult.FullResult it -> {
                    stack.pop();
                    if (stack.isEmpty()) {
                        element = it.element;
                        break END_WITH_STACK_EMPTY;
                    } else {
                        stack.peek().append(it.element);
                    }
                }
                case ParserResult.PartResult it -> stack.push(it.parser);
            }
        }

        if (tokenizer.nextToken() != null) {
            throw new SyntaxException("Invalid json");
        }
        return element;
    }


    public sealed interface ParserResult {

        static FullResult full(JsonElement element) {
            return new FullResult(element);
        }

        static PartResult part(ValueParser parser) {
            return new PartResult(parser);
        }

        record FullResult(JsonElement element) implements ParserResult {
        }

        record PartResult(ValueParser parser) implements ParserResult {
        }
    }

    public sealed interface ValueParser {

        ParserResult parse(Tokenizer tokenizer);

        void append(JsonElement element);

        final class SimpleValueParser implements ValueParser {

            private JsonElement element = null;

            @Override
            public ParserResult parse(Tokenizer tokenizer) {
                if (element != null) {
                    return ParserResult.full(element);
                }

                var token = tokenizer.nextToken();
                return switch (token) {
                    case null -> throw new SyntaxException("EOF");
                    case StringToken stringToken -> ParserResult.full(new JsonString(stringToken.value()));
                    case NullToken ignored -> ParserResult.full(JsonNull.INSTANCE);
                    case DecimalToken decimalToken -> ParserResult.full(new JsonDecimal(decimalToken.value()));
                    case IntegerToken integerToken -> ParserResult.full(new JsonInteger(integerToken.value()));
                    case BooleanToken booleanToken -> ParserResult.full(JsonBoolean.of(booleanToken.value()));
                    case ArrayOpenToken ignored -> ParserResult.part(new ArrayParser());
                    case ObjectOpenToken ignored -> ParserResult.part(new ObjectParser());
                    case ArrayCloseToken ignored -> throw new SyntaxException("Unexpected token: array close");
                    case ObjectCloseToken ignored -> throw new SyntaxException("Unexpected token: object close");
                    case SemiToken ignored -> throw new SyntaxException("Unexpected token: semi");
                    case CommaToken ignored -> throw new SyntaxException("Unexpected token: comma");
                };
            }

            @Override
            public void append(JsonElement element) {
                this.element = element;
            }
        }

        final class ArrayParser implements ValueParser {
            private final JsonArray array = new JsonArray();
            private CurrentState state = CurrentState.START;


            /**
             * <pre>
             *      -----> END
             *   ] /
             *    /
             * START ----------------> VALUE ------> END
             *    \                   /  ↑  \   ]
             *     -----> NEST ------>   |   |
             *      {|[    \   :append   |   | ,
             *              \            |   ↓
             *               <-----<-- NEED_VALUE
             *                  {|[
             * </pre>
             */
            enum CurrentState {
                START,
                VALUE,
                NEST,
                NEED_VALUE,
                END
            }

            @Override
            public ParserResult parse(Tokenizer tokenizer) {
                while (true) {
                    switch (this.state) {
                        case END -> {
                            return ParserResult.full(array);
                        }
                        case VALUE -> {
                            switch (tokenizer.nextToken()) {
                                case ArrayCloseToken ignored -> this.state = CurrentState.END;
                                case CommaToken ignored -> this.state = CurrentState.NEED_VALUE;
                                case null -> throw new SyntaxException("EOF");
                                default -> throw new SyntaxException("Unexpected token");
                            }
                        }
                        case NEST -> throw new IllegalStateException("Nest state should be handled by append method");
                        case START, NEED_VALUE -> {
                            switch (tokenizer.nextToken()) {
                                case StringToken stringToken -> {
                                    array.add(new JsonString(stringToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case NullToken ignored -> {
                                    array.add(JsonNull.INSTANCE);
                                    this.state = CurrentState.VALUE;
                                }
                                case DecimalToken decimalToken -> {
                                    array.add(new JsonDecimal(decimalToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case IntegerToken integerToken -> {
                                    array.add(new JsonInteger(integerToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case BooleanToken booleanToken -> {
                                    array.add(JsonBoolean.of(booleanToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case ArrayOpenToken ignored -> {
                                    this.state = CurrentState.NEST;
                                    return ParserResult.part(new ArrayParser());
                                }
                                case ObjectOpenToken ignored -> {
                                    this.state = CurrentState.NEST;
                                    return ParserResult.part(new ObjectParser());
                                }
                                case ArrayCloseToken ignored -> this.state = CurrentState.END;
                                case null -> throw new SyntaxException("EOF");
                                case ObjectCloseToken ignored ->
                                        throw new SyntaxException("Unexpected token: object close");
                                case SemiToken ignored -> throw new SyntaxException("Unexpected token: semi");
                                case CommaToken ignored -> throw new SyntaxException("Unexpected token: comma");
                            }
                        }
                    }
                }
            }

            @Override
            public void append(JsonElement element) {
                if (this.state == CurrentState.NEST) {
                    this.array.add(element);
                    this.state = CurrentState.VALUE;
                } else {
                    throw new IllegalStateException();
                }

            }
        }

        final class ObjectParser implements ValueParser {

            private final JsonObject obj = new JsonObject();
            private String property = null;
            private CurrentState state = CurrentState.START;

            /**
             * <pre>
             *        ----> END        str                    ,
             *     } /             <--<-------- NEED_NAME <---------
             *      /             /                                 \
             *  START -------> NAME -----> NEED_VALUE ----------> VALUE ------> END
             *          str           :         \                   /       }
             *                                   -----> NEST --->-->
             *                                    {|[        :append
             * </pre>
             */
            enum CurrentState {
                START,
                NAME,
                NEED_VALUE,
                VALUE,
                NEST,
                NEED_NAME,
                END
            }

            @Override
            public ParserResult parse(Tokenizer tokenizer) {
                while (true) {
                    switch (state) {
                        case START, NEED_NAME -> {
                            var propertyToken = tokenizer.nextToken();
                            switch (propertyToken) {
                                case StringToken token -> {
                                    this.property = token.value();
                                    this.state = CurrentState.NAME;
                                }
                                case ObjectCloseToken ignored -> this.state = CurrentState.END;
                                case null -> throw new SyntaxException("EOF");
                                default -> throw new SyntaxException("Unexpected token");
                            }
                        }
                        case NAME -> {
                            switch (tokenizer.nextToken()) {
                                case SemiToken ignored -> this.state = CurrentState.NEED_VALUE;
                                case null -> throw new SyntaxException("EOF");
                                default -> throw new SyntaxException("Unexpected token");
                            }
                        }
                        case NEED_VALUE -> {
                            switch (tokenizer.nextToken()) {
                                case StringToken stringToken -> {
                                    obj.put(property, new JsonString(stringToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case NullToken ignored -> {
                                    obj.put(property, JsonNull.INSTANCE);
                                    this.state = CurrentState.VALUE;
                                }
                                case DecimalToken decimalToken -> {
                                    obj.put(property, new JsonDecimal(decimalToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case IntegerToken integerToken -> {
                                    obj.put(property, new JsonInteger(integerToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case BooleanToken booleanToken -> {
                                    obj.put(property, JsonBoolean.of(booleanToken.value()));
                                    this.state = CurrentState.VALUE;
                                }
                                case ArrayOpenToken ignored -> {
                                    this.state = CurrentState.NEST;
                                    return ParserResult.part(new ArrayParser());
                                }
                                case ObjectOpenToken ignored -> {
                                    this.state = CurrentState.NEST;
                                    return ParserResult.part(new ObjectParser());
                                }
                                case null -> throw new SyntaxException("EOF");
                                case ObjectCloseToken ignored ->
                                        throw new SyntaxException("Unexpected token: object close");
                                case ArrayCloseToken ignored ->
                                        throw new SyntaxException("Unexpected token: array close");
                                case SemiToken ignored -> throw new SyntaxException("Unexpected token: semi");
                                case CommaToken ignored -> throw new SyntaxException("Unexpected token: comma");
                            }
                        }
                        case VALUE -> {
                            switch (tokenizer.nextToken()) {
                                case CommaToken ignored -> this.state = CurrentState.NEED_NAME;
                                case ObjectCloseToken ignored -> this.state = CurrentState.END;
                                case null -> throw new SyntaxException("EOF");
                                default -> throw new SyntaxException("Unexpected token");
                            }
                        }
                        case NEST -> throw new IllegalStateException("Nest state should be handled by append method");
                        case END -> {
                            return ParserResult.full(obj);
                        }
                    }
                }
            }

            @Override
            public void append(JsonElement element) {
                if (this.state == CurrentState.NEST) {
                    this.obj.put(property, element);
                    this.state = CurrentState.VALUE;
                } else {
                    throw new IllegalStateException("Unexpected state: " + this.state);
                }
            }
        }
    }

}

