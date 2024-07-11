package com.xyzwps.lib.dollar.seq;


class StopException extends RuntimeException {

    private StopException() {
        super("", null, false, false);
    }

    static final StopException INSTANCE = new StopException();

    static <T> Seq<T> stop(Seq<T> seq) {
        return tConsumer -> {
            try {
                seq.forEach(tConsumer);
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
