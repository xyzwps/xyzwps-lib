package com.xyzwps.lib.json.token;

import com.xyzwps.lib.json.SyntaxException;
import com.xyzwps.lib.json.util.CharGenerator;

import java.math.BigDecimal;
import java.math.BigInteger;

public class SimpleTokenizer implements Tokenizer {

    private final CharGenerator chars;

    public SimpleTokenizer(CharGenerator generator) {
        this.chars = generator;
    }

    @Override
    public JsonToken nextToken() {
        var picker = detect(chars);
        return picker == null ? null : picker.pick();
    }

    private static TokenPicker detect(CharGenerator chars) {
        while (chars.hasNext()) {
            char curr = chars.next();
            var picker = switch (curr) {
                case ' ', '\r', '\n', '\t' -> null;
                case ',' -> TokenPicker.CommaPicker.INSTANCE;
                case ':' -> TokenPicker.SemiPicker.INSTANCE;
                case '[' -> TokenPicker.ArrayOpenPicker.INSTANCE;
                case ']' -> TokenPicker.ArrayClosePicker.INSTANCE;
                case '{' -> TokenPicker.ObjectOpenPicker.INSTANCE;
                case '}' -> TokenPicker.ObjectClosePicker.INSTANCE;
                case '"' -> new TokenPicker.StringPicker(chars);
                case 't' -> new TokenPicker.TruePicker(chars);
                case 'f' -> new TokenPicker.FalsePicker(chars);
                case 'n' -> new TokenPicker.NullPicker(chars);
                case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-' -> new TokenPicker.NumberPicker(chars, curr);
                default -> throw new SyntaxException("Invalid token");
            };
            if (picker != null) return picker;
        }
        return null;
    }

    public interface TokenPicker {

        JsonToken pick();

        class CommaPicker implements TokenPicker {

            static final TokenPicker INSTANCE = new CommaPicker();

            @Override
            public JsonToken pick() {
                return new CommaToken();
            }
        }

        class SemiPicker implements TokenPicker {

            static final TokenPicker INSTANCE = new SemiPicker();

            @Override
            public JsonToken pick() {
                return new SemiToken();
            }
        }

        class ArrayOpenPicker implements TokenPicker {

            static final TokenPicker INSTANCE = new ArrayOpenPicker();

            @Override
            public JsonToken pick() {
                return new ArrayOpenToken();
            }
        }

        class ArrayClosePicker implements TokenPicker {

            static final TokenPicker INSTANCE = new ArrayClosePicker();

            @Override
            public JsonToken pick() {
                return new ArrayCloseToken();
            }
        }

        class ObjectOpenPicker implements TokenPicker {

            static final TokenPicker INSTANCE = new ObjectOpenPicker();

            @Override
            public JsonToken pick() {
                return new ObjectOpenToken();
            }
        }

        class ObjectClosePicker implements TokenPicker {

            static final TokenPicker INSTANCE = new ObjectClosePicker();

            @Override
            public JsonToken pick() {
                return new ObjectCloseToken();
            }
        }

        class StringPicker implements TokenPicker {

            private final CharGenerator chars;

            public StringPicker(CharGenerator chars) {
                this.chars = chars;
            }

            enum Escaping {
                START,
                UNICODE0,
                UNICODE1,
                UNICODE2,
                UNICODE3,
                NO
            }


            @Override
            public JsonToken pick() {
                var sb = new StringBuilder();
                var escaping = Escaping.NO;
                var unicodeValue = 0;
                while (chars.hasNext()) {
                    var curr = chars.next();
                    switch (escaping) {
                        case START -> {
                            switch (curr) {
                                case '\\', '/', '"', 'b', 'r', 't', 'n', 'f' -> {
                                    sb.append(escapeMap[curr]);
                                    escaping = Escaping.NO;
                                }
                                case 'u' -> escaping = Escaping.UNICODE0;
                                default -> throw new SyntaxException("Unexpected escaping character");
                            }
                        }
                        case UNICODE0 -> {
                            if (hexToIn[curr]) {
                                unicodeValue += hexToValue[curr] << 12;
                                escaping = Escaping.UNICODE1;
                            } else {
                                throw new SyntaxException("Unexpected unicode hex");
                            }
                        }
                        case UNICODE1 -> {
                            if (hexToIn[curr]) {
                                unicodeValue += hexToValue[curr] << 8;
                                escaping = Escaping.UNICODE2;
                            } else {
                                throw new SyntaxException("Unexpected unicode hex");
                            }
                        }
                        case UNICODE2 -> {
                            if (hexToIn[curr]) {
                                unicodeValue += hexToValue[curr] << 4;
                                escaping = Escaping.UNICODE3;
                            } else {
                                throw new SyntaxException("Unexpected unicode hex");
                            }
                        }
                        case UNICODE3 -> {
                            if (hexToIn[curr]) {
                                unicodeValue += hexToValue[curr];
                                escaping = Escaping.NO;
                                sb.append((char) unicodeValue);
                                unicodeValue = 0;
                            } else {
                                throw new SyntaxException("Unexpected unicode hex");
                            }
                        }
                        case NO -> {
                            if (curr == '\\') {
                                escaping = Escaping.START;
                            } else if (curr == '"') {
                                return new StringToken(sb.toString());
                            } else {
                                // TODO: 检查字符是否符合要求
                                sb.append(curr);
                            }
                        }
                    }
                }
                throw new SyntaxException("Invalid string");
            }

