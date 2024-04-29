package com.xyzwps.website;

import com.xyzwps.website.middleware.MiddlewareModule;
import dagger.Component;

import javax.inject.Singleton;

public class Main {

    @Singleton
    @Component(modules = MiddlewareModule.class)
    interface Entry {
        HttpServerLayer server();
    }

    public static void main(String[] args) {
        Entry entry = DaggerMain_Entry.create();
        entry.server().start(3000);
    }
}
