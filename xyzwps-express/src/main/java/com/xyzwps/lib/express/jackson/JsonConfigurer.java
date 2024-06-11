package com.xyzwps.lib.express.jackson;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.express.HttpPathVariables;
import com.xyzwps.lib.express.HttpProtocol;
import com.xyzwps.lib.express.HttpSearchParams;
import com.xyzwps.lib.json.JsonMapper;

import java.util.function.Consumer;

public final class JsonConfigurer implements Consumer<JsonMapper> {

    @Override
    public void accept(JsonMapper jm) {
        jm.addToElementConverter(HttpHeaders.class, new HttpHeadersToJsonElement());
        jm.addToElementConverter(HttpProtocol.class, new HttpProtocolToJsonElement());
        jm.addToElementConverter(HttpSearchParams.class, new HttpSearchParamsToJsonElement());
        jm.addToElementConverter(HttpPathVariables.class, new HttpPathVariablesToJsonElement());
    }
}
