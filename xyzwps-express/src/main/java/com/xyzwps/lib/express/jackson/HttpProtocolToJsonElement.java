package com.xyzwps.lib.express.jackson;

import com.xyzwps.lib.express.HttpProtocol;
import com.xyzwps.lib.json.ToElementConverter;
import com.xyzwps.lib.json.element.JsonElement;
import com.xyzwps.lib.json.element.JsonString;

public class HttpProtocolToJsonElement implements ToElementConverter<HttpProtocol> {

    @Override
    public JsonElement convert(HttpProtocol httpProtocol) {
        return new JsonString(httpProtocol.value);
    }
}
