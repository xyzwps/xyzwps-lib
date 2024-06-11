package com.xyzwps.lib.json.util;

import java.io.Reader;

public interface CharGenerator {

    /**
     * Check if there is next char.
     *
     * @return <code>true</code> if there is next char, otherwise <code>false</code>.
     */
    boolean hasNext();

    /**
     * Get next char.
     * <p>
     * No guarantee when <code>!hasNext()</code>, so you should make sure <code>hasNext()</code> before calling this method.
     *
     * @return next char
     */
    char next();

    /**
     * Seek next char but not get it.
     * <p>
     * No guarantee when <code>!hasNext()</code>, so you should make sure <code>hasNext()</code> before calling this method.
     *
     * @return next char
     */
    char seek();


    static CharGenerator from(String str) {
        return new StringCharGenerator(str);
    }

    static CharGenerator from(Reader reader) {
        return new ReaderCharGenerator(reader);
    }
}
