package com.xyzwps.lib.express.common;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.xyzwps.lib.dollar.Dollar.*;

public final class SegmentedUrl {

    private final UrlSegment[] segments;

    private SegmentedUrl(UrlSegment[] segments) {
        this.segments = Objects.requireNonNull(segments);
    }

    public int length() {
        return segments.length;
    }

    @Override
    public String toString() {
        return "/" + Arrays.stream(segments).map(Object::toString).collect(Collectors.joining("/"));
    }

    private static final UrlSegment[] EMPTY_SEGMENT_ARR = new UrlSegment[0];

    private static final SegmentedUrl ROOT = new SegmentedUrl(EMPTY_SEGMENT_ARR);

    public static SegmentedUrl from(String url) {
        if ($.isEmpty(url)) {
            return ROOT;
        }

        var segments = Arrays.stream(url.split("/"))
                .filter($::isNotEmpty)
                .map(UrlSegment::from)
                .toArray(UrlSegment[]::new);

        for (int i = 0; i < segments.length - 1; i++) {
            var seg = segments[i];
            if (seg instanceof UrlSegment.Star2Segment) {
                throw new IllegalArgumentException("Only the last segment can be " + UrlSegment.Star2Segment.class.getSimpleName());
            }
        }

        return new SegmentedUrl(segments);
    }

    private static final String[] EMPTY_SRT_ARR = new String[0];

    public static String[] urlToStringSegments(String url) {
        if ($.isEmpty(url)) {
            return EMPTY_SRT_ARR;
        }

        return Arrays.stream(url.split("/"))
                .filter($::isNotEmpty)
                .toArray(String[]::new);
    }

    public boolean match(String[] url, int matchStart) {
        if (segments[segments.length - 1] instanceof UrlSegment.Star2Segment) {
            /*
             * 0) this     /user/{id}/**
             * 1) url  /aaa/user/1234     (1) match
             * 2) url  /a/b/user/1234/ps  (2) match
             * 3) url  /aaa/user          (1) not match
             *
             */
            if (url.length - matchStart < segments.length - 1) {
                return false; // handle 3)
            }

            for (int i = 0; i < segments.length - 1; i++) {
                var str = url[i + matchStart];
                if (segments[i] instanceof UrlSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        } else {
            /*
             * 0) this     /user/{id}
             * 1) url  /aaa/user/1234     (1) match
             * 2) url  /a/b/user/1234/ps  (2) not match
             * 3) url  /aaa/user          (1) not match
             */
            if (url.length - matchStart != segments.length) {
                return false; // handle 2) and 3)
            }

            for (int i = 0; i < segments.length; i++) {
                var str = url[i + matchStart];
                if (segments[i] instanceof UrlSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isPrefixOf(String[] url, int matchStart) {
        if (segments[segments.length - 1] instanceof UrlSegment.Star2Segment) {
            /*
             * 0) this     /user/{id}/**
             * 1) url  /aaa/user/123    (1) true
             * 2) url  /a/c/user/123/ps (2) true
             * 3) url  /abc/user        (1) false
             */
            if (url.length - matchStart < segments.length - 1) {
                return false; // handle 3)
            }

            for (int i = 0; i < segments.length - 1; i++) {
                var str = url[i + matchStart];
                if (segments[i] instanceof UrlSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        } else {
            /*
             * 0) this     /user/{id}
             * 1) url  /aaa/user/1234     (1) match
             * 2) url  /a/b/user/1234/ps  (2) match
             * 3) url  /aaa/user          (1) not match
             */
            if (url.length - matchStart < segments.length) {
                return false; // handle 3)
            }

            for (int i = 0; i < segments.length; i++) {
                var str = url[i + matchStart];
                if (segments[i] instanceof UrlSegment.PlainSegment plain && plain.notMatch(str)) {
                    return false;
                }
            }
            return true;
        }
    }
}
