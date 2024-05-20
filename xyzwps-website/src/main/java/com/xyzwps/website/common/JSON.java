package com.xyzwps.website.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xyzwps.lib.express.jackson.ServerJacksonModule;

public class JSON {

    public static final ObjectMapper OM = new ObjectMapper();

    static {
        OM.registerModule(new ServerJacksonModule());
    }

    public static String stringify(Object obj) {
        try {
            return OM.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Cannot stringify argument into string");
        }
    }

    public static String stringify(Object obj, boolean pretty) {
        if (pretty) {
            try {
                return OM.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new IllegalArgumentException("Cannot stringify argument into string");
            }
        } else {
            return stringify(obj);
        }
    }
}
