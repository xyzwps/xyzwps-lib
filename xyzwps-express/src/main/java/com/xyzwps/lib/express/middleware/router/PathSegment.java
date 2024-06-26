package com.xyzwps.lib.express.middleware.router;

import java.util.regex.Pattern;

import static com.xyzwps.lib.dollar.Dollar.*;

public sealed interface PathSegment {

    static PathSegment from(String segment) {
        if ($.isEmpty(segment)) {
            throw new IllegalArgumentException("Segment string cannot be empty");
        }

        if (segment.startsWith("{")) {
            if (segment.endsWith("}") && segment.length() > 2) {
                var variableName = segment.substring(1, segment.length() - 1);
                if (!VariableSegment.VAR_PATTERN.matcher(variableName).matches()) {
                    throw new IllegalArgumentException("Invalid path variable name '" + variableName + "'");
                }
                return new VariableSegment(variableName); // TODO: 支持正则表达式
            } else {
                throw new IllegalArgumentException("Invalid path variable segment '" + segment + "'");
            }
        }

        if (segment.contains("*")) {
            if (segment.equals("*")) return StarSegment.INSTANCE;
            if (segment.equals("**")) return DoubleStarSegment.INSTANCE;
            throw new IllegalArgumentException("Invalid segment '" + segment + "'");
        }

        if (!PlainSegment.SEG_PATTERN.matcher(segment).matches()) {
            throw new IllegalArgumentException("Invalid path segment '" + segment + "'");
        }
        return new PlainSegment(segment);
    }

    record PlainSegment(String value) implements PathSegment {
        static final Pattern SEG_PATTERN = Pattern.compile("^[A-Za-z_][A-Za-z0-9_-]*");

        public boolean notMatch(String url) {
            return !value.equals(url);
        }

        @Override
        public String toString() {
            return value;
        }
    }

    record VariableSegment(String variableName) implements PathSegment {
        static final Pattern VAR_PATTERN = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*");

        @Override
        public String toString() {
            return "{" + variableName + "}";
        }
    }

    enum StarSegment implements PathSegment {
        INSTANCE;

        @Override
        public String toString() {
            return "*";
        }
    }

    enum DoubleStarSegment implements PathSegment {
        INSTANCE;

        @Override
        public String toString() {
            return "**";
        }
    }
}
