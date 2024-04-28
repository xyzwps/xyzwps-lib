package com.xyzwps.lib.express.util;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class ServerJacksonModule extends SimpleModule {

    public ServerJacksonModule() {
        this.addSerializer(MultiValuesMap.class, new MultiValuesMapSerializer());
    }

}
