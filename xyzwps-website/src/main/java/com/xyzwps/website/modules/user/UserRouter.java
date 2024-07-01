package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.middleware.router.NestRouter;
import com.xyzwps.website.Person;
import com.xyzwps.website.middleware.JsonHandlerFactory;
import com.xyzwps.website.middleware.LogRequestCostMiddleware;
import com.xyzwps.website.modules.user.handler.LoginBasicHandler;
import com.xyzwps.website.modules.user.handler.UserHandler;
import com.xyzwps.website.modules.user.payload.LoginBasicPayload;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.function.Consumer;

@Singleton
public class UserRouter implements Consumer<NestRouter> {

    private static final Logger log = Logger.getLogger(LogRequestCostMiddleware.class);

    private final UserHandler users;

    private final JsonHandlerFactory json;

    private final LoginBasicHandler basicLogin;

    public UserRouter(UserHandler users, JsonHandlerFactory json, LoginBasicHandler basicLogin) {
        this.json = json;
        this.users = users;
        this.basicLogin = basicLogin;
    }

    @Override
    public void accept(NestRouter nestRouter) {
        nestRouter
                .post("/login/basic", json.create(LoginBasicPayload.class, basicLogin::login))
                .get("/{id}", users::getById)
                .use((ctx) -> {
                    log.infof(" > ready to get posts");
                    ctx.next();
                })
                .post("/{id}", json.create(Person.class, users::create));
    }
}
