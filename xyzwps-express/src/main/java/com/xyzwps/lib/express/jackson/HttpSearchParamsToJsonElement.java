package com.xyzwps.lib.express.jackson;

import com.xyzwps.lib.express.HttpSearchParams;
import com.xyzwps.lib.json.ToElementConverter;
import com.xyzwps.lib.json.element.*;

public class HttpSearchParamsToJsonElement implements ToElementConverter<HttpSearchParams> {

    @Override
    public JsonElement convert(HttpSearchParams httpSearchParams) {
        var jsonObject = new JsonObject();
        for (var name : httpSearchParams.names()) {
            var values = httpSearchParams.getAll(name);
            if (values == null || values.isEmpty()) {
                jsonObject.put(name, JsonNull.INSTANCE);
            } else if (values.size() == 1) {
                jsonObject.put(name, values.getFirst());
            } else {
                var jsonArray = new JsonArray();
                values.forEach(v -> jsonArray.add(new JsonString(v)));
                jsonObject.put(name, jsonArray);
            }
        }
        return jsonObject;
    }
}
