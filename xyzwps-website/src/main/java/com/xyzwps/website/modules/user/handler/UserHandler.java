package com.xyzwps.website.modules.user.handler;

import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.website.Person;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class UserHandler {

    public Map<String, Object> getById(HttpContext ctx) {
        return Map.of("msg", "get user");
    }

    public Person create(HttpContext ctx, Person p) {
        return p;
    }

}
