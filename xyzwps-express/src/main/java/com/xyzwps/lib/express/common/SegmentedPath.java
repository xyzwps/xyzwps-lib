package com.xyzwps.lib.express.common;

import com.xyzwps.lib.dollar.iterator.ArrayIterable;

import java.util.Arrays;
import java.util.Iterator;

import static com.xyzwps.lib.dollar.Dollar.*;

public final class SegmentedPath implements Comparable<SegmentedPath>, Iterable<String> {

    private final String[] segments;

    /**
     * @param checkStart started index to check segments
     * @param segments   any of segments cannot be empty
     */
    private SegmentedPath(int checkStart, String... segments) {
        if (segments == null || segments.length == 0) {
            this.segments = EMPTY_ARRAY;
            return;
        }

        for (int i = checkStart; i < segments.length; i++) {
            var segment = segments[i];
            if ($.isEmpty(segment)) {
                throw new IllegalArgumentException(String.format("The segments[%d] cannot be empty", i));
            }
            // TODO: 更多检查
        }
        this.segments = Arrays.copyOf(segments, segments.length);
    }

    private static final String[] EMPTY_ARRAY = new String[0];

    public static final SegmentedPath ROOT = new SegmentedPath(0);

    public SegmentedPath append(String segment) {
        final int thisLen = segments.length;
        var newSegments = new String[thisLen + 1];
        System.arraycopy(segments, 0, newSegments, 0, thisLen);
        newSegments[thisLen] = segment;
        return new SegmentedPath(thisLen, newSegments);
    }

    public SegmentedPath append(SegmentedPath path) {
        if (path == null || path.isRoot()) {
            return this;
        }

        final int thisLen = segments.length;
        var newSegments = new String[thisLen + path.segments.length];
        System.arraycopy(segments, 0, newSegments, 0, thisLen);
        System.arraycopy(path.segments, 0, newSegments, thisLen, path.segments.length);
        return new SegmentedPath(newSegments.length, newSegments);
    }

    @Override
    public int compareTo(SegmentedPath o) {
        return Arrays.compare(segments, o == null ? null : o.segments);
    }

    @Override
    public String toString() {
        return "/" + String.join("/", segments);
    }

    public static SegmentedPath fromUrl(String url) {
        if (url == null || url.isEmpty()) {
            return ROOT;
        }

        return new SegmentedPath(0, Arrays.stream(url.split("/")).filter($::isNotEmpty).toArray(String[]::new));
    }

    public static SegmentedPath of(String... segments) {
        return new SegmentedPath(0, segments);
    }

    @Override
    public Iterator<String> iterator() {
        return new ArrayIterable.ArrayIterator<String>(segments);
    }

    public int length() {
        return segments.length;
    }

    public boolean isRoot() {
        return segments.length == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (o == this) return true;

        return o instanceof SegmentedPath p && Arrays.equals(segments, p.segments);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(segments);
    }
}
