package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.filter.SimpleRouter;
import com.xyzwps.website.Person;
import com.xyzwps.website.filter.JsonHandlerFactory;
import com.xyzwps.website.filter.LogRequestCostFilter;
import com.xyzwps.website.modules.user.handler.LoginBasicHandler;
import com.xyzwps.website.modules.user.handler.UserHandler;
import com.xyzwps.website.modules.user.payload.LoginBasicPayload;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class UserSimpleRouter extends SimpleRouter {

    private static final Logger log = Logger.getLogger(LogRequestCostFilter.class);

    public UserSimpleRouter(UserHandler users, JsonHandlerFactory json, LoginBasicHandler basicLogin) {

        this
                .post("/login/basic", json.create(LoginBasicPayload.class, basicLogin::login))
                .get("/{id}", users::getById)
                .use((req, resp, next) -> {
                    log.infof(" > ready to get posts");
                    next.next(req, resp);
                })
                .post("/{id}", json.create(Person.class, users::create));
    }
}
