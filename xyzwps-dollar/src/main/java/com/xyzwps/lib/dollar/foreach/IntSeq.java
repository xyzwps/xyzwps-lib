package com.xyzwps.lib.dollar.foreach;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.util.DoubleHolder;
import com.xyzwps.lib.dollar.util.FixedSizeOrderedIntArray;
import com.xyzwps.lib.dollar.util.IntHolder;
import com.xyzwps.lib.dollar.util.Range;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.function.IntPredicate;

public interface IntSeq {
    void forEach(IntConsumer consumer);

    default double avg() {
        var n = new IntHolder(0);
        var avg = new DoubleHolder(0);
        this.forEach(i -> avg.set((avg.get() * n.get() + i) / n.incrAndGet()));
        return avg.get();
    }

    // TODO: chunk

    // TODO: compact

    // TODO: concat

    default IntSeq filter(IntPredicate predicate) {
        Objects.requireNonNull(predicate);
        return (consumer -> this.forEach(it -> {
            if (predicate.test(it)) {
                consumer.accept(it);
            }
        }));
    }

    // TODO: first

    // TODO: flatMap

    // TODO: head

    // TODO: map

    // TODO: mapToObject

    // TODO: mapToOtherPrimitives

    default int max() {
        var holder = new IntHolder(Integer.MIN_VALUE);
        this.forEach(holder::addMax);
        return holder.get();
    }

    default List<Integer> max(int n) {
        if (n < 1) throw new IllegalArgumentException("n should be greater than 0");
        var array = new FixedSizeOrderedIntArray(n, Direction.DESC);
        this.forEach(array::add);
        return array.toList();
    }

    default int min() {
        var holder = new IntHolder(Integer.MAX_VALUE);
        this.forEach(holder::addMin);
        return holder.get();
    }

    default List<Integer> min(int n) {
        if (n < 1) throw new IllegalArgumentException("n should be greater than 0");
        var array = new FixedSizeOrderedIntArray(n, Direction.ASC);
        this.forEach(array::add);
        return array.toList();
    }

    // TODO: orderBy

    // TODO: reduce

    // TODO: reverse

    // TODO: skip

    default int sum() {
        var holder = new IntHolder(0);
        this.forEach(holder::add);
        return holder.get();
    }

    // TODO: take

    default List<Integer> toList() {
        var list = new ArrayList<Integer>();
        this.forEach(list::add);
        return list;
    }

    // TODO: unique

    // TODO: uniqueBy

    // TODO: zip


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
