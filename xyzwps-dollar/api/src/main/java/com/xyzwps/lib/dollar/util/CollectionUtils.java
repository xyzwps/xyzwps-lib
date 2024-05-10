package com.xyzwps.lib.dollar.util;

import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import static com.xyzwps.lib.dollar.util.ObjectUtils.*;

public final class CollectionUtils {

    /**
     * Creates a list of elements split into groups the length of size.
     * If list can't be split evenly, the final chunk will be the remaining elements.
     *
     * @param list The list to handle
     * @param size Chunk size which should be greater than 0.
     * @param <T>  Element type
     * @return new list of chunks
     */
    public static <T> List<List<T>> chunk(List<T> list, int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Chunk size should be greater than 0.");
        }

        if (isEmpty(list)) {
            return new ArrayList<>();
        }

        int listSize = list.size();
        int chunksCapacity = listSize / size + 1;
        List<List<T>> chunks = new ArrayList<>(chunksCapacity);
        List<T> chunk = null;
        int counter = 0;
        int i = 0;
        for (T element : list) {
            if (counter == 0) {
                chunk = new ArrayList<>(size);
            }

            chunk.add(element);
            counter++;
            i++;

            if (counter == size || i == listSize) {
                chunks.add(chunk);
                chunk = null;
                counter = 0;
            }
        }

        return chunks;
    }

    /**
     * Filter list with the elements which are not falsey.
     * <p>
     * The definition of falsey can be seen at {@link ObjectUtils#isFalsey}
     *
     * @param list The list to filter. Null is acceptable.
     * @param <T>  List element type
     * @return new compacted list
     * @see ObjectUtils#isFalsey(Object)
     */
    public static <T> List<T> compact(List<T> list) {
        return filter(list, it -> !isFalsey(it));
    }

    /**
     * Creates a new list which concatenating all lists in order.
     *
     * @param lists The lists to concatenate
     * @param <T>   Element type
     * @return concatenated new list
     */
    @SafeVarargs
    public static <T> List<T> concat(List<T>... lists) {
        if (lists.length == 0) {
            return new ArrayList<>();
        }

        int capacity = 0;
        for (List<T> list : lists) {
            if (isNotEmpty(list)) {
                capacity += list.size();
            }
        }

        if (capacity == 0) {
            return new ArrayList<>();
        }

        ArrayList<T> result = new ArrayList<>(capacity);
        for (List<T> list : lists) {
            if (isNotEmpty(list)) {
                result.addAll(list);
            }
        }
        return result;
    }

    /**
     * Iterate over the list and retaining the elements which are predicated true.
     *
     * @param list      The list to iterate. Null is acceptable.
     * @param predicate Predicate function. Cannot be null.
     * @param <T>       Element type
     * @return new filtered list
     */
    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        Objects.requireNonNull(predicate);
        return filter(list, (e, i) -> predicate.test(e));
    }


    /**
     * Iterate over the list and retaining the elements which are predicated true.
     *
     * @param list      The list to iterate. Null is acceptable.
     * @param predicate Predicate function with element index. Cannot be null.
     * @param <T>       Element type
     * @return new filtered list
     */
    public static <T> List<T> filter(List<T> list, BiPredicate<T, Integer> predicate) {
        Objects.requireNonNull(predicate);
        if (list == null) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        int i = 0;
        for (T element : list) {
            if (predicate.test(element, i++)) {
                result.add(element);
            }
        }
        return result;
    }

    /**
     * Get the first element from {@link Iterable}.
     * <p>
     * Warning: When {@link Optional#empty()} is returned, uou cannot recognize
     * that the <code>iterable</code> is empty, or it's first element is null.
     *
     * @param iterable to be handled
     * @param <T>      type of the first element
     * @return {@link Optional} of the first element
     */
    public static <T> Optional<T> first(Iterable<T> iterable) {
        if (iterable == null) {
            return Optional.empty();
        }
        if (iterable instanceof List<T> list) {
            return list.isEmpty() ? Optional.empty() : Optional.ofNullable(list.getFirst());
        }
        Iterator<T> itr = iterable.iterator();
        return itr.hasNext() ? Optional.ofNullable(itr.next()) : Optional.empty();
    }

    /**
     * Check if a {@link Collection} is empty of not.
     *
     * @param c to be checked
     * @return true if map is null, or it has no any entries.
     */
    public static boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * Check if the collection is not empty.
     *
     * @param collection to be checked
     * @param <T>        type of elements
     * @return true if collection {@link #isEmpty(Collection)} is false
     */
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    private CollectionUtils() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
