package com.xyzwps.lib.express;

import java.util.regex.Pattern;

public sealed interface UrlSegment {

    String text();

    record Text(String text) implements UrlSegment {
    }

    // TODO: 支持正则表达式
    record Param(String name) implements UrlSegment {
        static Pattern FORMAT = Pattern.compile("^:[a-zA-Z_][a-zA-Z0-9_]*$");

        @Override
        public String text() {
            return ":" + name;
        }
    }

    enum Star implements UrlSegment {
        INSTANCE;

        @Override
        public String text() {
            return "*";
        }
    }

    enum Star2 implements UrlSegment {
        INSTANCE;

        @Override
        public String text() {
            return "**";
        }
    }
}
