package com.xyzwps.lib.express.common;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Trie<T> {

    private final Node<T> root;

    public Trie() {
        this.root = new Node<>(new HashMap<>());
    }

    public void addSegmentedPath(List<String> pathSegments, T value) {
        if (pathSegments == null || pathSegments.isEmpty()) {
            // empty
            if (root.value != null) {
                throw new IllegalStateException("Duplicated path '/'");
            }
            this.root.value = value;
            return;
        }

        var current = root;
        for (var segment : pathSegments) {
            current = current.mapping.computeIfAbsent(segment, (key) -> new Node<T>(new HashMap<>()));
        }
        if (current.value != null) {
            throw new IllegalStateException(String.format("Duplicated path '/%s'", String.join("/", pathSegments)));
        }
        current.value = value;
    }

    public void addTrie(List<String> prefix, Trie<T> trie) {
        Objects.requireNonNull(prefix);
        Objects.requireNonNull(trie);

        trie.iterate((segments, value) -> {
            var fullSegments = new ArrayList<String>();
            fullSegments.addAll(prefix);
            fullSegments.addAll(segments);
            this.addSegmentedPath(fullSegments, value);
        });

    }

    public Optional<T> get(List<String> pathSegments) {
        if (pathSegments == null || pathSegments.isEmpty()) {
            return Optional.ofNullable(root.value);
        }

        var current = root;
        for (var segment : pathSegments) {
            current = current.mapping.get(segment);
            if (current == null) {
                return Optional.empty();
            }
        }
        return Optional.ofNullable(current.value);
    }

    /**
     * @param pathSegments    path segments
     * @param defaultSupplier cannot produce a null.
     */
    public T getOrSetDefault(List<String> pathSegments, Supplier<T> defaultSupplier) {
        if (pathSegments == null || pathSegments.isEmpty()) {
            if (root.value == null) {
                return root.value = Objects.requireNonNull(defaultSupplier.get(), "Default supplier cannot produce a null");
            }
            return root.value;
        }

        var current = root;
        for (var segment : pathSegments) {
            current = current.mapping.computeIfAbsent(segment, (key) -> new Node<T>(new HashMap<>()));
        }
        if (current.value == null) {
            return current.value = Objects.requireNonNull(defaultSupplier.get(), "Default supplier cannot produce a null");
        }
        return current.value;
    }

    public void iterate(BiConsumer<List<String>, T> consumer) {
        this.iterate(List.of(), root, Objects.requireNonNull(consumer));
    }

    private void iterate(List<String> prefix, Node<T> node, BiConsumer<List<String>, T> consumer) {
        if (node.value != null) {
            consumer.accept(prefix, node.value);
        }
        node.mapping.forEach((key, v) -> iterate(append(prefix, key), v, consumer));
    }

    private static <E> List<E> append(List<E> list, E e) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(e);
        var array = list.toArray((E[]) new Object[list.size() + 1]);
        array[array.length - 1] = e;
        return List.of(array);
    }

    // TODO: to immutable

    /**
     * <pre>
     *       (a, b, c)
     *           |
     *          / \
     *         T  (e, f, g)
     *                |
     *
     * </pre>
     */
    static class Node<T> {
        final Map<String, Node<T>> mapping;
        T value;

        Node(Map<String, Node<T>> mapping) {
            this.mapping = Objects.requireNonNull(mapping);
        }
    }
}
