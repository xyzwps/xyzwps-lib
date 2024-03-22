package com.xyzwps.lib.paimon;

public class CharReader {

    private final char[] chars;

    private int nextChIndex = 0;
    private int nextChLine = 0;

    public CharReader(String source) {
        this.chars = source.toCharArray();
    }

    public Char nextCh() {
        if (nextChIndex < this.chars.length) {
            var ch = chars[nextChIndex];
            if (ch == '\r') {
                if (nextChIndex == this.chars.length - 1) /* 已经是最后一个了 */ {
                    nextChIndex++;
                    return new Char(ch, nextChLine);
                }
                var ch1 = chars[nextChIndex + 1];
                if (ch1 == '\n') {
                    nextChIndex += 2;
                    return new Char('\n', nextChLine++);
                } else {
                    nextChIndex++;
                    return new Char(ch, nextChLine);
                }
            } else if (ch == '\n') {
                nextChIndex++;
                return new Char('\n', nextChLine++);
            } else {
                nextChIndex++;
                return new Char(ch, nextChLine);
            }
        } else {
            return new Char('\0', nextChLine);
        }
    }

    public record Char(char ch, int line) {
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

        public String toCode() {
            var c = switch (ch) {
                case '\r' -> "\\r";
                case '\n' -> "\\n";
                case '\t' -> "\\t";
                case '\f' -> "\\f";
                case '\0' -> "\\0";
                default -> "" + ch;
            };
            return String.format("new Char('%s', %d)", c, line);
        }
    }
}
