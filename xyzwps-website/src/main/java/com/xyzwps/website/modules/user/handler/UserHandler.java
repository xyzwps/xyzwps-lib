package com.xyzwps.website.modules.user.handler;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.Handler;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.website.Person;
import com.xyzwps.website.filter.JsonHandler;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class UserHandler {

    public JsonHandler getById() {
        return (req, resp) -> Map.of("msg", "get user");
    }

    public JsonHandler create() {
        return (req, resp) -> req.json(Person.class);
    }

}
