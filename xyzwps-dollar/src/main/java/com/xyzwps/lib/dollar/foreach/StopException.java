package com.xyzwps.lib.dollar.foreach;


class StopException extends RuntimeException {

    private StopException() {
        super("", null, false, false);
    }

    static final StopException INSTANCE = new StopException();

    static <T> Traversable<T> stop(Traversable<T> traversable) {
        return tConsumer -> {
            try {
                traversable.forEach(tConsumer);
            } catch (StopException ignored) {
            }
        };
    }

    static void stop(Runnable runnable) {
        try {
            runnable.run();
        } catch (StopException ignored) {
        }
    }
}
