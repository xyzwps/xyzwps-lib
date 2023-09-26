package com.xyzwps.lib.dollar.seq;

import com.xyzwps.lib.dollar.util.DoubleHolder;
import com.xyzwps.lib.dollar.util.IntHolder;
import com.xyzwps.lib.dollar.util.Range;

import java.util.function.IntConsumer;

public interface IntSeq {
    void forEach(IntConsumer consumer);

    // TODO: chunk

    // TODO: compact

    // TODO: concat

    // TODO: filter

    // TODO: first

    // TODO: flatMap

    // TODO: head

    // TODO: map

    // TODO: mapToObject

    // TODO: orderBy

    // TODO: reduce

    // TODO: reverse

    // TODO: skip

    // TODO: take

    // TODO: unique

    // TODO: uniqueBy

    // TODO: zip

    default int sum() {
        var holder = new IntHolder(0);
        this.forEach(holder::add);
        return holder.get();
    }

    default int min() {
        var holder = new IntHolder(Integer.MAX_VALUE);
        this.forEach(holder::addMin);
        return holder.get();
    }

    // TODO: min(n)

    default int max() {
        var holder = new IntHolder(Integer.MIN_VALUE);
        this.forEach(holder::addMax);
        return holder.get();
    }

    // TODO: max(n)

    default double avg() {
        var n = new IntHolder(0);
        var avg = new DoubleHolder(0);
        this.forEach(i -> avg.set((avg.get() * n.get() + i) / n.incrAndGet()));
        return avg.get();
    }

    // ------------ static ------------

    static IntSeq just(int... args) {
        return tConsumer -> {
            if (args == null) return;

            for (int arg : args) {
                tConsumer.accept(arg);
            }
        };
    }

    static IntSeq range(int start, int end) {
        return new Range(start, end)::forEachInt;
    }
}
