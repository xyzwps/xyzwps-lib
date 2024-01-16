package com.xyzwps.lib.dollar.seq;


import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.util.*;

import java.util.*;
import java.util.function.*;

import static com.xyzwps.lib.dollar.Dollar.*;
import static com.xyzwps.lib.dollar.util.Comparators.*;

/**
 * The sequential handlers of elements.
 *
 * @param <T>
 */
public interface Traversable<T> {

    void forEach(Consumer<? super T> consumer);

    default Traversable<List<T>> chunk(final int chunkSize) {
        if (chunkSize < 1) {
            throw new IllegalArgumentException("Each chunk should have at least one element.");
        }

        var listHolder = new ObjectHolder<List<T>>(new ArrayList<>());
        var counter = new Counter(0);
        return listConsumer -> {
            this.forEach(t -> {
                listHolder.value().add(t);
                if (counter.incrAndGet() >= chunkSize) {
                    listConsumer.accept(listHolder.value());
                    listHolder.set(new ArrayList<>());
                    counter.reset();
                }
            });
            if (counter.get() > 0) {
                listConsumer.accept(listHolder.value());
            }
        };
    }

    default Traversable<T> compact() {
        return this.filter(t -> !$.isFalsey(t));
    }

    default Traversable<T> concat(Iterable<T> seq2) {
        if (seq2 == null) return this;

        return tConsumer -> {
            this.forEach(tConsumer);
            seq2.forEach(tConsumer);
        };
    }

    // TODO: diff

    /**
     * 从当前 {@link Traversable} 中按顺序筛选满足条件的元素到另个 {@link Traversable} 中。
     * <br/>
     * Retain the elements which are satisfied with {@code predicate} into another {@link Traversable} in order.
     *
     * @param predicate 被保留的元素应该满足的条件。
     *                  <br/>
     *                  The condition that retaining elements should be satisfied with.
     * @return 筛选结果 {@link Traversable}。
     * <br/>Result {@link Traversable}.
     */
    default Traversable<T> filter(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return tConsumer -> this.forEach(t -> {
            if (predicate.test(t)) {
                tConsumer.accept(t);
            }
        });
    }

    /**
     * 从当前 {@link Traversable} 中按顺序筛选满足条件的元素到另个 {@link Traversable} 中。
     * <br/>
     * Retain the elements which are satisfied with {@code predicate} into another {@link Traversable} in order.
     *
     * @param predicate 被保留的元素应该满足的条件。第二个参数是元素对应的索引。
     *                  <br/>
     *                  The condition that retaining elements should be satisfied with.
     *                  The second argument is the index of corresponding element.
     * @return 筛选结果 {@link Traversable}。
     * <br/>Result {@link Traversable}.
     */
    default Traversable<T> filter(ObjIntPredicate<T> predicate) {
        Objects.requireNonNull(predicate);
        var counter = new Counter(0);
        return tConsumer -> this.forEach(t -> {
            if (predicate.test(t, counter.getAndIncr())) {
                tConsumer.accept(t);
            }
        });
    }

    default Optional<T> first() {
        var holder = new ObjectHolder<T>(null);
        StopException.stop(() -> this.forEach(t -> {
            holder.set(t);
            throw StopException.INSTANCE;
        }));
        return Optional.ofNullable(holder.value());
    }

    default <R> Traversable<R> flatMap(Function<T, Traversable<R>> flatMapFn) {
        return consumer -> this.forEach(t -> {
            Traversable<R> rt = flatMapFn.apply(t);
            if (rt != null) {
                rt.forEach(consumer);
            }
        });
    }

    default void forEach(ObjIntConsumer<? super T> handler) {
        Objects.requireNonNull(handler);
        Counter counter = new Counter(0);
        this.forEach(t -> handler.accept(t, counter.getAndIncr()));
    }

