package com.xyzwps.website.filter;

import com.xyzwps.lib.bedrock.Function3;
import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.filter.JsonParser;
import jakarta.inject.Singleton;

@Singleton
public class JsonHandlerFactory {

    private final JsonParser json;

    public JsonHandlerFactory(JsonParser json) {
        this.json = json;
    }

    public <T> Filter create(Class<T> tClass, Function3<HttpRequest, HttpResponse, T, Object> consumer) {
        JsonHandler jsonHandler = (req, resp) -> {
            var payload = req.body();
            if (tClass.isInstance(payload)) {
                var body = tClass.cast(payload);
                return consumer.apply(req, resp, body);
            } else {
                throw new RuntimeException();
            }
        };
        return json.json(tClass).andThen(jsonHandler.toFilter());
    }
}
