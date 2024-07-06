package com.xyzwps.website.common;

public record OK(boolean ok) {
    public static final OK INSTANCE = new OK(true);
}
