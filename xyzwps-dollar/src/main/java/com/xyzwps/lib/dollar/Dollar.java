package com.xyzwps.lib.dollar;

import com.xyzwps.lib.dollar.util.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.*;
import java.util.regex.Pattern;

import static com.xyzwps.lib.dollar.Comparators.ascComparator;
import static com.xyzwps.lib.dollar.Comparators.descComparator;

/**
 * Where to start.
 * <p>
 * TODO: optimize for RandomAccess
 * TODO: add examples
 * TODO: all apis should be null tolerant
 */
public final class Dollar {


    /**
     * Create a stage chain from a {@link List}.
     *
     * @param list source list. Null is acceptable.
     * @param <T>  list element type
     * @return a list stage
     */
    public static <T> Seq<T> $(Iterable<T> list) {
        return Seq.from(list);
    }


    /**
     * Create a stage chain from a map.
     *
     * @param map source map. Null is acceptable.
     * @param <K> map key type
     * @param <V> map value type
     * @return a map stage
     */
    public static <K, V> MapEntrySeq<K, V> $(Map<K, V> map) {
        return MapEntrySeq.from(map);
    }


    /**
     * Dollar functions.
     */
    public static final class $ {

        /**
         * Create an empty list stage.
         *
         * @param <T> element type
         * @return list stage
         */
        public static <T> Seq<T> empty() {
            return Seq.empty();
        }

        /**
         * Create a new chain of map entries from the specified map producer.
         *
         * @param mapProducer the producer of the map
         * @param <K>         the type of keys
         * @param <V>         the type of values
         * @return a new chain of map entries
         */
        public static <K, V> MapEntrySeq<K, V> fromMapSupplier(Supplier<Map<K, V>> mapProducer) {
            Objects.requireNonNull(mapProducer);
            // TODO: 修改 api 形式
            return MapEntrySeq.from(mapProducer.get());
        }


        /**
         * Create a stage from elements.
         *
         * @param args elements to be handled
         * @param <T>  type of elements
         * @return list stage
         */
        @SafeVarargs
        public static <T> Seq<T> just(T... args) {
            return Seq.just(args);
        }

        public static Seq<Integer> infinite(int start) {
            return Seq.infinite(start);
        }

        /**
         * Handle a range.
         *
         * @param start range start - included
         * @param end   range end - excluded
         * @return list stage
         */
        public static Seq<Integer> range(int start, int end) {
            return Seq.range(start, end);
        }

        /**
         * If the value is null, return the default value.
         *
         * @param value        which to be checked
         * @param defaultValue fallback default value
         * @param <T>          type of value
         * @return value if it is not null; defaultValue if value is null
         */
        public static <T> T defaultTo(T value, T defaultValue) {
            return value == null ? defaultValue : value;
        }

        /**
         * Check if a value is falsey. The value would be considered as falsey if it is null, false, 0, or empty string.
         *
         * @param value which to be checked
         * @return true if it would be considered as false
         * @see <a href="https://developer.mozilla.org/en-US/docs/Glossary/Falsy">MDN: Falsy</a>
         */
        public static boolean isFalsey(Object value) {
            return value == null
                    || Objects.equals(value, false)
                    || "".equals(value)
                    || Objects.equals(value, 0)
                    || Objects.equals(value, 0L)
                    || Objects.equals(value, 0.0)
                    || Objects.equals(value, 0.0f)
                    || Objects.equals(value, (short) 0)
                    || Objects.equals(value, Float.NaN)
                    || Objects.equals(value, Double.NaN)
                    || Objects.equals(value, BigInteger.ZERO)
                    || Objects.equals(value, BigDecimal.ZERO);
        }

        /**
         * Create an array list from an iterator.
         *
         * @param itr provides items for result
         * @param <T> type of element
         * @return an array list
         */
        public static <T> ArrayList<T> arrayListFrom(Iterator<T> itr) {
            if (itr == null) {
                return new ArrayList<>();
            }

            var list = new ArrayList<T>();
            while (itr.hasNext()) list.add(itr.next());
            return list;
        }

