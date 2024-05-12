package com.xyzwps.lib.express.util;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xyzwps.lib.express.HttpHeaders;

public class ServerJacksonModule extends SimpleModule {

    public ServerJacksonModule() {
        this.addSerializer(HttpHeaders.class, new HttpHeadersSerializer());
    }

}
