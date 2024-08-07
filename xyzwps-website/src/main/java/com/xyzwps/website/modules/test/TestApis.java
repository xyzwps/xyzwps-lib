package com.xyzwps.website.modules.test;

import com.xyzwps.lib.ap.*;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.filter.BasicAuth;
import com.xyzwps.lib.express.filter.NoopFilter;
import com.xyzwps.website.conf.Configurations;
import io.avaje.validation.ValidMethod;
import io.avaje.validation.constraints.NotEmpty;
import io.avaje.validation.constraints.Positive;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Singleton
@AllArgsConstructor
@API("/api/test")
@Slf4j
public class TestApis {

    private final TestDao testDao;

    private final Configurations conf;

    @ValidMethod
    @GET("/get-person")
    public Person getPerson(@SearchParam("id") @Positive(message = "Search param id should be positive.") int id) {
        return new Person(id, "张三");
    }

    @GET("/conf")
    public Map<String, Object> conf() {
        var map = new HashMap<String, Object>();
        map.put("name", conf.getAppName());
        return map;
    }

    @GET(value = "/auth", filters = {NoopFilter.class, BasicAuth.class})
    public Map<String, Object> auth() {
        return Map.of("Hello", "World");
    }

    @GET("/:id")
    public Map<String, Object> pathVar(@PathParam("id") @NotEmpty(message = "Path param id should not be empty.") String id, HttpRequest req) {
        req.attribute("haha", "ha\nha");

        var map = new HashMap<String, Object>();

        map.put("jdbc", testDao.count());
        map.put("protocol", req.protocol());
        map.put("method", req.method());
        map.put("path", req.path());
        map.put("headers", req.headers());
        map.put("searchParams", req.searchParams());
        map.put("attributes", req.attributes());
        map.put("pathVars", req.pathVariables());
        map.put("id", id);
        map.put("date", new Date());
        map.put("localDateTime", LocalDateTime.now());

        return map;
    }
}