        /**
         * Replace all substrings that match the given pattern with the result of the function.
         * For example:
         * <pre>
         * var pattern = Pattern.compile("\\{}");
         * System.out.println(replaceAll("a={} b={} c={}", pattern, i -> "{" + i + "}"));
         * </pre>
         * Output:
         * <pre>
         * a={0} b={1} c={2}
         * </pre>
         * <p>
         * If any of the arguments is {@code null}, the method will return the original string.
         *
         * @param string         to be replaced
         * @param pattern        to match
         * @param replacementGen to generate the replacement string by index
         * @return the replaced string
         */
        public static String replaceAll(String string, Pattern pattern, IntFunction<String> replacementGen) {
            if (string == null || pattern == null || replacementGen == null) {
                return string;
            }

            var matcher = pattern.matcher(string);
            boolean result = matcher.find();
            if (result) {
                StringBuilder sb = new StringBuilder();
                int start = 0;
                do {
                    matcher.appendReplacement(sb, replacementGen.apply(start++));
                    result = matcher.find();
                } while (result);
                matcher.appendTail(sb);
                return sb.toString();
            }
            return string;
        }

        /**
         * Converts a string to camel case. For example:
         * <pre>
         *     "foo_bar" -> "fooBar"
         * </pre>
         *
         * @param str the string to convert
         * @return the camel case string
         */
        public static String camelCase(String str) {
            if (str == null) {
                return null;
            }

            var len = str.length();
            if (len == 0) {
                return "";
            }

            var sb = new StringBuilder();
            boolean nextUpper = false;
            for (var i = 0; i < len; i++) {
                var c = str.charAt(i);
                switch (c) {
                    case '_', '-', ' ' -> nextUpper = true;
                    default -> {
                        if (nextUpper) {
                            if (sb.isEmpty()) {
                                sb.append(Character.toLowerCase(c));
                            } else {
                                sb.append(Character.toUpperCase(c));
                            }
                            nextUpper = false;
                        } else {
                            sb.append(Character.toLowerCase(c));
                        }
                    }
                }
            }
            return sb.toString();
        }


        /**
         * Check if a string is empty or not.
         *
         * @param string to be checked
         * @return true if string is null, or it's length is 0
         */
        public static boolean isEmpty(String string) {
            return string == null || string.isEmpty();
        }


        /**
         * Check if the string is not empty.
         *
         * @param string to be checked
         * @return true if string {@link #isEmpty(String)} is false
         */
        public static boolean isNotEmpty(String string) {
            return !isEmpty(string);
        }


