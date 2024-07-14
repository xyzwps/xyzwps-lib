package com.xyzwps.website.modules.test;


import com.xyzwps.lib.ap.API;
import com.xyzwps.lib.ap.GET;
import com.xyzwps.lib.ap.SearchParam;
import jakarta.inject.Singleton;

@Singleton
@API("/api/test")
public class TestApis {

    @GET("/get-person")
    public Person getPerson(@SearchParam("id") int id) {
        return new Person(id, "张三");
    }
}
