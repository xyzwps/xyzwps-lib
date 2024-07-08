package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.Counter;
import com.xyzwps.lib.dollar.util.ObjIntFunction;
import com.xyzwps.lib.dollar.util.ObjIntPredicate;
import com.xyzwps.lib.dollar.util.SharedUtils;

import java.util.*;
import java.util.function.*;

/**
 * The Chain interface provides a set of methods for performing chained operations on collections.
 * It is designed to work with any type of items, as specified by the generic parameter T.
 * <p>
 * The interface includes methods for various operations such as:
 * <ul>
 *  <li>chunking the collection into smaller parts</li>
 *  <li>filtering the collection based on a predicate</li>
 *  <li>transforming the collection into another form using a function</li>
 *  <li>reducing the collection to a single value</li>
 *  <li>skipping or taking a certain number of elements</li>
 *  <li>converting the collection to a list or a set</li>
 *  <li>and many more.</li>
 * </ul>
 * <p>
 * Each method in this interface returns a Chain, allowing the operations to be chained together.
 * This design enables a functional programming style, where transformations on the collection
 * can be expressed as a sequence of operations.
 *
 * @param <T> the type of items in the collection
 */
public interface Chain<T> {

    /**
     * Breaks the collection into chunks of the specified size.
     *
     * @param chunkSize the size of each chunk
     * @return a Chain for next stage
     */
    Chain<List<T>> chunk(int chunkSize);

    /**
     * Removes all falsey values from the collection.
     *
     * @return a Chain for next stage
     */
    default Chain<T> compact() {
        return this.filter(it -> !SharedUtils.isFalsey(it));
    }

    /**
     * Concatenates the collection with another iterable.
     *
     * @param iterable the iterable to concatenate
     * @return a Chain for next stage
     */
    Chain<T> concat(Iterable<T> iterable);

    /**
     * Filters the collection based on a predicate.
     *
     * @param predicate the predicate to filter the collection
     * @return a Chain for next stage
     */
    Chain<T> filter(Predicate<T> predicate);

    /**
     * Filters the collection based on a predicate that takes an index as well as the item.
     *
     * @param predicate the predicate to filter the collection
     * @return a Chain for next stage
     */
    Chain<T> filter(ObjIntPredicate<T> predicate);

    /**
     * Finds the first item in the collection.
     *
     * @return an Optional containing the first item that satisfies the predicate, or an empty Optional if no such item is found
     */
    Optional<T> first();

    /**
     * Flattens the collection by applying a function to each item and concatenating the results.
     *
     * @param flatter the function to flatten the collection
     * @param <R>     the type of items in the resulting collection
     * @return a Chain for next stage
     */
    <R> Chain<R> flatMap(Function<T, Iterable<R>> flatter);

    /**
     * Iterates over the collection and applies a consumer to each item.
     *
     * @param consumer the handler function to apply to each item
     */
    void forEach(Consumer<T> consumer);

    /**
     * Iterates over the collection and applies a consumer to each item and its index.
     *
     * @param consumer the handler function to apply to each item
     */
    void forEach(ObjIntConsumer<T> consumer);

    /**
     * Groups the collection by a key function.
     *
     * @param toKey the function to extract the key from each item
     * @param <K>   the type of keys
     * @return a Chain for next stage
     */
    <K> MapEntryChain<K, List<T>> groupBy(Function<T, K> toKey);

    /**
     * Returns the first item in the collection.
     *
     * @return an Optional containing the first item in the collection, or an empty Optional if the collection is empty
     */
    default Optional<T> head() {
        return first();
    }

    /**
     * Get an iterator for the chained collection.
     *
     * @return an iterator for the chained collection
     */
    Iterator<T> iterator();

    /**
     * Joins the items in the collection into a single string, separated by the specified separator.
     *
     * @param sep the separator to join the items. not null
     * @return the joined string
     */
    default String join(String sep) {
        Objects.requireNonNull(sep);
        return this.reduce(new StringJoiner(sep), (it, joiner) -> {
            joiner.add(it == null ? null : it.toString());
            return joiner;
        }).toString();
    }

    /**
     * Returns a map entry chain for the collection, which key is computed by the specified function.
     *
     * @param toKey the function to compute the key
     * @param <K>   the type of keys
     * @return a MapEntryChain for next stage
     */
    <K> MapEntryChain<K, T> keyBy(Function<T, K> toKey);

