package com.xyzwps.lib.express.json;

import com.xyzwps.lib.express.HttpPathVariables;
import com.xyzwps.lib.json.ToElementConverter;
import com.xyzwps.lib.json.element.JsonElement;
import com.xyzwps.lib.json.element.JsonObject;

public class HttpPathVariablesToJsonElement implements ToElementConverter<HttpPathVariables> {

    @Override
    public JsonElement convert(HttpPathVariables httpPathVariables) {
        var jsonObject = new JsonObject();
        for (var name : httpPathVariables.names()) {
            jsonObject.put(name, httpPathVariables.get(name));
        }
        return jsonObject;
    }
}
