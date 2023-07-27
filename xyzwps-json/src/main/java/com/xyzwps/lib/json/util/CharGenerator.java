package com.xyzwps.lib.json.util;

public interface CharGenerator {

    boolean hasNext();

    /**
     * Get next char.
     * <p>
     * No guarantee when <tt>!hasNext()</tt>.
     */
    char next();

    /**
     * Seek next char but not get it.
     * <p>
     * No guarantee when <tt>!hasNext()</tt>.
     */
    char seek();
}
