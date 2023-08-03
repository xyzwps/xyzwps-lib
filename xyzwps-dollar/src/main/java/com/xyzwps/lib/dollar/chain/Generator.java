package com.xyzwps.lib.dollar.chain;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;
import com.xyzwps.lib.dollar.util.BooleanValueHolder;
import com.xyzwps.lib.dollar.util.Counter;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.util.Comparators.*;

// TODO: 更严格的测试
public interface Generator<T> {

    Result<T> next();

    default <R> Generator<R> map(Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        return () -> this.next() instanceof Result.Value<T> v
                ? Result.value(mapFn.apply(v.value()))
                : Result.done();
    }

    default <R> Generator<R> map(ObjIntFunction<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        var counter = new Counter(0);
        return () -> this.next() instanceof Result.Value<T> v
                ? Result.value(mapFn.apply(v.value(), counter.getAndIncr()))
                : Result.done();
    }

    default Generator<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return () -> {
            while (true) {
                if (this.next() instanceof Result.Value<T> v) {
                    if (predicate.test(v.value())) {
                        return v;
                    }
                } else {
                    return Result.done();
                }
            }
        };
    }

    default Generator<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        var counter = new Counter(0);
        return () -> {
            while (true) {
                if (this.next() instanceof Result.Value<T> v) {
                    if (predicate.test(v.value(), counter.getAndIncr())) {
                        return v;
                    }
                } else {
                    return Result.done();
                }
            }
        };
    }

    default Generator<List<T>> chunk(final int chunkSize) {
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Each chunk should have at least one element.");
        }

        return () -> {
            List<T> list = new ArrayList<>(chunkSize);
            for (int i = 0; i < chunkSize; i++) {
                if (this.next() instanceof Result.Value<T> v) {
                    list.add(v.value());
                } else {
                    break;
                }
            }
            return list.isEmpty() ? Result.done() : Result.value(list);
        };
    }

    default Generator<T> concat(Generator<T> gen2) {
        return gen2 == null ? this
                : () -> this.next() instanceof Result.Value<T> v ? v : gen2.next();
    }

    default Generator<T> take(final int n) {
        if (n < 1) {
            throw new IllegalArgumentException("You should take at least one element.");
        }

        var counter = new Counter(0);
        return () -> counter.getAndIncr() < n ? this.next() : Result.done();
    }

    default Generator<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        var end = new BooleanValueHolder(false);
        return () -> {
            if (end.value()) {
                return Result.done();
            }

            if (this.next() instanceof Result.Value<T> v) {
                if (predicate.test(v.value())) {
                    return v;
                }
                end.set(true);
            }
            return Result.done();
        };
    }

    default Generator<T> skip(int n) {
        var counter = new Counter(0);
        return () -> {
            while (counter.getAndIncr() < n) {
                if (this.next() instanceof Result.Done<T> d) {
                    return d;
                }
            }
            return this.next();
        };
    }

    default Generator<T> skipWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        var end = new BooleanValueHolder(false);
        return () -> {
            if (end.value()) {
                return this.next();
            }

            while (true) {
                if (this.next() instanceof Result.Value<T> v) {
                    if (!predicate.test(v.value())) {
                        end.set(true);
                        return v;
                    }
                } else {
                    return Result.done();
                }
            }
        };
    }

    default <R> R reduce(R initValue, BiFunction<R, T, R> reducer) {
        Objects.requireNonNull(reducer);

        R result = initValue;
        while (this.next() instanceof Result.Value<T> v) {
            result = reducer.apply(result, v.value());
        }
        return result;
    }

    default Generator<T> reverse() {
        var list = this.reduce(new LinkedList<T>(), (li, v) -> {
            li.add(v);
            return li;
        });
        return () -> list.isEmpty() ? Result.done() : Result.value(list.pollLast());
    }

    default <K extends Comparable<K>> Generator<T> orderBy(Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);
        var list = this.reduce(new ArrayList<T>(), (li, v) -> {
            li.add(v);
            return li;
        });
        Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
        list.sort(comparator);
        return Generator.fromIterator(list.iterator());
    }

    default <R, T2> Generator<R> zip(Generator<T2> t2Gen, BiFunction<T, T2, R> zipper) {
        Objects.requireNonNull(zipper);
        if (t2Gen == null) {
            return this.map(t -> zipper.apply(t, null));
        }

        return () -> {
            if (this.next() instanceof Result.Value<T> t1) {
                return t2Gen.next() instanceof Result.Value<T2> t2
                        ? Result.value(zipper.apply(t1.value(), t2.value()))
                        : Result.value(zipper.apply(t1.value(), null));
            } else {
                return t2Gen.next() instanceof Result.Value<T2> t2
                        ? Result.value(zipper.apply(null, t2.value()))
                        : Result.done();
            }
        };
    }

    // TODO: group by

    // TODO: key by

    default <K> Generator<T> uniqueBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        var dedupSet = new HashSet<K>();
        return () -> {
            while (this.next() instanceof Result.Value<T> v) {
                var key = toKey.apply(v.value());
                if (!dedupSet.contains(key)) {
                    dedupSet.add(key);
                    return v;
                }
            }
            return Result.done();
        };
    }

    // TODO: unique


    default <R> Generator<R> flatMap(Function<T, Iterable<R>> flatMapFn) {
        Objects.requireNonNull(flatMapFn);
        final Generator<T> generator = this;
        return new Generator<>() {
            private Iterator<R> itr = null;

            @Override
            public Result<R> next() {
                while (true) {
                    if (this.itr == null) {
                        if (generator.next() instanceof Result.Value<T> v) {
                            this.itr = flatMapFn.apply(v.value()).iterator();
                        } else {
                            return Result.done();
                        }
                    }

                    if (itr.hasNext()) {
                        return Result.value(itr.next());
                    } else {
                        this.itr = null;
                    }
                }
            }
        };
    }


    default Iterator<T> toIterator() {
        final Generator<T> generator = this;

        return new Iterator<>() {
            private final IteratorHolder<T> holder = new IteratorHolder<>() {
                @Override
                void tryToGetNext() {
                    if (this.hasNext) {
                        return;
                    }

                    if (generator.next() instanceof Result.Value<T> v) {
                        holder.addNext(v.value());
                    } else {
                        holder.clear();
                    }
                }
            };

            @Override
            public boolean hasNext() {
                holder.tryToGetNext();
                return holder.hasNext;
            }

            @Override
            public T next() {
                holder.tryToGetNext();
                // TODO: no such element exception
                return holder.getNextAndClear();
            }
        };
    }

    static <T> Generator<T> fromIterator(Iterator<T> itr) {
        if (itr == null) {
            return empty();
        }

        return () -> itr.hasNext() ? Result.value(itr.next()) : Result.done();
    }

    static <T> Generator<T> fromArray(T[] arr) {
        if (arr == null || arr.length == 0) {
            return empty();
        }

        var len = arr.length;
        var counter = new Counter(0);
        return () -> {
            var index = counter.getAndIncr();
            return index < len ? Result.value(arr[index]) : Result.done();
        };
    }

    static <E> Generator<E> empty() {
        return Result::done;
    }
}
