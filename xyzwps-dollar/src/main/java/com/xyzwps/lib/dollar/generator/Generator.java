package com.xyzwps.lib.dollar.generator;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.util.*;

import java.util.*;
import java.util.function.*;

import static com.xyzwps.lib.dollar.util.Comparators.ascComparator;
import static com.xyzwps.lib.dollar.util.Comparators.descComparator;

public interface Generator<T> {
    NextResult<T> next();

    static <T> Generator<T> fromIterator(Iterator<T> iterator) {
        Objects.requireNonNull(iterator);
        return () -> iterator.hasNext() ? new NextResult.Value<>(iterator.next()) : NextResult.end();
    }

    static <T> Generator<T> fromIterable(Iterable<T> iterable) {
        return fromIterator(iterable.iterator());
    }

    static <T> Generator<T> fromSupplier(Supplier<Iterable<T>> iterableSupplier) {
        if (iterableSupplier == null) return empty();
        return new Generator<>() {
            Iterator<T> itr = null;

            @Override
            public NextResult<T> next() {
                if (this.itr == null) {
                    this.itr = iterableSupplier.get().iterator();
                }
                return itr.hasNext() ? new NextResult.Value<>(itr.next()) : NextResult.end();
            }
        };
    }

    static <T> Generator<T> empty() {
        return NextResult::end;
    }

    static Generator<Integer> infinite(int start) {
        var counter = new Counter(start);
        return () -> new NextResult.Value<>(counter.getAndIncr());
    }

    static <T> Generator<T> fromArray(T[] array) {
        if (array == null || array.length == 0) return empty();

        var index = new Counter(0);
        return () -> {
            var i = index.getAndIncr();
            return i < array.length ? new NextResult.Value<>(array[i]) : NextResult.end();
        };
    }

    default Generator<List<T>> chunk(int n) {
        if (n < 1) throw new IllegalArgumentException();
        return () -> {
            var list = this.takeInto(n, new ArrayList<T>(n), (it, li) -> {
                li.add(it);
                return li;
            });
            if (list.isEmpty()) return NextResult.end();
            return new NextResult.Value<>(list);
        };
    }

    default Generator<T> concat(Iterable<T> iterable) {
        return concat(fromIterable(iterable));
    }

    default Generator<T> concat(Generator<T> generator) {
        return () -> switch (this.next()) {
            case NextResult.End ignored -> generator.next();
            case NextResult.Value<T> value -> value;
        };
    }

    default Generator<T> filter(Predicate<T> predicate) {
        return filter((it, i) -> predicate.test(it));
    }

    default Generator<T> filter(ObjIntPredicate<T> predicate) {
        var counter = new Counter(0);
        return () -> {
            while (true) {
                switch (this.next()) {
                    case NextResult.End ignored -> {
                        return NextResult.end();
                    }
                    case NextResult.Value<T> value -> {
                        if (predicate.test(value.value(), counter.getAndIncr())) {
                            return value;
                        }
                    }
                }
            }
        };
    }

    default Optional<T> first() {
        return switch (this.next()) {
            case NextResult.End ignored -> Optional.empty();
            case NextResult.Value<T> value -> Optional.ofNullable(value.value());
        };
    }

    default <R> Generator<R> flatMap(Function<T, Iterable<R>> fn) {
        return this.flatMapToGenerator((it) -> Generator.fromIterable(fn.apply(it)));
    }

    default <R> Generator<R> flatMapToGenerator(Function<T, Generator<R>> fn) {
        Objects.requireNonNull(fn);
        var holder = new ObjectHolder<Generator<R>>(null);
        return () -> {
            while (true) {
                if (holder.value() == null) {
                    switch (this.next()) {
                        case NextResult.End ignore -> {
                            return NextResult.end();
                        }
                        case NextResult.Value<T> value -> holder.set(fn.apply(value.value()));
                    }
                }

                var holdLazySeq = holder.value();
                switch (holdLazySeq.next()) {
                    case NextResult.Value<R> value -> {
                        return value;
                    }
                    case NextResult.End ignored -> holder.set(null);
                }
            }
        };
    }

