package com.xyzwps.website.modules.user.handler;

import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.website.Person;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class UserHandler {

    public Map<String, Object> getById(HttpRequest req, HttpResponse resp) {
        return Map.of("msg", "get user");
    }

    public Person create(HttpRequest req, HttpResponse resp, Person p) {
        return p;
    }

}
