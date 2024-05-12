package com.xyzwps.lib.express.util;

import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public interface MultiValuesMap<K, V> {

    /**
     * Append a specified name/value pair.
     * If the name does not exist, we add it.
     *
     * @param name  cannot be null
     * @param value cannot be null
     */
    void append(K name, V value);

    /**
     * Delete values by a name.
     *
     * @param name cannot be null
     */
    void delete(K name);

    /**
     * Iterate all name/values pairs via a callback function.
     *
     * @param callback cannot be null
     */
    void forEach(BiConsumer<K, List<V>> callback);

    /**
     * Get the first value associated with a given name.
     *
     * @param name cannot be null
     * @return null if name does not exist
     */
    V get(K name);

    /**
     * Get all values associated with a given name.
     * <p>
     * You should NOT change corresponding values via the returned list,
     * but {@link #append(K, V)} or {@link #set(K, V)}.
     * We would not give the guarantee that you can do it as you expected.
     *
     * @param name cannot be null
     * @return at least an empty list
     */
    List<V> getAll(K name);

    /**
     * Check the name whether exists.
     *
     * @param name cannot be null.
     * @return true if the name has already existed; or else false
     */
    boolean has(K name);

    /**
     * Return a set containing all names already been added.
     *
     * @return never be null
     */
    Set<K> names();

    /**
     * Set a value to a specified name.
     * If the name does not exist, we add it.
     * If the name exists, we overwrite it.
     *
     * @param name  cannot be null
     * @param value cannot be null
     */
    void set(K name, V value);

}