    default void forEach(Consumer<T> consumer) {
        this.forEach((it, i) -> consumer.accept(it));
    }

    default void forEach(ObjIntConsumer<T> consumer) {
        var counter = new Counter(0);
        for (var current = this.next(); current instanceof NextResult.Value<T> value; current = this.next()) {
            consumer.accept(value.value(), counter.getAndIncr());
        }
    }

    default Iterator<T> iterator() {
        var self = this;
        return new Iterator<>() {
            private NextResult<T> nextResult = null;

            @Override
            public boolean hasNext() {
                if (nextResult == null) {
                    nextResult = self.next();
                }
                return nextResult instanceof NextResult.Value<T>;
            }

            @Override
            public T next() {
                return switch (nextResult) {
                    case NextResult.End ignored -> throw new NoSuchElementException();
                    case NextResult.Value<T> value -> {
                        nextResult = null;
                        yield value.value();
                    }
                };
            }
        };
    }

    default <R> Generator<R> map(Function<T, R> mapper) {
        return this.map((it, i) -> mapper.apply(it));
    }

    default <R> Generator<R> map(ObjIntFunction<T, R> mapper) {
        var counter = new Counter(0);
        return () -> switch (this.next()) {
            case NextResult.End end -> end;
            case NextResult.Value<T> value -> new NextResult.Value<>(mapper.apply(value.value(), counter.getAndIncr()));
        };
    }

    default <K extends Comparable<K>> Generator<T> orderBy(Function<T, K> toKey, Direction direction) {
        return fromSupplier(() -> {
            ArrayList<T> list = this.toList();
            Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
            list.sort(comparator);
            return list;
        });
    }

    default <R> R reduce(R init, BiFunction<T, R, R> reducer) {
        R result = init;
        for (var current = this.next(); current instanceof NextResult.Value<T> value; current = this.next()) {
            result = reducer.apply(value.value(), result);
        }
        return result;
    }

    default Generator<T> reverse() {
        return fromSupplier(() -> this.toList().reversed());
    }

    default Generator<T> skip(int n) {
        var counter = new Counter(0);
        return () -> {
            if (counter.get() >= n) {
                return this.next();
            }

            while (counter.getAndIncr() < n) {
                switch (this.next()) {
                    case NextResult.End ignored -> {
                        return NextResult.end();
                    }
                    case NextResult.Value<T> ignored -> {
                        // skip, do nothing
                    }
                }
            }
            return this.next();
        };
    }

    default Generator<T> skipWhile(Predicate<T> predicate) {
        var shouldSkip = new BooleanValueHolder(true);
        return () -> {
            if (shouldSkip.value()) {
                while (true) {
                    switch (this.next()) {
                        case NextResult.End ignored -> {
                            shouldSkip.set(false);
                            return NextResult.end();
                        }
                        case NextResult.Value<T> value -> {
                            if (!predicate.test(value.value())) {
                                shouldSkip.set(false);
                                return value;
                            }
                        }
                    }
                }
            }

            return this.next();
        };
    }


    default Generator<T> take(int n) {
        var counter = new Counter(0);
        return () -> counter.getAndIncr() < n ? this.next() : NextResult.end();
    }

    default Generator<T> takeWhile(Predicate<T> predicate) {
        var done = new BooleanValueHolder(false);
        return () -> {
            if (done.value()) return NextResult.end();

            return switch (this.next()) {
                case NextResult.End end -> end;
                case NextResult.Value<T> value -> {
                    if (predicate.test(value.value())) {
                        yield value;
                    } else {
                        done.set(true);
                        yield NextResult.end();
                    }
                }
            };
        };
    }

    default <R> R takeInto(int n, R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        R result = init;
        for (int i = 0; i < n; i++) {
            if (this.next() instanceof NextResult.Value<T> value) {
                result = reducer.apply(value.value(), result);
            } else {
                break;
            }
        }
        return result;
    }

    default ArrayList<T> toList() {
        return this.reduce(new ArrayList<>(), (it, li) -> {
            li.add(it);
            return li;
        });
    }

    // TODO: zip
}