    /**
     * Maps each item in the collection to another value using a function.
     *
     * @param mapper the function to map each item
     * @param <R>    the type of the resulting items
     * @return a Chain for next stage
     */
    <R> Chain<R> map(Function<T, R> mapper);

    /**
     * Maps each item in the collection to another value using a function that takes an index as well as the item.
     *
     * @param mapper the function to map each item
     * @param <R>    the type of the resulting items
     * @return a Chain for next stage
     */
    <R> Chain<R> map(ObjIntFunction<T, R> mapper);

    /**
     * Orders the collection by a key function.
     *
     * @param toKey     the function to extract the key from each item
     * @param direction the direction to order the collection
     * @param <K>       the type of keys
     * @return a Chain for next stage
     */
    <K extends Comparable<K>> Chain<T> orderBy(Function<T, K> toKey, Direction direction);

    /**
     * Reduces the collection to a single value.
     *
     * @param init    the initial value
     * @param reducer the function to reduce the collection
     * @param <R>     the type of the resulting value
     * @return the reduced value
     */
    <R> R reduce(R init, BiFunction<T, R, R> reducer);

    /**
     * Reverses the collection.
     *
     * @return a Chain for next stage
     */
    Chain<T> reverse();

    /**
     * Counts the number of items in the collection.
     *
     * @return the number of items in the collection
     */
    default int size() {
        var counter = new Counter(0);
        this.forEach(it -> counter.incrAndGet());
        return counter.get();
    }

    /**
     * Skips the first n items in the collection.
     *
     * @param n the number of items to skip
     * @return a Chain for next stage
     */
    Chain<T> skip(int n);

    /**
     * Skips items in the collection until the predicate is false.
     *
     * @param predicate the predicate to skip items
     * @return a Chain for next stage
     */
    Chain<T> skipWhile(Predicate<T> predicate);

    /**
     * Take the first n items in the collection.
     *
     * @param n the number of items to take
     * @return a Chain for next stage
     */
    Chain<T> take(int n);

    /**
     * Take items in the collection until the predicate is false.
     *
     * @param predicate the predicate to take items
     * @return a Chain for next stage
     */
    Chain<T> takeWhile(Predicate<T> predicate);

    /**
     * Converts the collection to a list.
     *
     * @return a list containing the items in the collection
     */
    default ArrayList<T> toList() {
        return this.reduce(new ArrayList<>(), (it, list) -> {
            list.add(it);
            return list;
        });
    }

    /**
     * Converts the collection to a set.
     *
     * @return a set containing the items in the collection
     */
    default HashSet<T> toSet() {
        return this.reduce(new HashSet<>(), (it, list) -> {
            list.add(it);
            return list;
        });
    }

    /**
     * Removes duplicate items from the collection.
     *
     * @return a Chain for next stage
     */
    default Chain<T> unique() {
        var set = new HashSet<T>();
        return this.filter(it -> {
            if (set.contains(it)) return false;
            set.add(it);
            return true;
        });
    }

    /**
     * Removes duplicate items from the collection based on a key function.
     *
     * @param toKey the function to extract the key from each item
     * @param <K>   the type of keys
     * @return a Chain for next stage
     */
    default <K> Chain<T> uniqueBy(Function<T, K> toKey) {
        Objects.requireNonNull(toKey);
        var set = new HashSet<K>();
        return this.filter(it -> {
            var key = toKey.apply(it);
            if (set.contains(key)) return false;
            set.add(key);
            return true;
        });
    }

    /**
     * Zips the collection with another iterable using a zipper function.
     *
     * @param iterable the iterable to zip
     * @param zipper   the function to zip the items
     * @param <R>      the type of the resulting items
     * @param <T2>     the type of items in the other iterable
     * @return a Chain for next stage
     */
    <R, T2> Chain<R> zip(Iterable<T2> iterable, BiFunction<T, T2, R> zipper);

    /**
     * Zips the collection with another iterable into pairs.
     *
     * @param iterable the iterable to zip
     * @param <T2>     the type of items in the other iterable
     * @return a Chain for next stage
     */
    default <T2> Chain<Pair<T, T2>> zip(Iterable<T2> iterable) {
        return this.zip(iterable, Pair::of);
    }
}
