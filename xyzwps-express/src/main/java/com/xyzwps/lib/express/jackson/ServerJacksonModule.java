package com.xyzwps.lib.express.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpPathVariables;
import com.xyzwps.lib.express.HttpProtocol;
import com.xyzwps.lib.express.HttpSearchParams;

public class ServerJacksonModule extends SimpleModule {

    public ServerJacksonModule() {
        this.addSerializer(HttpHeaders.class, new HttpHeadersSerializer());
        this.addSerializer(HttpProtocol.class, new HttpProtocolSerializer());
        this.addSerializer(HttpSearchParams.class, new HttpSearchParamsSerializer());
        this.addSerializer(HttpPathVariables.class, new HttpPathVariablesSerializer());
    }

}