            private static final char[] escapeMap = new char[128];
            private static final int[] hexToValue = new int[128];
            private static final boolean[] hexToIn = new boolean[128];

            static {
                escapeMap['\\'] = '\\';
                escapeMap['/'] = '/';
                escapeMap['"'] = '"';
                escapeMap['b'] = '\b';
                escapeMap['r'] = '\r';
                escapeMap['t'] = '\t';
                escapeMap['n'] = '\n';
                escapeMap['f'] = '\f';

                hexToIn['0'] = true;
                hexToIn['1'] = true;
                hexToIn['2'] = true;
                hexToIn['3'] = true;
                hexToIn['4'] = true;
                hexToIn['5'] = true;
                hexToIn['6'] = true;
                hexToIn['7'] = true;
                hexToIn['8'] = true;
                hexToIn['9'] = true;
                hexToIn['a'] = true;
                hexToIn['b'] = true;
                hexToIn['c'] = true;
                hexToIn['d'] = true;
                hexToIn['e'] = true;
                hexToIn['f'] = true;
                hexToIn['A'] = true;
                hexToIn['B'] = true;
                hexToIn['C'] = true;
                hexToIn['D'] = true;
                hexToIn['E'] = true;
                hexToIn['F'] = true;

                hexToValue['0'] = 0;
                hexToValue['1'] = 1;
                hexToValue['2'] = 2;
                hexToValue['3'] = 3;
                hexToValue['4'] = 4;
                hexToValue['5'] = 5;
                hexToValue['6'] = 6;
                hexToValue['7'] = 7;
                hexToValue['8'] = 8;
                hexToValue['9'] = 9;
                hexToValue['a'] = 10;
                hexToValue['b'] = 11;
                hexToValue['c'] = 12;
                hexToValue['d'] = 13;
                hexToValue['e'] = 14;
                hexToValue['f'] = 15;
                hexToValue['A'] = 10;
                hexToValue['B'] = 11;
                hexToValue['C'] = 12;
                hexToValue['D'] = 13;
                hexToValue['E'] = 14;
                hexToValue['F'] = 15;
            }
        }


        class WordPicker implements TokenPicker {
            private final CharGenerator chars;
            private final char[] word;
            private final JsonToken token;

            public WordPicker(CharGenerator chars, char[] word, JsonToken token) {
                this.chars = chars;
                this.word = word;
                this.token = token;
            }

            @Override
            public JsonToken pick() {
                tryPick(word, chars);
                return token;
            }

            static void tryPick(char[] test, CharGenerator chars) {
                for (int i = 1; i < test.length; i++) {
                    if (chars.hasNext()) {
                        if (test[i] != chars.next()) {
                            throw new SyntaxException("Invalid token");
                        }
                    } else {
                        throw new SyntaxException("Invalid token");
                    }
                }
            }
        }

        class TruePicker extends WordPicker {
            private static final char[] word = "true".toCharArray();

            public TruePicker(CharGenerator chars) {
                super(chars, word, new BooleanToken(true));
            }
        }

        class FalsePicker extends WordPicker {
            private static final char[] word = "false".toCharArray();

            public FalsePicker(CharGenerator chars) {
                super(chars, word, new BooleanToken(false));
            }
        }

        class NullPicker extends WordPicker {
            private static final char[] word = "null".toCharArray();

