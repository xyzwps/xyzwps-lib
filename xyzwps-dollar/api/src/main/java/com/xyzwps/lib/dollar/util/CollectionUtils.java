package com.xyzwps.lib.dollar.util;

import com.xyzwps.lib.dollar.Direction;
import com.xyzwps.lib.dollar.Pair;

import java.util.*;
import java.util.function.*;

import static com.xyzwps.lib.dollar.util.Comparators.*;

public interface CollectionUtils {

    /**
     * Creates a list of elements split into groups the length of size.
     * If list can't be split evenly, the final chunk will be the remaining elements.
     *
     * @param list The list to handle
     * @param size Chunk size which should be greater than 0.
     * @param <T>  Element type
     * @return new list of chunks
     */
    default <T> List<List<T>> chunk(List<T> list, int size) {
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
    default <T> List<T> compact(List<T> list) {
        return filter(list, it -> !SharedUtils.isFalsey(it));
    }

    /**
     * Creates a new list which concatenating all lists in order.
     *
     * @param lists The lists to concatenate
     * @param <T>   Element type
     * @return concatenated new list
     */
    @SuppressWarnings("unchecked")
    default <T> List<T> concat(List<T>... lists) {
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
    default <T> List<T> filter(List<T> list, Predicate<T> predicate) {
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
    default <T> List<T> filter(List<T> list, BiPredicate<T, Integer> predicate) {
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
    default <T> Optional<T> first(Iterable<T> iterable) {
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
     * Map elements to {@link Iterable}s in order and flat them into next stage.
     *
     * @param flatMapFn which map an element to an {@link Iterable}
     * @param <R>       flatten elements type
     * @return next stage
     */
    default <T, R> List<R> flatMap(Iterable<T> iterable, Function<T, Iterable<R>> flatMapFn) {
        Objects.requireNonNull(flatMapFn);

        if (iterable == null) {
            return new ArrayList<>();
        }

        ArrayList<R> result = new ArrayList<>();
        for (T t : iterable) {
            Iterable<R> itr = flatMapFn.apply(t);
            if (itr != null) {
                itr.forEach(result::add);
            }
        }
        return result;
    }

    /**
     * 按顺序遍历 {@link Iterable} 中元素。
     * Iterate over the elements from an {@link Iterable} with their indices.
     *
     * @param iterable 要遍历的 Iterable。
     *                 <br/>
     *                 Which to be iterated.
     * @param handler  处理元素回调函数。
     *                 <br/>
     *                 The function to handle an element.
     * @param <T>      被遍历的元素类型。
     *                 <br/>
     *                 Type of elements.
     */
    default <T> void forEach(Iterable<T> iterable, Consumer<T> handler) {
        if (iterable == null) {
            return;
        }

        iterable.forEach(handler);
    }

    /**
     * 按顺序遍历 {@link Iterable} 中元素，并带上对应索引。
     * Iterate over the elements from an {@link Iterable} with their indices in order.
     *
     * @param iterable 要遍历的 Iterable。
     *                 <br/>
     *                 Which to be iterated.
     * @param handler  处理元素回调函数，第二个参数是元素的索引。
     *                 <br/>
     *                 The function to handle an element, and it's second parameter is element's index.
     * @param <T>      被遍历的元素类型。
     *                 <br/>
     *                 Type of elements.
     */
    default <T> void forEach(Iterable<T> iterable, ObjIntConsumer<T> handler) {
        if (iterable == null) {
            return;
        }

        int i = 0;
        for (T it : iterable) {
            handler.accept(it, i++);
        }
    }

    /**
     * Create a new {@link Map} from {@link Iterable}.
     * The values are {@link List} of elements with the same key,
     * and the keys are computed from corresponding elements.
     *
     * @param iterable to be handled
     * @param toKey    function mapping an element to it's key
     * @param <T>      type of iterable elements
     * @param <K>      type of key
     * @return new {@link Map}
     */
    default <T, K> Map<K, List<T>> groupBy(Iterable<T> iterable, Function<T, K> toKey) {
        Objects.requireNonNull(toKey);

        if (iterable == null) {
            return new HashMap<>();
        }

        Map<K, List<T>> result = new HashMap<>();
        iterable.forEach(it -> result.computeIfAbsent(toKey.apply(it), k -> new ArrayList<>()).add(it));
        return result;
    }

    /**
     * Alias of {@link #first(Iterable)}.
     *
     * @param iterable to be handled
     * @param <T>      type of the first element
     * @return {@link Optional} of the first element
     */
    default <T> Optional<T> head(Iterable<T> iterable) {
        return this.first(iterable);
    }

    /**
     * Create a new {@link Map} from {@link Iterable}.
     * The values are {@link Iterable} elements,
     * and the keys are computed from corresponding elements.
     * If two keys of different elements are equal, the first entry would be covered.
     *
     * @param iterable to be handled
     * @param toKey    function mapping an element to it's key
     * @param <T>      type of iterable elements
     * @param <K>      type of key
     * @return new {@link Map}
     */
    default <T, K> Map<K, T> keyBy(Iterable<T> iterable, Function<T, K> toKey) {
        Objects.requireNonNull(toKey);

        if (iterable == null) {
            return new HashMap<>();
        }

        Map<K, T> result = new HashMap<>();
        iterable.forEach(it -> result.computeIfAbsent(toKey.apply(it), k -> it));
        return result;
    }

    /**
     * Get the last element of a {@link List}.
     *
     * @param list to be handled
     * @param <T>  the element type of list
     * @return empty if list is empty or the last element of list is null.
     */
    default <T> Optional<T> last(List<T> list) {
        return isEmpty(list)
                ? Optional.empty()
                : Optional.ofNullable(list.getLast());
    }

    /**
     * Check if a {@link Collection} is empty of not.
     *
     * @param c to be checked
     * @return true if map is null, or it has no any entries.
     */
    default boolean isEmpty(Collection<?> c) {
        return c == null || c.isEmpty();
    }

    /**
     * Check if the collection is not empty.
     *
     * @param collection to be checked
     * @param <T>        type of elements
     * @return true if collection {@link #isEmpty(Collection)} is false
     */
    default <T> boolean isNotEmpty(Collection<T> collection) {
        return !isEmpty(collection);
    }

    /**
     * Mapping a list of elements to another.
     *
     * @param iterable to be mapped
     * @param mapFn    mapping function
     * @param <T>      type of elements applied to mapping function
     * @param <R>      type of elements returned by mapping function
     * @return mapping result
     */
    default <T, R> List<R> map(Iterable<T> iterable, Function<T, R> mapFn) {
        Objects.requireNonNull(mapFn);

        if (iterable == null) {
            return new ArrayList<>();
        }

        int capacity = iterable instanceof Collection<T> c ? c.size() : 16;

        List<R> result = new ArrayList<>(capacity);
        if (iterable instanceof ArrayList<T> list) {
            for (T t : list) {
                result.add(mapFn.apply(t));
            }
        } else {
            for (T t : iterable) result.add(mapFn.apply(t));
        }
        return result;
    }

    /**
     * Mapping a list of elements to another.
     *
     * @param iterable to be mapped
     * @param mapFn    mapping function
     * @param <T>      type of elements applied to mapping function
     * @param <R>      type of elements returned by mapping function
     * @return mapping result
     */
    default <T, R> List<R> map(Iterable<T> iterable, ObjIntFunction<T, R> mapFn) {
        Objects.requireNonNull(mapFn);

        if (iterable == null) {
            return new ArrayList<>();
        }

        int capacity = 16;
        if (iterable instanceof List<T> list) {
            capacity = list.size();
        }

        List<R> result = new ArrayList<>(capacity);
        int index = 0;
        for (T t : iterable) {
            result.add(mapFn.apply(t, index++));
        }
        return result;
    }

    /**
     * Order iterable into a list by element keys with specified direction.
     *
     * @param iterable  to be ordered
     * @param toKey     a function to get element key
     * @param direction order direction
     * @param <T>       type of elements
     * @param <K>       type of element keys
     * @return a list with sorted elements
     */
    default <T, K extends Comparable<K>> List<T> orderBy(Iterable<T> iterable, Function<T, K> toKey, Direction direction) {
        Objects.requireNonNull(toKey);
        Objects.requireNonNull(direction);

        if (iterable == null) {
            return new ArrayList<>();
        }


        List<T> list = SharedUtils.arrayListFrom(iterable.iterator());
        Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
        list.sort(comparator);
        return list;
    }

    /**
     * Reducing {@link Iterable} to a value which is the accumulated result of running each element in
     * {@link Iterable} through reducer.
     *
     * @param iterable  to be handled
     * @param initValue the init value
     * @param reducer   reducer
     * @param <T>       type of elements
     * @param <R>       type of result
     * @return reducing result
     */
    default <T, R> R reduce(Iterable<T> iterable, R initValue, BiFunction<R, T, R> reducer) {
        Objects.requireNonNull(reducer);

        if (iterable == null) {
            return initValue;
        }

        R result = initValue;
        for (T t : iterable) {
            result = reducer.apply(result, t);
        }
        return result;
    }

    /**
     * Add all elements from iterable into a {@link Set}.
     *
     * @param iterable to be handled
     * @param <T>      type of elements
     * @return a set of elements from iterable
     */
    default <T> Set<T> toSet(Iterable<T> iterable) {
        if (iterable == null) {
            return new HashSet<>();
        }

        Set<T> set = new HashSet<>();
        iterable.forEach(set::add);
        return set;
    }

    /**
     * Reverse elements from an iterable into a list.
     *
     * @param iterable to be reversed
     * @param <T>      type of elements
     * @return a list of elements in reversed order
     */
    default <T> List<T> reverse(Iterable<T> iterable) {
        ArrayList<T> list = reduce(iterable, new ArrayList<>(), (li, it) -> {
            li.add(it);
            return li;
        });

        int last = list.size() - 1;
        int half = list.size() / 2;
        for (int i = 0; i < half; i++) {
            T t1 = list.get(i);
            T t2 = list.get(last - i);
            list.set(last - i, t1);
            list.set(i, t2);
        }
        return list;
    }

    /**
     * Count the elements of a <code>collection</code>.
     * Return 0 if <code>collection</code> is <code>null</code>.
     *
     * @param collection which to handle
     * @param <E>        collection element type
     * @return count of elements in collection
     */
    default <E> int size(Collection<E> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * Alias of {@link #last(List)}
     *
     * @param list to be handled
     * @param <T>  the element type of list
     * @return empty if list is empty or the last element of list is null.
     */
    default <T> Optional<T> tail(List<T> list) {
        return this.last(list);
    }

    /**
     * Take the first <code>n</code> elements.
     *
     * @param iterable to be handled.
     * @param n        count of element to be taken which should be greater than 0
     * @param <T>      type of elements
     * @return a list of elements to be taken
     */
    default <T> List<T> take(Iterable<T> iterable, int n) {
        if (n < 1) {
            throw new IllegalArgumentException("You should take at least one element.");
        }

        if (iterable == null) {
            return new ArrayList<>();
        }

        ArrayList<T> list = new ArrayList<>();
        int i = 0;
        for (T t : iterable) {
            if (i < n) {
                list.add(t);
                i++;
            } else {
                break;
            }
        }
        return list;
    }

    /**
     * Take elements from iterable until the first element predicated to be false is found.
     *
     * @param iterable  to be handled.
     * @param predicate a function to test elements
     * @param <T>       type of elements
     * @return a list of elements to be taken
     */
    default <T> List<T> takeWhile(Iterable<T> iterable, Predicate<T> predicate) {
        Objects.requireNonNull(predicate);

        if (iterable == null) {
            return new ArrayList<>();
        }

        ArrayList<T> list = new ArrayList<>();
        for (T t : iterable) {
            if (predicate.test(t)) {
                list.add(t);
            } else {
                break;
            }
        }
        return list;
    }

    /**
     * Iterates over elements of {@link Iterable} and remove the duplicated.
     *
     * @param iterable to be handled
     * @param <T>      type of elements
     * @return new {@link List} with unique elements
     */
    default <T> List<T> unique(Iterable<T> iterable) {
        if (iterable == null) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        Set<T> dedupSet = new HashSet<>();
        for (T it : iterable) {
            if (!dedupSet.contains(it)) {
                dedupSet.add(it);
                result.add(it);
            }
        }
        return result;
    }

    /**
     * Iterates over elements of {@link Iterable} and remove the duplicated by keys.
     *
     * @param iterable to be handled
     * @param <T>      type of elements
     * @return new {@link List} with unique elements
     */
    default <T, K> List<T> uniqueBy(Iterable<T> iterable, Function<T, K> toKey) {
        Objects.requireNonNull(toKey);

        if (iterable == null) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        Set<K> dedupSet = new HashSet<>();
        for (T it : iterable) {
            K key = toKey.apply(it);
            if (!dedupSet.contains(key)) {
                dedupSet.add(key);
                result.add(it);
            }
        }
        return result;
    }

    /**
     * Iterates over elements of {@link Iterable} and remove the duplicated by keys.
     * The second argument of toKey function is the index of corresponding element.
     *
     * @param iterable to be handled
     * @param <T>      type of elements
     * @return new {@link List} with unique elements
     */
    default <T, K> List<T> uniqueBy(Iterable<T> iterable, ObjIntFunction<T, K> toKey) {
        Objects.requireNonNull(toKey);

        if (iterable == null) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        Set<K> dedupSet = new HashSet<>();
        int i = 0;
        for (T it : iterable) {
            K key = toKey.apply(it, i++);
            if (!dedupSet.contains(key)) {
                dedupSet.add(key);
                result.add(it);
            }
        }
        return result;
    }


    /**
     * {@link #zip(Iterable, Iterable, BiFunction) zip} two lists into a list of pairs.
     *
     * @param left  first list
     * @param right second list
     * @param <T>   element type of the first list
     * @param <R>   element type of the second list
     * @return a list of pairs
     */
    default <T, R> List<Pair<T, R>> zip(Iterable<T> left, Iterable<R> right) {
        return zip(left, right, Pair::of);
    }


    /**
     * Combine the elements at the same position from two lists into one object in order.
     *
     * @param left      first list
     * @param right     second list
     * @param combineFn combine function.
     * @param <T>       element type of the first list
     * @param <R>       element type of the second list
     * @return a list of combined
     */
    default <T, R, S> List<S> zip(Iterable<T> left, Iterable<R> right, BiFunction<T, R, S> combineFn) {
        Objects.requireNonNull(combineFn);

        List<S> result = new ArrayList<>();
        Iterator<T> li = left == null ? EmptyIterator.create() : left.iterator();
        Iterator<R> ri = right == null ? EmptyIterator.create() : right.iterator();
        while (true) {
            int state = 0;
            if (li.hasNext()) state += 1;
            if (ri.hasNext()) state += 2;
            switch (state) {
                case 0:
                    return result;
                case 1:
                    result.add(combineFn.apply(li.next(), null));
                    break;
                case 2:
                    result.add(combineFn.apply(null, ri.next()));
                    break;
                case 3:
                    result.add(combineFn.apply(li.next(), ri.next()));
                    break;
                default:
                    throw new Unreachable();
            }
        }
    }
}
