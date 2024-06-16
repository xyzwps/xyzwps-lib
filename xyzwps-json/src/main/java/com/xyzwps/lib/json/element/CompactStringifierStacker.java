package com.xyzwps.lib.json.element;

import java.util.Iterator;
import java.util.Objects;
import java.util.Stack;

// TODO: 提取出来
import static com.xyzwps.lib.json.element.CompactStringifierVisitor.appendToHandleEscapeChars;

public final class CompactStringifierStacker {

    private CompactStringifierStacker() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }

    public static String stringify(JsonElement json) {
        Objects.requireNonNull(json);

        var sb = new StringBuilder();

        var stack = new Stack<Frame>();
        stack.push(switch (json) {
            case JsonArray array -> new Frame.ArrayFrame(array);
            case JsonObject object -> new Frame.ObjectFrame(object);
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

        FrameState END = End.INSTANCE;

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
                    case JsonDecimal jd -> sb.append(jd.value());
                    case JsonInteger ji -> sb.append(ji.value());
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

        final class ArrayFrame implements Frame {

            private int state = 0;
            private final Iterator<JsonElement> iterator;

            private ArrayFrame(JsonArray array) {
                this.iterator = array.iterator();
            }

            @Override
            public FrameState write(StringBuilder sb) {
                while (true) {
                    switch (state) {
                        case 0 -> {
                            sb.append('[');
                            if (iterator.hasNext()) {
                                state = 1;
                            } else {
                                state = 3;
                            }
                        }
                        case 1 -> {
                            var element = iterator.next();
                            state = 2;
                            return switch (element) {
                                case JsonArray array -> new FrameState.Halt(new ArrayFrame(array));
                                case JsonObject object -> new FrameState.Halt(new ObjectFrame(object));
                                default -> new FrameState.Halt(new AtomFrame(element));
                            };
                        }
                        case 2 -> {
                            if (iterator.hasNext()) {
                                sb.append(',');
                                state = 1;
                            } else {
                                state = 3;
                            }
                        }
                        case 3 -> {
                            sb.append(']');
                            return FrameState.END;
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + state);
                    }
                }
            }
        }

        final class ObjectFrame implements Frame {

            private final JsonObject object;
            private final Iterator<String> keys;
            private int state = 0;

            private ObjectFrame(JsonObject object) {
                this.object = object;
                this.keys = object.keySet().iterator();
            }

            @Override
            public FrameState write(StringBuilder sb) {
                while (true) {
                    switch (state) {
                        case 0 -> {
                            sb.append('{');
                            if (keys.hasNext()) {
                                state = 1;
                            } else {
                                state = 3;
                            }
                        }
                        case 1 -> {
                            var key = keys.next();
                            state = 2;
                            sb.append('"');
                            appendToHandleEscapeChars(sb, key);
                            sb.append('"').append(':');
                            var element = object.get(key);
                            return switch (element) {
                                case JsonArray array -> new FrameState.Halt(new ArrayFrame(array));
                                case JsonObject obj -> new FrameState.Halt(new ObjectFrame(obj));
                                default -> new FrameState.Halt(new AtomFrame(element));
                            };
                        }
                        case 2 -> {
                            if (keys.hasNext()) {
                                sb.append(',');
                                state = 1;
                            } else {
                                state = 3;
                            }
                        }
                        case 3 -> {
                            sb.append('}');
                            return FrameState.END;
                        }
                    }
                }
            }
        }
    }
}
