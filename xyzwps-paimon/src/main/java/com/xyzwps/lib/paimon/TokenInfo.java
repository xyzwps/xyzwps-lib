package com.xyzwps.lib.paimon;

public record TokenInfo(TokenType type, String value, int line) {

    @Override
    public String toString() {
        return String.format("<token>: %-4d %-12s %-3d %s", line, type, value.length(), value);
    }
}
