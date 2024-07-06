package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.Handler;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.Person;
import com.xyzwps.website.filter.JsonHandlerFactory;
import com.xyzwps.website.modules.user.handler.SimpleAuthenticationHandler;
import com.xyzwps.website.modules.user.handler.UserHandler;
import com.xyzwps.website.modules.user.payload.LoginBasicPayload;
import com.xyzwps.website.modules.user.payload.SendRegisterVcodePayload;
import com.xyzwps.website.modules.user.payload.SimpleRegisterPayload;
import jakarta.inject.Singleton;

@Singleton
public class UserRouter extends Router.Nest {

    public UserRouter(UserHandler users, JsonHandlerFactory json, SimpleAuthenticationHandler simpleAuth) {
        this
                .post("/register/simple/vcode", json.create(SendRegisterVcodePayload.class, simpleAuth::sendRegisterVcode))
                .post("/register/simple", json.create(SimpleRegisterPayload.class, simpleAuth::register))
                .post("/login/simple", json.create(LoginBasicPayload.class, simpleAuth::login))
                .get("/{id}", ((Handler) users::getById).toFilter())
                .post("/{id}", json.create(Person.class, users::create));
    }
}
