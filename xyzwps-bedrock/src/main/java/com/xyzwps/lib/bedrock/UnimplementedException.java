package com.xyzwps.lib.bedrock;

public final class UnimplementedException extends RuntimeException {

    private UnimplementedException(String message) {
        super(message);
    }

    public static UnimplementedException todo() {
        return new UnimplementedException("todo");
    }

    public static UnimplementedException fixme() {
        return new UnimplementedException("fixme");
    }

    public static UnimplementedException wontDo() {
        return new UnimplementedException("wont do");
    }
}