            public NullPicker(CharGenerator chars) {
                super(chars, word, new NullToken());
            }
        }

        class NumberPicker implements TokenPicker {
            private final CharGenerator chars;

            private State state;
            private final StringBuilder sb;

            public NumberPicker(CharGenerator chars, char firstChar) {
                this.chars = chars;
                this.state = State.transfer(State.START, firstChar);
                this.sb = new StringBuilder();
                this.sb.append(firstChar);
            }


            enum State {
                START(0),
                SIGN(0),
                ZERO(0),
                NONE_ZERO(0),

                POINT(0),
                FRACTION(0),

                E(0),
                EXPO_SIGN(0),
                EXPO(0),

                END_INT(1),
                END_FLOAT(1),

                NOT_A_NUMBER(2),
                INVALID_NEGATIVE(2),
                INVALID_FLOAT(2),
                INVALID_EXPO(2);

                /**
                 * <ul>
                 *  <li>0 - 非终态</li>
                 *  <li>1 - 正常结束</li>
                 *  <li>2 - 异常结束</li>
                 * </ul>
                 */
                final int kind;

                State(int kind) {
                    this.kind = kind;
                }

                @SuppressWarnings("SwitchStatementWithTooFewBranches")
                static State transfer(State state, char c) {
                    return switch (state) {
                        case START -> switch (c) {
                            case '-' -> SIGN;
                            case '0' -> ZERO;
                            case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NONE_ZERO;
                            default -> NOT_A_NUMBER;
                        };
                        case SIGN -> switch (c) {
                            case '0' -> ZERO;
                            case '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NONE_ZERO;
                            default -> INVALID_NEGATIVE;
                        };
                        case ZERO -> switch (c) {
                            case '.' -> POINT;
                            default -> END_INT;
                        };
                        case NONE_ZERO -> switch (c) {
                            case '.' -> POINT;
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> NONE_ZERO;
                            default -> END_INT;
                        };
                        case POINT -> switch (c) {
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> FRACTION;
                            default -> INVALID_FLOAT;
                        };
                        case FRACTION -> switch (c) {
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> FRACTION;
                            case 'e', 'E' -> E;
                            default -> END_FLOAT;
                        };
                        case E -> switch (c) {
                            case '-', '+' -> EXPO_SIGN;
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> EXPO;
                            default -> INVALID_EXPO;
                        };
                        case EXPO_SIGN -> switch (c) {
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> EXPO;
                            default -> INVALID_EXPO;
                        };
                        case EXPO -> switch (c) {
                            case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> EXPO;
                            default -> END_FLOAT;
                        };
                        default -> throw new IllegalStateException("MAYBE A BUG: " + switch (state.kind) {
                            case 0 -> "Unhandled non terminal state";
                            case 1 -> "Terminal state is unacceptable";
                            case 2 -> "Failure state is unacceptable";
                            default -> "Unrecognized kind.";
                        });
                    };
                }

            }

            @Override
            public JsonToken pick() {
                while (chars.hasNext()) {
                    var curr = chars.seek();
                    this.state = State.transfer(this.state, curr);
                    switch (this.state.kind) {
                        case 0 -> this.sb.append(chars.next());
                        case 1 -> {
                            return switch (this.state) {
                                case END_INT -> new IntegerToken(new BigInteger(this.sb.toString()));
                                case END_FLOAT -> new DecimalToken(new BigDecimal(this.sb.toString()));
                                default -> throw new IllegalStateException("MAYBE A BUG: Unrecognized end.");
                            };
                        }
                        case 2 -> throw new SyntaxException(this.state.name());
                        default -> throw new IllegalStateException("MAYBE A BUG: Unrecognized kind.");
                    }
                }

                this.state = State.transfer(this.state, ' '); // 到了末尾，随便补一个字符，看看啥情况
                return switch (this.state.kind) {
                    case 0 -> throw new SyntaxException("Invalid number");
                    case 1 -> switch (this.state) {
                        case END_INT -> new IntegerToken(new BigInteger(this.sb.toString()));
                        case END_FLOAT -> new DecimalToken(new BigDecimal(this.sb.toString()));
                        default -> throw new IllegalStateException("MAYBE A BUG: Unrecognized end.");
                    };
                    case 2 -> throw new SyntaxException(this.state.name());
                    default -> throw new IllegalStateException("MAYBE A BUG: Unrecognized kind.");
                };
            }
        }
    }
}
