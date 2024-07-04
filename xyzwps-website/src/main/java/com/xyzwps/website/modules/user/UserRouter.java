package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.Handler;
import com.xyzwps.lib.express.filter.TrieRouter;
import com.xyzwps.website.Person;
import com.xyzwps.website.filter.JsonHandlerFactory;
import com.xyzwps.website.modules.user.handler.LoginBasicHandler;
import com.xyzwps.website.modules.user.handler.UserHandler;
import com.xyzwps.website.modules.user.payload.LoginBasicPayload;
import jakarta.inject.Singleton;

@Singleton
public class UserRouter extends TrieRouter.Nest {

    public UserRouter(UserHandler users, JsonHandlerFactory json, LoginBasicHandler basicLogin) {
        this
                .post("/login/basic", json.create(LoginBasicPayload.class, basicLogin::login))
                .get("/{id}", ((Handler) users::getById).toFilter())
//                .use((req, resp, next) -> {
//                    log.infof(" > ready to get posts");
//                    next.next(req, resp);
//                })
                .post("/{id}", json.create(Person.class, users::create));
    }
}
