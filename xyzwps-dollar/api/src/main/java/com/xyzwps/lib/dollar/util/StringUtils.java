package com.xyzwps.lib.dollar.util;

/**
 * A collection of string utility functions.
 */
public interface StringUtils {

    /**
     * Check if a string is empty or not.
     *
     * @param string to be checked
     * @return true if string is null, or it's length is 0
     */
    default boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }


    /**
     * Check if the string is not empty.
     *
     * @param string to be checked
     * @return true if string {@link #isEmpty(String)} is false
     */
    default boolean isNotEmpty(String string) {
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
    default String pad(String string, int length, String chars) {
        if (length < 0) {
            throw new IllegalArgumentException("Argument length cannot be less than 0");
        }

        string = SharedUtils.defaultTo(string, "");
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
    default String padEnd(String string, int length, String chars) {
        if (length < 0) {
            throw new IllegalArgumentException("Argument length cannot be less than 0");
        }

        string = SharedUtils.defaultTo(string, "");
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
    default String padStart(String string, int length, String chars) {
        if (length < 0) {
            throw new IllegalArgumentException("Argument length cannot be less than 0");
        }

        string = SharedUtils.defaultTo(string, "");
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
     * Take the substring made up of the first <code>n</code> characters.
     *
     * @param str the string to take
     * @param n   substring length
     * @return the substring made up of the first <code>n</code> characters.
     */
    default String take(final String str, final int n) {
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
    default String takeRight(final String str, final int n) {
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
    default int length(String string) {
        return string == null ? 0 : string.length();
    }
}
