package com.xyzwps.lib.paimon;

public class StringCharReader {

    private final char[] chars;

    private int nextChIndex = 0;
    private int nextChLine = 0;

    public StringCharReader(String source) {
        this.chars = source.toCharArray();
    }

    public Next nextCh() {
        if (nextChIndex < this.chars.length) {
            var ch = chars[nextChIndex];
            if (ch == '\r') {
                if (nextChIndex == this.chars.length - 1) /* 已经是最后一个了 */ {
                    nextChIndex++;
                    return new Next(ch, nextChLine);
                }
                var ch1 = chars[nextChIndex + 1];
                if (ch1 == '\n') {
                    nextChIndex += 2;
                    return new Next('\n', nextChLine++);
                } else {
                    nextChIndex++;
                    return new Next(ch, nextChLine);
                }
            } else if (ch == '\n') {
                nextChIndex++;
                return new Next('\n', nextChLine++);
            } else {
                nextChIndex++;
                return new Next(ch, nextChLine);
            }
        } else {
            return new Next('\0', nextChLine);
        }
    }

    public record Next(char ch, int line) {
        @Override
        public String toString() {
            var str = switch (ch) {
                case '\r' -> "\\r";
                case '\n' -> "\\n";
                case '\t' -> "\\t";
                case '\f' -> "\\f";
                case '\0' -> "EOF";
                case ' ' -> "[_]";
                default -> "" + ch;
            };
            return String.format("<char>: %-4d %s", line, str);
        }
    }
}
