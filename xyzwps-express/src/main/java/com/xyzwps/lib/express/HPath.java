package com.xyzwps.lib.express;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.xyzwps.lib.dollar.Dollar.*;

/**
 * Http path
 */
public final class HPath {

    private final PathSegment[] segments;

    private HPath(PathSegment[] segments) {
        this.segments = Objects.requireNonNull(segments);
    }

    public int length() {
        return segments.length;
    }

    @Override
    public String toString() {
        return "/" + Arrays.stream(segments).map(Object::toString).collect(Collectors.joining("/"));
    }

    private static final PathSegment[] EMPTY_SEGMENT_ARR = new PathSegment[0];

    private static final HPath ROOT = new HPath(EMPTY_SEGMENT_ARR);

    public static HPath from(String path) {
        if ($.isEmpty(path)) {
            return ROOT;
        }

        var segments = Arrays.stream(path.split("/"))
                .filter($::isNotEmpty)
                .map(PathSegment::from)
                .toArray(PathSegment[]::new);

        for (int i = 0; i < segments.length - 1; i++) {
            var seg = segments[i];
            if (seg instanceof PathSegment.Star2Segment) {
                throw new IllegalArgumentException("Only the last segment can be " + PathSegment.Star2Segment.class.getSimpleName());
            }
        }

        return new HPath(segments);
    }

    private static final String[] EMPTY_SRT_ARR = new String[0];

    public static String[] pathToSegmentStrings(String path) {
        if ($.isEmpty(path)) {
            return EMPTY_SRT_ARR;
        }

        return Arrays.stream(path.split("/"))
                .filter($::isNotEmpty)
                .toArray(String[]::new);
    }


    // TODO: 好好测试
    public boolean match(String[] path, int matchStart) {
        if (segments.length == 0) {
            return matchStart == path.length;
        }

        if (segments[segments.length - 1] instanceof PathSegment.Star2Segment) {
            /*
             * 0) this      /user/{id}/**
             * 1) path  /aaa/user/1234     (1) match
             * 2) path  /a/b/user/1234/ps  (2) match
             * 3) path  /aaa/user          (1) not match
             *
             */
            if (path.length - matchStart < segments.length - 1) {
                return false; // handle 3)
            }

            for (int i = 0; i < segments.length - 1; i++) {
                var str = path[i + matchStart];
                if (segments[i] instanceof PathSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        } else {
            /*
             * 0) this      /user/{id}
             * 1) path  /aaa/user/1234     (1) match
             * 2) path  /a/b/user/1234/ps  (2) not match
             * 3) path  /aaa/user          (1) not match
             */
            if (path.length - matchStart != segments.length) {
                return false; // handle 2) and 3)
            }

            for (int i = 0; i < segments.length; i++) {
                var str = path[i + matchStart];
                if (segments[i] instanceof PathSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isPrefixOf(String[] path, int matchStart) {
        if (segments[segments.length - 1] instanceof PathSegment.Star2Segment) {
            /*
             * 0) this      /user/{id}/**
             * 1) path  /aaa/user/123    (1) true
             * 2) path  /a/c/user/123/ps (2) true
             * 3) path  /abc/user        (1) false
             */
            if (path.length - matchStart < segments.length - 1) {
                return false; // handle 3)
            }

            for (int i = 0; i < segments.length - 1; i++) {
                var str = path[i + matchStart];
                if (segments[i] instanceof PathSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        } else {
            /*
             * 0) this      /user/{id}
             * 1) path  /aaa/user/1234     (1) match
             * 2) path  /a/b/user/1234/ps  (2) match
             * 3) path  /aaa/user          (1) not match
             */
            if (path.length - matchStart < segments.length) {
                return false; // handle 3)
            }

            for (int i = 0; i < segments.length; i++) {
                var str = path[i + matchStart];
                if (segments[i] instanceof PathSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        }
    }
}
