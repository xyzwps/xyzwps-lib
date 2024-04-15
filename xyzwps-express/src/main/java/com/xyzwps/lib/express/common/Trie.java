package com.xyzwps.lib.express.common;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class Trie<T> {

    private final Node<T> root;

    public Trie() {
        this.root = new Node<>(new HashMap<>());
    }

    public void add(SegmentedPath path, T value) {
        if (path == null || path.isRoot()) {
            if (root.value != null) {
                throw duplicatedPathError(SegmentedPath.ROOT);
            }
            this.root.value = value;
            return;
        }

        var current = root;
        for (var segment : path) {
            current = current.mapping.computeIfAbsent(segment, (key) -> new Node<T>(new HashMap<>()));
        }
        if (current.value != null) {
            throw duplicatedPathError(path);
        }
        current.value = value;
    }

    private static IllegalArgumentException duplicatedPathError(SegmentedPath duplicatedPath) {
        return new IllegalArgumentException(String.format("Duplicated path '%s'", duplicatedPath));
    }

    public void addTrie(SegmentedPath prefix, Trie<T> trie) {
        Objects.requireNonNull(prefix);
        Objects.requireNonNull(trie);
        trie.iterate((segments, value) -> this.add(prefix.append(segments), value));

    }

    public Optional<T> get(SegmentedPath path) {
        if (path == null || path.isRoot()) {
            return Optional.ofNullable(root.value);
        }

        var current = root;
        for (var segment : path) {
            current = current.mapping.get(segment);
            if (current == null) {
                return Optional.empty();
            }
        }
        return Optional.ofNullable(current.value);
    }

    /**
     * @param path            path
     * @param defaultSupplier cannot produce a null.
     */
    public T getOrSetDefault(SegmentedPath path, Supplier<T> defaultSupplier) {
        if (path == null || path.isRoot()) {
            if (root.value == null) {
                return root.value = Objects.requireNonNull(defaultSupplier.get(), "Default supplier cannot produce a null");
            }
            return root.value;
        }

        var current = root;
        for (var segment : path) {
            current = current.mapping.computeIfAbsent(segment, (key) -> new Node<T>(new HashMap<>()));
        }
        if (current.value == null) {
            return current.value = Objects.requireNonNull(defaultSupplier.get(), "Default supplier cannot produce a null");
        }
        return current.value;
    }

    public void iterate(BiConsumer<SegmentedPath, T> consumer) {
        this.iterate(SegmentedPath.ROOT, root, Objects.requireNonNull(consumer));
    }

    // TODO: support /{param}
    // TODO: support /*
    // TODO: support /**

    private void iterate(SegmentedPath prefix, Node<T> node, BiConsumer<SegmentedPath, T> consumer) {
        if (node.value != null) {
            consumer.accept(prefix, node.value);
        }
        node.mapping.forEach((key, v) -> iterate(prefix.append(key), v, consumer));
    }

    // TODO: to immutable

    static class Node<T> {
        final Map<String, Node<T>> mapping;
        T value;

        Node(Map<String, Node<T>> mapping) {
            this.mapping = Objects.requireNonNull(mapping);
        }
    }
}