    default <K> MapEntryTraversable<K, List<T>> groupBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Map<K, List<T>> map = new HashMap<>();
        this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> new ArrayList<>()).add(t));
        return map::forEach;
    }

    default Optional<T> head() {
        return this.first();
    }

    default String join(String sep) {
        return this.reduce(new StringJoiner($.defaultTo(sep, "null")), (joiner, t) -> {
            joiner.add(t == null ? null : t.toString());
            return joiner;
        }).toString();
    }

    default <K> MapEntryTraversable<K, T> keyBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Map<K, T> map = new HashMap<>();
        this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> t));
        return map::forEach;
    }

    /**
     * 把当前 {@link Traversable} 中的元素按顺序映射到另一个 {@link Traversable} 中。
     * <br/>
     * Mapping elements into another {@link Traversable} in order.
     *
     * @param mapFn 映射函数。
     *              <br/>
     *              Mapping function.
     * @param <R>   映射结果的类型。
     *              <br/>
     *              Type of mapping result.
     * @return 映射结果 {@link Traversable}。
     * <br/>Mapping result {@link Traversable}.
     */
    default <R> Traversable<R> map(Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        return rConsumer -> this.forEach(t -> rConsumer.accept(mapFn.apply(t)));
    }

    default IntSeq mapToInt(ToIntFunction<T> mapFn) {
        Objects.requireNonNull(mapFn);
        return consumer -> this.forEach(t -> consumer.accept(mapFn.applyAsInt(t)));
    }

    /**
     * 把当前 {@link Traversable} 中的元素按顺序映射到另一个 {@link Traversable} 中。
     * <br/>
     * Mapping elements into another {@link Traversable} in order.
     *
     * @param mapFn 映射函数。第二个参数是元素对应的索引。
     *              <br/>
     *              Mapping function.
     *              The second argument is the index of corresponding element.
     * @param <R>   映射结果的类型。
     *              <br/>
     *              Type of mapping result.
     * @return 映射结果 {@link Traversable}。
     * <br/>Result {@link Traversable}.
     */
    default <R> Traversable<R> map(ObjIntFunction<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        Counter counter = new Counter(0);
        return rConsumer -> this.forEach(t -> rConsumer.accept(mapFn.apply(t, counter.getAndIncr())));
    }

    default <K extends Comparable<K>> Traversable<T> orderBy(Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);
        ArrayList<T> list = this.toList();
        Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
        list.sort(comparator);
        return list::forEach;
    }

    default <R> R reduce(R initValue, BiFunction<R, T, R> reducer) {
        var rHolder = new ObjectHolder<>(initValue);
        this.forEach(t -> rHolder.set(reducer.apply(rHolder.value(), t)));
        return rHolder.value();
    }

    default Traversable<T> reverse() {
        ArrayList<T> list = this.toList();
        ArrayListReverseIterator<T> itr = new ArrayListReverseIterator<>(list);
        return tConsumer -> {
            while (itr.hasNext()) tConsumer.accept(itr.next());
        };
    }

    default Traversable<T> skip(int n) {
        return tConsumer -> {
            int[] counter = {0};
            this.forEach(t -> {
                if (counter[0] < n) {
                    counter[0]++;
                } else {
                    tConsumer.accept(t);
                }
            });
        };
    }

    default Traversable<T> skipWhile(Predicate<T> predicate) {
        return tConsumer -> {
            boolean[] next = {true};
            this.forEach(t -> {
                next[0] = next[0] && predicate.test(t);
                if (!next[0]) {
                    tConsumer.accept(t);
                }
            });
        };
    }

    default Traversable<T> take(final int n) {
        if (n < 1) throw new IllegalArgumentException();


        return StopException.stop(tConsumer -> {
            Counter counter = new Counter(0);
            this.forEach(t -> {
                if (counter.getAndIncr() < n) {
                    tConsumer.accept(t);
                }

                if (counter.get() >= n) {
                    throw StopException.INSTANCE;
                }
            });
        });
    }

    default Traversable<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return StopException.stop(tConsumer -> this.forEach(t -> {
            if (predicate.test(t)) {
                tConsumer.accept(t);
            } else {
                throw StopException.INSTANCE;
            }
        }));
    }

    default ArrayList<T> toList() {
        return this.reduce(new ArrayList<>(), (list, t) -> {
            list.add(t);
            return list;
        });
    }

    default HashSet<T> toSet() {
        return this.reduce(new HashSet<>(), (set, t) -> {
            set.add(t);
            return set;
        });
    }

    default Traversable<T> unique() {
        Set<T> set = new HashSet<>();
        return tConsumer -> {
            this.forEach(t -> {
                if (set.contains(t)) {
                    return;
                }

                set.add(t);
                tConsumer.accept(t);
            });
        };
    }

    default <K> Traversable<T> uniqueBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Set<K> set = new HashSet<>();
        return tConsumer -> {
            this.forEach(t -> {
                K key = toKey.apply(t);
                if (set.contains(key)) {
                    return;
                }

                set.add(key);
                tConsumer.accept(t);
            });
        };
    }

    default ArrayList<T> value() {
        return this.toList();
    }


    default <R, T2> Traversable<R> zip(Iterable<T2> iterable, BiFunction<T, T2, R> zipper) {
        Objects.requireNonNull(zipper);
        if (iterable == null) {
            return this.map(t -> zipper.apply(t, null));
        }

        Iterator<T2> itr = iterable.iterator();
        return rConsumer -> {
            this.forEach(t -> rConsumer.accept(zipper.apply(t, itr.hasNext() ? itr.next() : null)));
            while (itr.hasNext()) {
                rConsumer.accept(zipper.apply(null, itr.next()));
            }
        };
    }

    default <T2> Traversable<Pair<T, T2>> zip(Iterable<T2> iterable) {
        return this.zip(iterable, Pair::of);
    }


    // ------------ static ------------

    static <T> Traversable<T> empty() {
        return Functions::consumeNothing;
    }

    static <T> Traversable<T> from(Iterable<T> list) {
        return list == null ? Traversable.empty() : list::forEach;
    }

    @SafeVarargs
    static <T> Traversable<T> just(T... args) {
        return tConsumer -> {
            for (T t : args) {
                tConsumer.accept(t);
            }
        };
    }

    @SuppressWarnings("InfiniteLoopStatement")
    static Traversable<Long> infinite(long start) {
        var counter = new LongCounter(start);
        return consumer -> {
            while (true) {
                consumer.accept(counter.getAndIncr());
            }
        };
    }

    static Traversable<Integer> range(int start, int end) {
        return from(new Range(start, end));
    }
}
