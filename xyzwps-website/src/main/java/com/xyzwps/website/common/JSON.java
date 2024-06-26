package com.xyzwps.website.common;

import com.xyzwps.lib.express.json.JsonConfigurer;
import com.xyzwps.lib.json.DateTimeConfigurer;
import com.xyzwps.lib.json.JsonMapper;

public final class JSON {

    public static final JsonMapper JM = new JsonMapper();

    static {
        JM.configure(new JsonConfigurer());
        JM.configure(new DateTimeConfigurer());
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
