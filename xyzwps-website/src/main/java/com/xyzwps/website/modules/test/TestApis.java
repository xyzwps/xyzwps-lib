package com.xyzwps.website.modules.test;


import com.xyzwps.lib.ap.API;
import com.xyzwps.lib.ap.GET;
import com.xyzwps.lib.ap.PathParam;
import com.xyzwps.lib.ap.SearchParam;
import com.xyzwps.lib.express.HttpRequest;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static manifold.collections.api.range.RangeFun.to;

@Singleton
@AllArgsConstructor
@API("/api/test")
public class TestApis {

    private final TestDao testDao;

    @GET("/get-person")
    public Person getPerson(@SearchParam("id") int id) {
        return new Person(id, "张三");
    }

    @GET("/manifold")
    public String manifold() {
        for (var i : 1to 5) {
            System.out.println(i);
        }
        return "Hello, Manifold";
    }

    @GET("/:id")
    public Map<String, Object> pathVar(@PathParam("id") String id, HttpRequest req) {
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
        map.put("date", new Date());
        map.put("localDateTime", LocalDateTime.now());

        return map;
    }
}
