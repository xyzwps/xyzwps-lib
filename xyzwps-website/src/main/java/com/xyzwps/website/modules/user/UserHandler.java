package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.website.Person;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class UserHandler {

    public Map<String, Object> getById(HttpContext ctx) {
        return Map.of("msg", "get user");
    }

    public Person create(HttpContext ctx) {
        var req = ctx.request();

        var body = req.body();
        if (body instanceof Person p) {
            return p;
        } else {
            throw new RuntimeException("create user failed");
        }
    }

}
