package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.modules.user.handler.SimpleAuthenticationHandler;
import com.xyzwps.website.modules.user.handler.UserHandler;
import jakarta.inject.Singleton;

@Singleton
public class UserRouter extends Router.Nest {

    public UserRouter(UserHandler users, SimpleAuthenticationHandler simpleAuth) {
        this
                .post("/register/simple/vcode", simpleAuth::sendRegisterVcode)
                .post("/register/simple", simpleAuth::register)
                .post("/login/simple", simpleAuth::login)
                .get("/{id}", users.getById().toFilter())
                .post("/{id}", users.create().toFilter());
    }
}
