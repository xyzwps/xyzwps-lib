package com.xyzwps.website.middleware;

import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.lib.express.HttpMiddleware;
import com.xyzwps.lib.express.middleware.JsonParser;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.function.BiFunction;

@Singleton
public class JsonHandlerFactory {

    private final JsonParser json;

    public JsonHandlerFactory(JsonParser json) {
        this.json = json;
    }

    public <T> HttpMiddleware create(Class<T> tClass, BiFunction<HttpContext, T, Object> consumer) {
        JsonHandler jsonHandler = context -> {
            var payload = context.request().body();
            if (tClass.isInstance(payload)) {
                return consumer.apply(context, tClass.cast(payload));
            } else {
                throw new RuntimeException();
            }
        };
        return HttpMiddleware.compose(List.of(json.json(tClass), jsonHandler));
    }
}
