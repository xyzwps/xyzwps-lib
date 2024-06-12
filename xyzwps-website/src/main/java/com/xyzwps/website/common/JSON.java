package com.xyzwps.website.common;

import com.xyzwps.lib.express.jackson.JsonConfigurer;
import com.xyzwps.lib.json.JsonMapper;

public final class JSON {

    public static final JsonMapper JM = new JsonMapper();

    static {
        new JsonConfigurer().accept(JM);
    }

    public static String stringify(Object obj) {
        return JM.stringify(obj);
    }

    public static String stringify(Object obj, boolean pretty) {
        return JM.stringify(obj, pretty);
    }

    private JSON() throws IllegalAccessException {
        throw new IllegalAccessException("??");
    }
}