        /**
         * Pads <code>string</code> on the left and right sides if it's shorter than <code>length</code>.
         * Padding characters are truncated if they can't be evenly divided by <code>length</code>.
         *
         * @param string The string to pad
         * @param length The padding length
         * @param chars  The string used as padding
         * @return Padded string
         */
        public static String pad(String string, int length, String chars) {
            if (length < 0) {
                throw new IllegalArgumentException("Argument length cannot be less than 0");
            }

            string = $.defaultTo(string, "");
            if (string.length() >= length) {
                return string;
            }

            char[] padChars = (isEmpty(chars) ? " " : chars).toCharArray();
            StringBuilder sb = new StringBuilder();
            int padLength = length - string.length();
            int padHalf = padLength / 2;
            for (int i = 0; i < padHalf; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            sb.append(string);
            for (int i = padHalf; i < padLength; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            return sb.toString();

        }

        /**
         * Pads <code>string</code> on the right side if it's shorter than <code>length</code>.
         * Padding characters are truncated if they exceed <code>length</code>.
         *
         * @param string The string to pad
         * @param length The padding length
         * @param chars  The string used as padding
         * @return Padded string
         */
        public static String padEnd(String string, int length, String chars) {
            if (length < 0) {
                throw new IllegalArgumentException("Argument length cannot be less than 0");
            }

            string = $.defaultTo(string, "");
            if (string.length() >= length) {
                return string;
            }

            char[] padChars = (isEmpty(chars) ? " " : chars).toCharArray();
            StringBuilder sb = new StringBuilder(string);
            int padLength = length - string.length();
            for (int i = 0; i < padLength; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            return sb.toString();
        }

        /**
         * Pads <code>string</code> on the left side if it's shorter than <code>length</code>.
         * Padding characters are truncated if they exceed <code>length</code>.
         *
         * @param string The string to pad
         * @param length The padding length
         * @param chars  The string used as padding
         * @return Padded string
         */
        public static String padStart(String string, int length, String chars) {
            if (length < 0) {
                throw new IllegalArgumentException("Argument length cannot be less than 0");
            }

            string = $.defaultTo(string, "");
            if (string.length() >= length) {
                return string;
            }

            char[] padChars = (isEmpty(chars) ? " " : chars).toCharArray();
            StringBuilder sb = new StringBuilder();
            int padLength = length - string.length();
            for (int i = 0; i < padLength; i++) {
                sb.append(padChars[i % padChars.length]);
            }
            sb.append(string);
            return sb.toString();
        }

        /**
         * Converts a string to snake case. For example:
         * <pre>
         *    "fooBar" ->  "foo_bar"
         * </pre>
         *
         * @param str the string to convert
         * @return the snake case string
         */
        public static String snakeCase(String str) {
            if (str == null) {
                return null;
            }

            var len = str.length();
            if (len == 0) {
                return "";
            }

            var sb = new StringBuilder();
            var shouldAppendUnderline = false;
            for (var i = 0; i < len; i++) {
                var c = str.charAt(i);
                if (c == ' ' || c == '-' || c == '_') {
                    if (!shouldAppendUnderline && !sb.isEmpty()) {
                        shouldAppendUnderline = true;
                    }
                    continue;
                }
                if (Character.isUpperCase(c)) {
                    if (!shouldAppendUnderline && !sb.isEmpty()) {
                        shouldAppendUnderline = true;
                    }
                }
                if (shouldAppendUnderline) {
                    sb.append('_').append(Character.toLowerCase(c));
                    shouldAppendUnderline = false;
                } else {
                    sb.append(Character.toLowerCase(c));
                }
            }
            return sb.toString();
        }

        /**
         * Take the substring made up of the first <code>n</code> characters.
         *
         * @param str the string to take
         * @param n   substring length
         * @return the substring made up of the first <code>n</code> characters.
         */
        public static String take(final String str, final int n) {
            if (n < 0) {
                throw new IllegalArgumentException("n should be greater than or equal to 0");
            }

            if (str == null || n == 0) {
                return "";
            }

            return str.length() < n ? str : str.substring(0, n);
        }

        /**
         * Take the substring made up of the last <code>n</code> characters.
         *
         * @param str the string to take
         * @param n   substring length
         * @return the substring made up of the last <code>n</code> characters.
         */
        public static String takeRight(final String str, final int n) {
            if (n < 0) {
                throw new IllegalArgumentException("n should be greater than or equal to 0");
            }

            if (str == null || n == 0) {
                return "";
            }

            var len = str.length();
            return len < n ? str : str.substring(len - n, len);
        }

        /**
         * Get the length of specified string.
         *
         * @param string to check
         * @return the length of string
         */
        public static int length(String string) {
            return string == null ? 0 : string.length();
        }

        /**
         * Check if a {@link Map} is empty of not.
         *
         * @param map to be checked
         * @return true if map is null, or it has no any entries.
         */
        public static boolean isEmpty(Map<?, ?> map) {
            return map == null || map.isEmpty();
        }

        /**
         * Check if a {@link Map} is not empty.
         *
         * @param map to be checked
         * @return true if map {@link #isEmpty(Map)} is false
         */
        public static boolean isNotEmpty(Map<?, ?> map) {
            return !isEmpty(map);
        }

        /**
         * Create a new {@link Map} with new keys, which are mapped from the old keys.
         * If two new keys of old keys are the same, the last reached entry would be discarded.
         *
         * @param map   to be handled
         * @param mapFn mapping function
         * @param <K>   type of keys
         * @param <V>   type of values
         * @param <K2>  type of mapped keys
         * @return new {@link Map}
         */
        public static <K, V, K2> Map<K2, V> mapKeys(Map<K, V> map, Function<K, K2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            Map<K2, V> result = new HashMap<>();
            map.forEach((k, v) -> {
                K2 k2 = mapFn.apply(k);
                if (!result.containsKey(k2)) {
                    result.put(k2, v);
                }
            });
            return result;
        }

        /**
         * Create a new {@link Map} with new keys, which are mapped from the old keys.
         * If two new keys of different entries are equal, the last reached entry would be discarded.
         *
         * @param map   to be handled
         * @param mapFn mapping function
         * @param <K>   type of keys
         * @param <V>   type of values
         * @param <K2>  type of mapped keys
         * @return new {@link Map}
         */
        public static <K, V, K2> Map<K2, V> mapKeys(Map<K, V> map, BiFunction<K, V, K2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            Map<K2, V> result = new HashMap<>();
            map.forEach((k, v) -> {
                K2 k2 = mapFn.apply(k, v);
                if (!result.containsKey(k2)) {
                    result.put(k2, v);
                }
            });
            return result;
        }

        /**
         * Mapping {@link Map} values to another.
         *
         * @param map   {@link Map} to be handled.
         * @param mapFn mapping function
         * @param <K>   type of {@link Map} key
         * @param <V>   type of {@link Map} value
         * @param <V2>  type of mapping result
         * @return a new map
         */
        public static <K, V, V2> Map<K, V2> mapValues(Map<K, V> map, Function<V, V2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            Map<K, V2> result = new HashMap<>();
            map.forEach((k, v) -> result.put(k, mapFn.apply(v)));
            return result;
        }

        /**
         * Mapping {@link Map} values to another.
         *
         * @param map   {@link Map} to be handled.
         * @param mapFn mapping function which accept key as the second argument
         * @param <K>   type of {@link Map} key
         * @param <V>   type of {@link Map} value
         * @param <V2>  type of mapping result
         * @return a new map
         */
        public static <K, V, V2> Map<K, V2> mapValues(Map<K, V> map, BiFunction<V, K, V2> mapFn) {
            Objects.requireNonNull(mapFn);

            if (isEmpty(map)) {
                return new HashMap<>();
            }

            Map<K, V2> result = new HashMap<>();
            map.forEach((k, v) -> result.put(k, mapFn.apply(v, k)));
            return result;
        }


        /**
         * Reducing {@link Iterable} to a value which is the accumulated result of running each element in
         * {@link Iterable} through reducer.
         *
         * @param map       to be handled
         * @param initValue the init value
         * @param reducer   reducer
         * @param <K>       type of map keys
         * @param <V>       type of map values
         * @param <R>       type of result
         * @return reducing result
         */
        public static <K, V, R> R reduce(Map<K, V> map, R initValue, Function3<R, K, V, R> reducer) {
            Objects.requireNonNull(reducer);

            if (map == null) {
                return initValue;
            }

            final Env1<R> env = new Env1<>();
            env.v1 = initValue;
            map.forEach((k, v) -> env.v1 = reducer.apply(env.v1, k, v));
            return env.v1;
        }

        /**
         * A simple environment class for holding a single value.
         *
         * @param <T> type of value
         */
        private static class Env1<T> {

            /**
             * Default constructor.
             */
            Env1() {
            }

            /**
             * The value.
             */
            T v1;
        }

        /**
         * Count the entries of a <code>map</code>.
         * Return 0 if <code>map</code> is <code>null</code>.
         *
         * @param map which to handle
         * @param <K> type of keys
         * @param <V> type of values
         * @return count of entries in map
         */
        public static <K, V> int size(Map<K, V> map) {
            return map == null ? 0 : map.size();
        }

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
         * The definition of falsey can be seen at {@link $#isFalsey}
         *
         * @param list The list to filter. Null is acceptable.
         * @param <T>  List element type
         * @return new compacted list
         * @see $#isFalsey(Object)
         */
        public static <T> List<T> compact(List<T> list) {
            return filter(list, it -> !$.isFalsey(it));
        }

        /**
         * Creates a new list which concatenating all lists in order.
         *
         * @param lists The lists to concatenate
         * @param <T>   Element type
         * @return concatenated new list
         */
        @SuppressWarnings("unchecked")
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
         * Map elements to {@link Iterable}s in order and flat them into next stage.
         *
         * @param iterable  to be handled
         * @param flatMapFn which map an element to an {@link Iterable}
         * @param <T>       type of elements
         * @param <R>       flatten elements type
         * @return next stage
         */
        public static <T, R> List<R> flatMap(Iterable<T> iterable, Function<T, Iterable<R>> flatMapFn) {
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
        public static <T> void forEach(Iterable<T> iterable, Consumer<T> handler) {
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
        public static <T> void forEach(Iterable<T> iterable, ObjIntConsumer<T> handler) {
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
        public static <T, K> Map<K, List<T>> groupBy(Iterable<T> iterable, Function<T, K> toKey) {
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
        public static <T> Optional<T> head(Iterable<T> iterable) {
            return $.first(iterable);
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
        public static <T, K> Map<K, T> keyBy(Iterable<T> iterable, Function<T, K> toKey) {
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
        public static <T> Optional<T> last(List<T> list) {
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

        /**
         * Mapping a list of elements to another.
         *
         * @param iterable to be mapped
         * @param mapFn    mapping function
         * @param <T>      type of elements applied to mapping function
         * @param <R>      type of elements returned by mapping function
         * @return mapping result
         */
        public static <T, R> List<R> map(Iterable<T> iterable, Function<T, R> mapFn) {
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
        public static <T, R> List<R> map(Iterable<T> iterable, ObjIntFunction<T, R> mapFn) {
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
        public static <T, K extends Comparable<K>> List<T> orderBy(Iterable<T> iterable, Function<T, K> toKey, Direction direction) {
            Objects.requireNonNull(toKey);
            Objects.requireNonNull(direction);

            if (iterable == null) {
                return new ArrayList<>();
            }



            List<T> list = $.arrayListFrom(iterable.iterator());
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
        public static <T, R> R reduce(Iterable<T> iterable, R initValue, BiFunction<R, T, R> reducer) {
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
        public static <T> Set<T> toSet(Iterable<T> iterable) {
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
        public static <T> List<T> reverse(Iterable<T> iterable) {
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
        public static <E> int size(Collection<E> collection) {
            return collection == null ? 0 : collection.size();
        }

        /**
         * Alias of {@link #last(List)}
         *
         * @param list to be handled
         * @param <T>  the element type of list
         * @return empty if list is empty or the last element of list is null.
         */
        public static <T> Optional<T> tail(List<T> list) {
            return $.last(list);
        }

        /**
         * Take the first <code>n</code> elements.
         *
         * @param iterable to be handled.
         * @param n        count of element to be taken which should be greater than 0
         * @param <T>      type of elements
         * @return a list of elements to be taken
         */
        public static <T> List<T> take(Iterable<T> iterable, int n) {
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
        public static <T> List<T> takeWhile(Iterable<T> iterable, Predicate<T> predicate) {
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
        public static <T> List<T> unique(Iterable<T> iterable) {
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
         * @param toKey    function to get key from element
         * @param <T>      type of elements
         * @param <K>      type of keys
         * @return new {@link List} with unique elements
         */
        public static <T, K> List<T> uniqueBy(Iterable<T> iterable, Function<T, K> toKey) {
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
         * @param toKey    function to get key from element
         * @param <T>      type of elements
         * @param <K>      type of keys
         * @return new {@link List} with unique elements
         */
        public static <T, K> List<T> uniqueBy(Iterable<T> iterable, ObjIntFunction<T, K> toKey) {
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
        public static <T, R> List<Pair<T, R>> zip(Iterable<T> left, Iterable<R> right) {
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
         * @param <S>       type of combined object
         * @return a list of combined
         */
        public static <T, R, S> List<S> zip(Iterable<T> left, Iterable<R> right, BiFunction<T, R, S> combineFn) {
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


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap() {
            return new HashMap<>();
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param k8  the eighth key
         * @param v8  the eighth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            map.put(k8, v8);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param k8  the eighth key
         * @param v8  the eighth value
         * @param k9  the ninth key
         * @param v9  the ninth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            map.put(k8, v8);
            map.put(k9, v9);
            return map;
        }


        /**
         * Create a {@link HashMap} with key-value pairs.
         *
         * @param k1  the first key
         * @param v1  the first value
         * @param k2  the second key
         * @param v2  the second value
         * @param k3  the third key
         * @param v3  the third value
         * @param k4  the fourth key
         * @param v4  the fourth value
         * @param k5  the fifth key
         * @param v5  the fifth value
         * @param k6  the sixth key
         * @param v6  the sixth value
         * @param k7  the seventh key
         * @param v7  the seventh value
         * @param k8  the eighth key
         * @param v8  the eighth value
         * @param k9  the ninth key
         * @param v9  the ninth value
         * @param k10 the tenth key
         * @param v10 the tenth value
         * @param <K> key type
         * @param <V> value type
         * @return new HashMap
         */
        public static <K, V> HashMap<K, V> hashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8, K k9, V v9, K k10, V v10) {
            var map = new HashMap<K, V>();
            map.put(k1, v1);
            map.put(k2, v2);
            map.put(k3, v3);
            map.put(k4, v4);
            map.put(k5, v5);
            map.put(k6, v6);
            map.put(k7, v7);
            map.put(k8, v8);
            map.put(k9, v9);
            map.put(k10, v10);
            return map;
        }


        /**
         * Create a list.
         *
         * @param args elements of list
         * @param <T>  type of elements
         * @return new list
         */
        @SuppressWarnings("unchecked")
        public static <T> ArrayList<T> arrayList(T... args) {
            var list = new ArrayList<T>(args.length);
            list.addAll(Arrays.asList(args));
            return list;
        }
    }


    private Dollar() throws IllegalAccessException {
        throw new IllegalAccessException("???");
    }
}
