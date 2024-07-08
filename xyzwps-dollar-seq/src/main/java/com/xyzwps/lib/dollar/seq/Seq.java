package com.xyzwps.lib.dollar.seq;


import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.dollar.util.*;

import java.util.*;
import java.util.function.*;

import static com.xyzwps.lib.dollar.util.Comparators.*;

/**
 * The sequential handlers of elements.
 *
 * @param <T> seq element type
 */
public interface Seq<T> {

    /**
     * Create a {@link Seq} from an {@link Iterable} producer.
     *
     * @param iterableProducer the iterable producer
     * @param <T>              element type
     * @return the seq
     */
    static <T> Seq<T> create(Supplier<Iterable<T>> iterableProducer) {
        if (iterableProducer == null) return empty();
        return consumer -> {
            for (T t : iterableProducer.get()) {
                consumer.accept(t);
            }
        };
    }

    /**
     * Iterate over the elements in the sequence.
     *
     * @param consumer the consumer function
     */
    void forEach(Consumer<? super T> consumer);

    /**
     * Split the current {@link Seq} into chunks with the specified size.
     *
     * @param chunkSize the size of each chunk
     * @return the chunked {@link Seq}
     */
    default Seq<List<T>> chunk(final int chunkSize) {
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

    /**
     * Filter out the elements which are falsey.
     *
     * @return the compact {@link Seq}
     */
    default Seq<T> compact() {
        return this.filter(t -> !SharedUtils.isFalsey(t));
    }

    /**
     * Concatenate the current {@link Seq} with another {@link Iterable}.
     *
     * @param seq2 the other {@link Iterable}
     * @return the concatenated {@link Seq}
     */
    default Seq<T> concat(Iterable<T> seq2) {
        return tConsumer -> {
            this.forEach(tConsumer);
            seq2.forEach(tConsumer);
        };
    }

    /**
     * 从当前 {@link Seq} 中按顺序筛选满足条件的元素到另个 {@link Seq} 中。
     * <br/>
     * Retain the elements which are satisfied with {@code predicate} into another {@link Seq} in order.
     *
     * @param predicate 被保留的元素应该满足的条件。
     *                  <br/>
     *                  The condition that retaining elements should be satisfied with.
     * @return 筛选结果 {@link Seq}。
     * <br/>Result {@link Seq}.
     */
    default Seq<T> filter(Predicate<T> predicate) {
        return tConsumer -> this.forEach(t -> {
            if (predicate.test(t)) {
                tConsumer.accept(t);
            }
        });
    }

    /**
     * 从当前 {@link Seq} 中按顺序筛选满足条件的元素到另个 {@link Seq} 中。
     * <br/>
     * Retain the elements which are satisfied with {@code predicate} into another {@link Seq} in order.
     *
     * @param predicate 被保留的元素应该满足的条件。第二个参数是元素对应的索引。
     *                  <br/>
     *                  The condition that retaining elements should be satisfied with.
     *                  The second argument is the index of corresponding element.
     * @return 筛选结果 {@link Seq}。
     * <br/>Result {@link Seq}.
     */
    default Seq<T> filter(ObjIntPredicate<T> predicate) {
        var counter = new Counter(0);
        return tConsumer -> this.forEach(t -> {
            if (predicate.test(t, counter.getAndIncr())) {
                tConsumer.accept(t);
            }
        });
    }

    /**
     * Find the first element.
     *
     * @return the first element
     */
    default Optional<T> first() {
        var holder = new ObjectHolder<T>(null);
        StopException.stop(() -> this.forEach(t -> {
            holder.set(t);
            throw StopException.INSTANCE;
        }));
        return Optional.ofNullable(holder.value());
    }

    /**
     * Flat map the elements in the current {@link Seq} to another {@link Seq}.
     *
     * @param flatMapFn the flat map function
     * @param <R>       the type of the flat mapped result
     * @return the flat mapped {@link Seq}
     */
    default <R> Seq<R> flatMap(Function<T, Seq<R>> flatMapFn) {
        return consumer -> this.forEach(t -> {
            Seq<R> rt = flatMapFn.apply(t);
            if (rt != null) {
                rt.forEach(consumer);
            }
        });
    }

    /**
     * Iterate over the elements in the sequence with their indexes.
     *
     * @param handler the handler function
     */
    default void forEach(ObjIntConsumer<? super T> handler) {
        Counter counter = new Counter(0);
        this.forEach(t -> handler.accept(t, counter.getAndIncr()));
    }

    /**
     * Group the elements in the current {@link Seq} by the specified key.
     *
     * @param toKey the key function
     * @param <K>   the type of the key
     * @return the grouped {@link MapEntrySeq}
     */
    default <K> MapEntrySeq<K, List<T>> groupBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Map<K, List<T>> map = new HashMap<>();
        this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> new ArrayList<>()).add(t));
        return map::forEach;
    }

    /**
     * Get the first element.
     *
     * @return the first element
     */
    default Optional<T> head() {
        return this.first();
    }

    /**
     * Join the elements in the current {@link Seq} with the specified separator.
     *
     * @param sep the separator
     * @return the joined string
     */
    default String join(String sep) {
        return this.reduce(new StringJoiner(SharedUtils.defaultTo(sep, "null")), (t, joiner) -> {
            joiner.add(t == null ? null : t.toString());
            return joiner;
        }).toString();
    }

    /**
     * Map a seq into a map entry seq by the specified key function.
     *
     * @param toKey the key function
     * @param <K>   the type of the key
     * @return the last element
     */
    default <K> MapEntrySeq<K, T> keyBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        Map<K, T> map = new HashMap<>();
        this.forEach(t -> map.computeIfAbsent(toKey.apply(t), k -> t));
        return map::forEach;
    }

    /**
     * 把当前 {@link Seq} 中的元素按顺序映射到另一个 {@link Seq} 中。
     * <br/>
     * Mapping elements into another {@link Seq} in order.
     *
     * @param mapFn 映射函数。
     *              <br/>
     *              Mapping function.
     * @param <R>   映射结果的类型。
     *              <br/>
     *              Type of mapping result.
     * @return 映射结果 {@link Seq}。
     * <br/>Mapping result {@link Seq}.
     */
    default <R> Seq<R> map(Function<T, R> mapFn) {
        return rConsumer -> this.forEach(t -> rConsumer.accept(mapFn.apply(t)));
    }

    /**
     * 把当前 {@link Seq} 中的元素按顺序映射到另一个 {@link Seq} 中。
     * <br/>
     * Mapping elements into another {@link Seq} in order.
     *
     * @param mapFn 映射函数。第二个参数是元素对应的索引。
     *              <br/>
     *              Mapping function.
     *              The second argument is the index of corresponding element.
     * @param <R>   映射结果的类型。
     *              <br/>
     *              Type of mapping result.
     * @return 映射结果 {@link Seq}。
     * <br/>Result {@link Seq}.
     */
    default <R> Seq<R> map(ObjIntFunction<T, R> mapFn) {
        Objects.requireNonNull(mapFn);
        Counter counter = new Counter(0);
        return rConsumer -> this.forEach(t -> rConsumer.accept(mapFn.apply(t, counter.getAndIncr())));
    }

    /**
     * Order the elements in the current {@link Seq} by the specified key.
     *
     * @param toKey     the key function
     * @param direction the order direction
     * @param <K>       the type of the key
     * @return the ordered {@link Seq}
     */
    default <K extends Comparable<K>> Seq<T> orderBy(Function<T, K> toKey, Direction direction) {
        return consumer -> {
            ArrayList<T> list = this.toList();
            Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
            list.sort(comparator);
            list.forEach(consumer);
        };
    }

    /**
     * Reduce the elements in the current {@link Seq} to a single value.
     *
     * @param initValue the initial value
     * @param reducer   the reducer function
     * @param <R>       the type of the result
     * @return the reduced result
     */
    default <R> R reduce(R initValue, BiFunction<T, R, R> reducer) {
        var rHolder = new ObjectHolder<>(initValue);
        this.forEach(t -> rHolder.set(reducer.apply(t, rHolder.value())));
        return rHolder.value();
    }

    /**
     * Reverse the elements in the current {@link Seq}.
     *
     * @return the reversed {@link Seq}
     */
    default Seq<T> reverse() {
        return consumer -> {
            ArrayList<T> list = this.toList();
            ArrayListReverseIterator<T> itr = new ArrayListReverseIterator<>(list);
            while (itr.hasNext()) consumer.accept(itr.next());
        };
    }

    /**
     * Skip the first {@code n} elements.
     *
     * @param n the number of elements to skip
     * @return the skipped {@link Seq}
     */
    default Seq<T> skip(int n) {
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

    /**
     * Skip the elements until the condition is false.
     *
     * @param predicate the condition
     * @return the skipped {@link Seq}
     */
    default Seq<T> skipWhile(Predicate<T> predicate) {
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

    /**
     * Take the first {@code n} elements.
     *
     * @param n the number of elements to take
     * @return the taken {@link Seq}
     */
    default Seq<T> take(final int n) {
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

    /**
     * Take the elements until the condition is false.
     *
     * @param predicate the condition
     * @return the taken {@link Seq}
     */
    default Seq<T> takeWhile(Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return StopException.stop(tConsumer -> this.forEach(t -> {
            if (predicate.test(t)) {
                tConsumer.accept(t);
            } else {
                throw StopException.INSTANCE;
            }
        }));
    }

    /**
     * Collect the elements in the current {@link Seq} to a {@link List}.
     *
     * @return the collected {@link List}
     */
    default ArrayList<T> toList() {
        return this.reduce(new ArrayList<>(), (t, list) -> {
            list.add(t);
            return list;
        });
    }

    /**
     * Collect the elements in the current {@link Seq} to a {@link Set}.
     *
     * @return the collected {@link Set}
     */
    default HashSet<T> toSet() {
        return this.reduce(new HashSet<>(), (t, set) -> {
            set.add(t);
            return set;
        });
    }

    /**
     * Duplicate the elements in the current {@link Seq}.
     *
     * @return the duplicated {@link Seq}
     */
    default Seq<T> unique() {
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

    /**
     * Duplicate the elements in the current {@link Seq} by the specified key.
     *
     * @param toKey the key function
     * @param <K>   the type of the key
     * @return the duplicated {@link Seq}
     */
    default <K> Seq<T> uniqueBy(Function<T, K> toKey) {
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

    /**
     * The alias of {@link #toList()}.
     *
     * @return the collected {@link List}
     */
    default ArrayList<T> value() {
        return this.toList();
    }

    /**
     * Zip the elements in the current {@link Seq} with another {@link Iterable}.
     *
     * @param iterable the other {@link Iterable}
     * @param zipper   the zipper function
     * @param <R>      the type of the result
     * @param <T2>     the type of the other {@link Iterable}
     * @return the zipped {@link Seq}
     */
    default <R, T2> Seq<R> zip(Iterable<T2> iterable, BiFunction<T, T2, R> zipper) {
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

    /**
     * Zip the elements in the current {@link Seq} with another {@link Iterable}.
     *
     * @param iterable the other {@link Iterable}
     * @param <T2>     the type of the other {@link Iterable}
     * @return the zipped {@link Seq}
     */
    default <T2> Seq<Pair<T, T2>> zip(Iterable<T2> iterable) {
        return this.zip(iterable, Pair::of);
    }


    // ------------ static ------------

    /**
     * Create an empty {@link Seq}.
     *
     * @param <T> the type of elements
     * @return the empty {@link Seq}
     */
    static <T> Seq<T> empty() {
        return Functions::consumeNothing;
    }

    /**
     * Create a {@link Seq} from an {@link Iterable}.
     *
     * @param list the {@link Iterable}
     * @param <T>  the type of elements
     * @return the {@link Seq}
     */
    static <T> Seq<T> from(Iterable<T> list) {
        return list == null ? Seq.empty() : list::forEach;
    }

    /**
     * Create a {@link Seq} from a list of elements.
     *
     * @param args the elements
     * @param <T>  the type of elements
     * @return the {@link Seq}
     */
    @SafeVarargs
    static <T> Seq<T> just(T... args) {
        return tConsumer -> {
            for (T t : args) {
                tConsumer.accept(t);
            }
        };
    }

    /**
     * Create an infinite {@link Seq} with the specified start value.
     *
     * @param start the start value
     * @return the infinite {@link Seq}
     */
    @SuppressWarnings("InfiniteLoopStatement")
    static Seq<Integer> infinite(int start) {
        var counter = new Counter(start);
        return consumer -> {
            while (true) {
                consumer.accept(counter.getAndIncr());
            }
        };
    }

    /**
     * Create a {@link Seq} from a range of integers.
     *
     * @param start the start value
     * @param end   the end value
     * @return the {@link Seq}
     */
    static Seq<Integer> range(int start, int end) {
        return from(new RangeIterable(start, end));
    }
}
