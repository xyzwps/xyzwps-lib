package com.xyzwps.lib.bedrock.lang;

import java.lang.reflect.Type;

public final class DefaultValues {

    public static Object get(Type type) {
        if (type == byte.class) return (byte) 0;
        if (type == short.class) return (short) 0;
        if (type == int.class) return 0;
        if (type == long.class) return 0L;
        if (type == char.class) return (char) 0;
        if (type == float.class) return 0F;
        if (type == double.class) return 0.0;
        if (type == boolean.class) return false;
        return null;
    }

}
