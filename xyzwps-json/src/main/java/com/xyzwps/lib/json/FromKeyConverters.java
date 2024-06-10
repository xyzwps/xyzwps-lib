package com.xyzwps.lib.json;

public final class FromKeyConverters {

    public static final FromKeyConverter<String> KEY_TO_STRING = key -> key;

    private FromKeyConverters() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }

}
