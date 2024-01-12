package com.xyzwps.lib.dollar.sequence;

import com.xyzwps.lib.dollar.Dollar;
import com.xyzwps.lib.dollar.util.Counter;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;
import com.xyzwps.lib.dollar.util.ObjectHolder;

import java.util.*;
import java.util.function.*;

public interface LazySequence<T> {
    Current<T> popCurrent();

    static <T> LazySequence<T> create(Sequence<T> sequence) {
        Objects.requireNonNull(sequence);
        return new LazySequence<>() {
            private Sequence<T> current = sequence;

            @Override
            public Current<T> popCurrent() {
                return switch (current) {
                    case Sequence.Nil ignored -> Current.end();
                    case Sequence.Cons<T> cons -> {
                        var value = new Current.Value<>(cons.value());
                        current = cons.rest();
                        yield value;
                    }
                };
            }
        };
    }

    static <T> LazySequence<T> empty() {
        return Current::end;
    }

    default LazySequence<List<T>> chunk(int n) {
        if (n < 2) throw new IllegalArgumentException();
        return () -> {
            var list = this.takeInto(n, new ArrayList<T>(n), (it, li) -> {
                li.add(it);
                return li;
            });
            if (list.isEmpty()) return Current.end();
            return new Current.Value<>(list);
        };
    }

    default LazySequence<T> compact() {
        return this.filter(Dollar.$::isFalsey);
    }

    default LazySequence<T> concat(Sequence<T> sequence) {
        var second = create(sequence);
        return () -> switch (this.popCurrent()) {
            case Current.End ignored -> second.popCurrent();
            case Current.Value<T> value -> value;
        };
    }

    default LazySequence<T> concat(Iterable<T> iterable) {
        return concat(Sequence.from(iterable));
    }

    default LazySequence<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return filter((it, i) -> predicate.test(it));
    }

    default LazySequence<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return () -> {
            for (int i = 0; true; i++) {
                switch (this.popCurrent()) {
                    case Current.End ignored -> {
                        return Current.end();
                    }
                    case Current.Value<T> value -> {
                        if (predicate.test(value.value(), i)) {
                            return value;
                        }
                    }
                }
            }
        };
    }

    default Optional<T> first() {
        return switch (this.popCurrent()) {
            case Current.End ignored -> Optional.empty();
            case Current.Value<T> value -> Optional.ofNullable(value.value());
        };
    }

    default <R> LazySequence<R> flatMap(Function<T, Sequence<R>> fn) {
        Objects.requireNonNull(fn);
        var holder = new ObjectHolder<LazySequence<R>>(null);
        return () -> {
            while (true) {
                if (holder.value() == null) {
                    switch (this.popCurrent()) {
                        case Current.End ignore -> {
                            return Current.end();
                        }
                        case Current.Value<T> value -> holder.set(create(fn.apply(value.value())));
                    }
                }

                var holdLazySeq = holder.value();
                switch (holdLazySeq.popCurrent()) {
                    case Current.Value<R> value -> {
                        return value;
                    }
                    case Current.End ignored -> holder.set(null);
                }
            }
        };
    }

    default void forEach(Consumer<T> consumer) {
        Objects.requireNonNull(consumer);
        this.forEach((it, i) -> consumer.accept(it));
    }

    default void forEach(ObjIntConsumer<T> consumer) {
        Objects.requireNonNull(consumer);
        var counter = new Counter(0);
        for (var current = this.popCurrent(); current instanceof Current.Value<T> value; current = this.popCurrent()) {
            consumer.accept(value.value(), counter.getAndIncr());
        }
    }

    // TODO: groupBy

    default Optional<T> head() {
        return first();
    }

    default Iterator<T> iterator() {
        var self = this;
        return new Iterator<T>() {
            private Current<T> current = self.popCurrent();

            @Override
            public boolean hasNext() {
                return current instanceof Current.Value<T>;
            }

            @Override
            public T next() {
                return switch (current) {
                    case Current.End ignored -> throw new NoSuchElementException();
                    case Current.Value<T> value -> {
                        current = self.popCurrent();
                        yield value.value();
                    }
                };
            }
        };
    }

    // TODO: keyBy

    default <R> LazySequence<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return this.map((it, i) -> mapper.apply(it));
    }

    default <R> LazySequence<R> map(ObjIntFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        var counter = new Counter(0);
        return () -> switch (this.popCurrent()) {
            case Current.End end -> end;
            case Current.Value<T> value -> new Current.Value<>(mapper.apply(value.value(), counter.getAndIncr()));
        };
    }

    // TODO: orderBy

    default <R> R reduce(R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        R result = init;
        for (var current = this.popCurrent(); current instanceof Current.Value<T> value; current = this.popCurrent()) {
            result = reducer.apply(value.value(), result);
        }
        return result;
    }

    default LazySequence<T> reverse() {
        var list = this.toList();
        return create(Sequence.from(list.reversed()));
    }

    default int size() {
        int s = 0;
        for (var current = this.popCurrent(); current instanceof Current.Value<T>; current = this.popCurrent()) {
            s++;
        }
        return s;
    }


    default LazySequence<T> skip(int n) {
        var counter = new Counter(0);
        return () -> {
            if (counter.get() >= n) {
                return this.popCurrent();
            } else {
                while (counter.getAndIncr() < n) {
                    switch (this.popCurrent()) {
                        case Current.End ignored -> {
                            return Current.end();
                        }
                        case Current.Value<T> ignored -> {
                            // skip, do nothing
                        }
                    }
                }
                return this.popCurrent();
            }
        };
    }

    default LazySequence<T> take(int n) {
        var counter = new Counter(0);
        return () -> counter.getAndIncr() < n ? this.popCurrent() : Current.end();
    }

    default <R> R takeInto(int n, R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        R result = init;
        int i = 0;
        for (var current = this.popCurrent(); current instanceof Current.Value<T> value; current = this.popCurrent()) {
            if (i < n) {
                result = reducer.apply(value.value(), result);
                i++;
            } else {
                break;
            }
        }
        return result;
    }

    default List<T> toList() {
        return this.reduce(new ArrayList<>(), (it, li) -> {
            li.add(it);
            return li;
        });
    }

    default Set<T> toSet() {
        return this.reduce(new HashSet<>(), (it, li) -> {
            li.add(it);
            return li;
        });
    }


    default LazySequence<T> unique() {
        var dedupSet = new HashSet<T>();
        return this.filter(it -> {
            if (dedupSet.contains(it)) {
                return false;
            } else {
                dedupSet.add(it);
                return true;
            }
        });
    }

    // TODO: zip

    static void main(String[] args) {
        var seq = new Sequence.Cons<>(1, new Sequence.Cons<>(2, new Sequence.Cons<>(3, new Sequence.Cons<>(4, Sequence.nil()))));
        var lazy = LazySequence.create(seq);

        var handled = lazy
                .map(i -> {
                    System.out.printf("=> map: %d + 4 = %d\n", i, i + 4);
                    return i + 4;
                })
                .take(3);
        System.out.println("============");
        handled.forEach(it -> System.out.printf("=> forEach %d \n", it));

        handled.forEach(it -> System.out.printf("=> forEach %d \n", it));

        LazySequence.create(Sequence.infinite(0)).take(7).forEach(System.out::println);

        LazySequence.create(Sequence.infinite(0)).map(it -> it * 2).take(7).forEach(System.out::println);
    }

}
