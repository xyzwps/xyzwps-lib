package com.xyzwps.lib.json.element;

import java.util.*;

public final class ToJavaObjectStacker {

    private ToJavaObjectStacker() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }

    public static Object toJavaObject(JsonElement json) {
        Objects.requireNonNull(json);

        var stack = new Stack<Frame>();
        var register = new Register();
        stack.push(switch (json) {
            case JsonArray array -> new Frame.ArrayFrame(array);
            case JsonObject object -> new Frame.ObjectFrame(object);
            default -> new Frame.AtomFrame(json);
        });

        while (!stack.isEmpty()) {
            var result = stack.peek().write(register);
            switch (result) {
                case FrameState.Halt halt -> stack.push(halt.frame());
                case FrameState.End ignored -> stack.pop();
            }
        }

        return register.value;
    }

    private sealed interface FrameState {

        FrameState END = FrameState.End.INSTANCE;

        record Halt(Frame frame) implements FrameState {
        }

        enum End implements FrameState {
            INSTANCE
        }
    }

    private static final class Register {
        Object value;
    }

    private sealed interface Frame {

        FrameState write(Register register);

        record AtomFrame(JsonElement element) implements Frame {

            @Override
            public FrameState write(Register register) {
                register.value = switch (element) {
                    case JsonBoolean jb -> jb == JsonBoolean.TRUE;
                    case JsonDecimal jd -> jd.value();
                    case JsonInteger ji -> ji.value();
                    case JsonNull ignored -> null;
                    case JsonString js -> js.value();
                    default -> throw new IllegalStateException("Unexpected value: " + element);
                };
                return FrameState.END;
            }
        }

        final class ArrayFrame implements Frame {

            private final Iterator<JsonElement> iterator;
            private int state = 0;
            private final List<Object> list;

            public ArrayFrame(JsonArray array) {
                this.iterator = array.iterator();
                this.list = new ArrayList<>(array.length());
            }

            @Override
            public FrameState write(Register register) {
                while (true) {
                    switch (state) {
                        case 0 -> {
                            if (iterator.hasNext()) {
                                state = 1;
                            } else {
                                register.value = this.list;
                                return FrameState.END;
                            }
                        }
                        case 1 -> {
                            var element = iterator.next();
                            state = 2;
                            return switch (element) {
                                case JsonObject jo -> new FrameState.Halt(new Frame.ObjectFrame(jo));
                                case JsonArray ja -> new FrameState.Halt(new Frame.ArrayFrame(ja));
                                default -> new FrameState.Halt(new Frame.AtomFrame(element));
                            };
                        }
                        case 2 -> {
                            list.add(register.value);
                            state = 0;
                        }
                    }
                }
            }
        }

        final class ObjectFrame implements Frame {

            private final Iterator<Map.Entry<String, JsonElement>> iterator;
            private int state = 0;
            private String key;
            private final Map<String, Object> map;

            public ObjectFrame(JsonObject object) {
                this.iterator = object.entrySet().iterator();
                this.map = new HashMap<>(object.size());
            }

            @Override
            public FrameState write(Register register) {
                while (true) {
                    switch (state) {
                        case 0 -> {
                            if (iterator.hasNext()) {
                                state = 1;
                            } else {
                                register.value = this.map;
                                return FrameState.END;
                            }
                        }
                        case 1 -> {
                            var entry = iterator.next();
                            this.key = entry.getKey();
                            var element = entry.getValue();
                            state = 2;
                            return switch (element) {
                                case JsonObject jo -> new FrameState.Halt(new Frame.ObjectFrame(jo));
                                case JsonArray ja -> new FrameState.Halt(new Frame.ArrayFrame(ja));
                                default -> new FrameState.Halt(new Frame.AtomFrame(element));
                            };
                        }
                        case 2 -> {
                            var value = register.value;
                            map.put(this.key, value);
                            state = 0;
                        }
                    }
                }
            }
        }
    }
}
