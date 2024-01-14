package com.xyzwps.lib.dollar.sequence;

import com.xyzwps.lib.dollar.Dollar;
import com.xyzwps.lib.dollar.util.*;

import java.util.*;
import java.util.function.*;

public interface Generator<T> {
    NextResult<T> next();

    static <T> Generator<T> create(Iterator<T> iterator) {
        Objects.requireNonNull(iterator);
        return () -> iterator.hasNext() ? new NextResult.Value<>(iterator.next()) : NextResult.end();
    }

    static <T> Generator<T> create(Iterable<T> iterable) {
        return create(iterable.iterator());
    }

    static <T> Generator<T> empty() {
        return NextResult::end;
    }

    static Generator<Long> infinite(long start) {
        var counter = new LongCounter(start);
        return () -> new NextResult.Value<>(counter.getAndIncr());
    }

    default Generator<List<T>> chunk(int n) {
        if (n < 2) throw new IllegalArgumentException();
        return () -> {
            var list = this.takeInto(n, new ArrayList<T>(n), (it, li) -> {
                li.add(it);
                return li;
            });
            if (list.isEmpty()) return NextResult.end();
            return new NextResult.Value<>(list);
        };
    }

    default Generator<T> compact() {
        return this.filter(Dollar.$::isFalsey);
    }

    default Generator<T> concat(Iterable<T> iterable) {
        return concat(create(iterable));
    }

    default Generator<T> concat(Generator<T> generator) {
        return () -> switch (this.next()) {
            case NextResult.End ignored -> generator.next();
            case NextResult.Value<T> value -> value;
        };
    }

    default Generator<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return filter((it, i) -> predicate.test(it));
    }

    default Generator<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return () -> {
            for (int i = 0; true; i++) {
                switch (this.next()) {
                    case NextResult.End ignored -> {
                        return NextResult.end();
                    }
                    case NextResult.Value<T> value -> {
                        if (predicate.test(value.value(), i)) {
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

    default <R> Generator<R> flatMap(Function<T, Generator<R>> fn) {
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
        Objects.requireNonNull(consumer);
        this.forEach((it, i) -> consumer.accept(it));
    }

    default void forEach(ObjIntConsumer<T> consumer) {
        Objects.requireNonNull(consumer);
        var counter = new Counter(0);
        for (var current = this.next(); current instanceof NextResult.Value<T> value; current = this.next()) {
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
            private NextResult<T> nextResult = self.next();

            @Override
            public boolean hasNext() {
                return nextResult instanceof NextResult.Value<T>;
            }

            @Override
            public T next() {
                return switch (nextResult) {
                    case NextResult.End ignored -> throw new NoSuchElementException();
                    case NextResult.Value<T> value -> {
                        nextResult = self.next();
                        yield value.value();
                    }
                };
            }
        };
    }

    // TODO: keyBy

    default <R> Generator<R> map(Function<T, R> mapper) {
        Objects.requireNonNull(mapper);
        return this.map((it, i) -> mapper.apply(it));
    }

    default <R> Generator<R> map(ObjIntFunction<T, R> mapper) {
        Objects.requireNonNull(mapper);
        var counter = new Counter(0);
        return () -> switch (this.next()) {
            case NextResult.End end -> end;
            case NextResult.Value<T> value -> new NextResult.Value<>(mapper.apply(value.value(), counter.getAndIncr()));
        };
    }

    // TODO: orderBy

    default <R> R reduce(R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        R result = init;
        for (var current = this.next(); current instanceof NextResult.Value<T> value; current = this.next()) {
            result = reducer.apply(value.value(), result);
        }
        return result;
    }

    default Generator<T> reverse() {
        return create(this.toList().reversed());
    }

    default int size() {
        int s = 0;
        for (var current = this.next(); current instanceof NextResult.Value<T>; current = this.next()) {
            s++;
        }
        return s;
    }


    default Generator<T> skip(int n) {
        var counter = new Counter(0);
        return () -> {
            if (counter.get() >= n) {
                return this.next();
            } else {
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
            }
        };
    }

    default Generator<T> take(int n) {
        var counter = new Counter(0);
        return () -> counter.getAndIncr() < n ? this.next() : NextResult.end();
    }

    default <R> R takeInto(int n, R init, BiFunction<T, R, R> reducer) {
        Objects.requireNonNull(reducer);
        R result = init;
        int i = 0;
        for (var current = this.next(); current instanceof NextResult.Value<T> value; current = this.next()) {
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


    default Generator<T> unique() {
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
        var lazy = Generator.create(List.of(1, 2, 3, 4, 5));

        var handled = lazy
                .map(i -> {
                    System.out.printf("=> map: %d + 4 = %d\n", i, i + 4);
                    return i + 4;
                })
                .take(3);
        System.out.println("============");
        handled.forEach(it -> System.out.printf("=> forEach %d \n", it));

        handled.forEach(it -> System.out.printf("=> forEach %d \n", it));

        infinite(0).take(7).forEach(System.out::println);

        infinite(0).map(it -> it * 2).take(7).forEach(System.out::println);
    }

}
