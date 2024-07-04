package com.xyzwps.lib.express;


import com.xyzwps.lib.dollar.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class UrlPath {

    private final UrlSegment[] segments;

    private final String rawPath;

    private final boolean hasStar2;

    private UrlPath(UrlSegment[] segments, String rawPath) {
        for (int i = 0; i < segments.length - 1; i++) {
            var segment = segments[i];
            if (segment instanceof UrlSegment.Star2) {
                throw new IllegalArgumentException("Invalid UrlPath: ** must be the last segment");
            }
        }
        this.segments = segments;
        this.rawPath = rawPath;
        this.hasStar2 = segments.length > 0 && segments[segments.length - 1] == UrlSegment.Star2.INSTANCE;
    }

    public boolean hasStar2() {
        return hasStar2;
    }

    public boolean isPlain() {
        for (UrlSegment segment : segments) {
            if (!(segment instanceof UrlSegment.Text)) {
                return false;
            }
        }
        return true;
    }

    public int length() {
        return segments.length;
    }

    public String rawPath() {
        return rawPath;
    }

    public sealed interface MatchResult {

        MatchResult MATCHED = new Matched(List.of());

        MatchResult NOT_MATCHED = NotMatched.INSTANCE;

        record Matched(List<Pair<String, String>> pathVariables) implements MatchResult {
        }

        enum NotMatched implements MatchResult {
            INSTANCE
        }
    }

    public MatchResult prefixOf(UrlPath path, int start) {
        if (this.hasStar2) {
            throw new IllegalStateException("Cannot call prefixOf on a UrlPath with **");
        }

        final int length = this.segments.length;

        if (length == 0) {
            return MatchResult.MATCHED;
        }

        if (path.segments.length - start < length) {
            return MatchResult.NOT_MATCHED;
        }

        return match(path, start, length);
    }

    public MatchResult match(UrlPath path, int start) {
        if (this.hasStar2) {
            final int length = this.segments.length - 1;
            if (path.segments.length - start < length) {
                return MatchResult.NOT_MATCHED;
            }

            return match(path, start, length);
        } else {
            if (this.segments.length != path.segments.length - start) {
                return MatchResult.NOT_MATCHED;
            }

            if (this.segments.length == 0) {
                return MatchResult.MATCHED;
            }

            return match(path, start, this.segments.length);
        }
    }

    private MatchResult match(UrlPath path, int start, int length) {
        List<Pair<String, String>> pathVariables = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            var pseg = path.segments[i + start];
            switch (segments[i]) {
                case UrlSegment.Star.INSTANCE -> {
                    // do nothing
                }
                case UrlSegment.Param param -> pathVariables.add(new Pair<>(param.name(), pseg.text()));
                case UrlSegment.Text text -> {
                    if (!text.text().equals(pseg.text())) {
                        return MatchResult.NOT_MATCHED;
                    }
                }
                default ->
                        throw new IllegalStateException("Unknown UrlSegment type: " + segments[i].getClass().getName());
            }
        }
        return new MatchResult.Matched(pathVariables);
    }

    public UrlSegment get(int index) {
        return segments[index];
    }

    public static UrlPath of(String path) {
        if (path == null || path.isEmpty()) {
            return ROOT;
        }

        String[] pathSegments = path.split("/");
        UrlSegment[] segments = new UrlSegment[pathSegments.length];
        int count = 0;
        for (String segment : pathSegments) {
            if (segment.isEmpty()) {
                continue;
            }

            if (UrlSegment.Param.FORMAT.matcher(segment).matches()) {
                segments[count] = new UrlSegment.Param(segment.substring(1));
            } else if (segment.equals("*")) {
                segments[count] = UrlSegment.Star.INSTANCE;
            } else if (segment.equals("**")) {
                segments[count] = UrlSegment.Star2.INSTANCE;
            } else {
                segments[count] = new UrlSegment.Text(segment);
            }
            count++;
        }

        if (count == 0) {
            return ROOT;
        }

        if (count == pathSegments.length) {
            return new UrlPath(segments, path);
        }

        UrlSegment[] results = new UrlSegment[count];
        System.arraycopy(segments, 0, results, 0, count);
        return new UrlPath(results, path);
    }

    private static final UrlSegment[] EMPTY_SEGMENT_ARR = new UrlSegment[0];

    public static final UrlPath ROOT = new UrlPath(EMPTY_SEGMENT_ARR, "/");

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (UrlSegment segment : segments) {
            sb.append("/");
            sb.append(segment);
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj instanceof UrlPath path) {
            if (segments.length != path.segments.length) {
                return false;
            }

            for (int i = 0; i < segments.length; i++) {
                if (!segments[i].equals(path.segments[i])) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) segments);
    }
}
