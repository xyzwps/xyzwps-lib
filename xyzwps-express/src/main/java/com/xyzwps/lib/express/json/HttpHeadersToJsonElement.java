package com.xyzwps.lib.express.json;

import com.xyzwps.lib.express.HttpHeaders;
import com.xyzwps.lib.json.ToElementConverter;
import com.xyzwps.lib.json.element.*;

public class HttpHeadersToJsonElement implements ToElementConverter<HttpHeaders> {

    @Override
    public JsonElement convert(HttpHeaders it) {
        var jo = new JsonObject();
        it.forEach((name, values) -> {
            if (values == null || values.isEmpty()) {
                jo.put(name, JsonNull.INSTANCE);
            } else if (values.size() == 1) {
                jo.put(name, values.getFirst());
            } else {
                var jsonArray = new JsonArray();
                values.forEach(v -> jsonArray.add(new JsonString(v)));
                jo.put(name, jsonArray);
            }
        });
        return jo;
    }
}
