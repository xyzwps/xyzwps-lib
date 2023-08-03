package com.xyzwps.lib.dollar.sub;

import java.util.ArrayList;
import java.util.List;

public class Subscription {

    private final List<Runnable> teardowns = new ArrayList<>();

    public void add(Runnable teardown) {
        this.teardowns.add(teardown); // TODO: 线程安全
    }

    public void unsubscribe() {
        for (var teardown : teardowns) {
            if (teardown != null) {
                teardown.run();
            }
        }
        this.teardowns.clear();
    }
}
