package com.xyzwps.lib.json.element;

import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

import static com.xyzwps.lib.json.element.CompactStringifierVisitor.appendToHandleEscapeChars;

public final class PrettyStringifierStacker {

    private PrettyStringifierStacker() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }

    public static String stringify(JsonElement json) {
        Objects.requireNonNull(json);

        var sb = new StringBuilder();

        var stack = new Stack<Frame>();
        stack.push(switch (json) {
            case JsonArray array -> new Frame.ArrayFrame(array, 0);
            case JsonObject object -> new Frame.ObjectFrame(object, 0);
            default -> new Frame.AtomFrame(json);
        });

        while (!stack.isEmpty()) {
            var result = stack.peek().write(sb);
            switch (result) {
                case FrameState.Halt halt -> stack.push(halt.frame());
                case FrameState.End ignored -> stack.pop();
            }
        }

        return sb.toString();
    }

    private sealed interface FrameState {

        FrameState END = FrameState.End.INSTANCE;

        record Halt(Frame frame) implements FrameState {
        }

        enum End implements FrameState {
            INSTANCE
        }
    }

    private sealed interface Frame {


        FrameState write(StringBuilder sb);

        record AtomFrame(JsonElement element) implements Frame {

            @Override
            public FrameState write(StringBuilder sb) {
                switch (element) {
                    case JsonBoolean jb -> sb.append(jb.stringValue);
                    case JsonDecimal jd -> sb.append(jd.value().toString());
                    case JsonInteger ji -> sb.append(ji.value().toString());
                    case JsonNull ignored -> sb.append("null");
                    case JsonString js -> {
                        sb.append('"');
                        appendToHandleEscapeChars(sb, js.value());
                        sb.append('"');
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + element);
                }
                return FrameState.END;
            }
        }

        String INDENT = "    ";

        private static void appendIndent(StringBuilder sb, int indent) {
            if (indent <= 0) {
                return;
            }
            sb.append(INDENT.repeat(indent));
        }


        final class ArrayFrame implements Frame {

            private int state = 0;
            private final Iterator<JsonElement> iterator;
            private final JsonArray ja;
            private final int indent;

            ArrayFrame(JsonArray array, int indent) {
                this.ja = array;
                this.iterator = array.iterator();
                this.indent = indent;
            }


            @Override
            public FrameState write(StringBuilder sb) {
                if (ja.isEmpty()) {
                    sb.append("[]");
                    return FrameState.END;
                }

                while (true) {
                    switch (state) {
                        case 0 -> {
                            sb.append("[\n");
                            if (iterator.hasNext()) {
                                state = 1;
                            } else {
                                state = 3;
                            }
                        }
                        case 1 -> {
                            var element = iterator.next();
                            appendIndent(sb, indent + 1);
                            state = 2;
                            return switch (element) {
                                case JsonArray array -> new FrameState.Halt(new ArrayFrame(array, indent + 1));
                                case JsonObject object -> new FrameState.Halt(new ObjectFrame(object, indent + 1));
                                default -> new FrameState.Halt(new AtomFrame(element));
                            };
                        }
                        case 2 -> {
                            if (iterator.hasNext()) {
                                sb.append(",\n");
                                state = 1;
                            } else {
                                sb.append('\n');
                                state = 3;
                            }
                        }
                        case 3 -> {
                            appendIndent(sb, indent);
                            sb.append("]");
                            return FrameState.END;
                        }
                    }
                }
            }
        }


        final class ObjectFrame implements Frame {

            private final JsonObject object;
            private final Iterator<String> keys;
            private int state = 0;
            private final int indent;

            private ObjectFrame(JsonObject object, int indent) {
                this.object = object;
                this.keys = object.keySet().iterator();
                this.indent = indent;
            }

            @Override
            public FrameState write(StringBuilder sb) {
                if (object.isEmpty()) {
                    sb.append("{}");
                    return FrameState.END;
                }

                while (true) {
                    switch (state) {
                        case 0 -> {
                            sb.append("{\n");
                            if (keys.hasNext()) {
                                state = 1;
                            } else {
                                state = 3;
                            }
                        }
                        case 1 -> {
                            var key = keys.next();
                            appendIndent(sb, indent + 1);
                            sb.append('"');
                            appendToHandleEscapeChars(sb, key);
                            sb.append("\": ");
                            var value = object.get(key);
                            state = 2;
                            return switch (value) {
                                case JsonArray array -> new FrameState.Halt(new ArrayFrame(array, indent + 1));
                                case JsonObject obj -> new FrameState.Halt(new ObjectFrame(obj, indent + 1));
                                default -> new FrameState.Halt(new AtomFrame(value));
                            };
                        }
                        case 2 -> {
                            if (keys.hasNext()) {
                                sb.append(",\n");
                                state = 1;
                            } else {
                                sb.append('\n');
                                state = 3;
                            }
                        }
                        case 3 -> {
                            appendIndent(sb, indent);
                            sb.append("}");
                            return FrameState.END;
                        }
                    }
                }
            }
        }

    }

}
