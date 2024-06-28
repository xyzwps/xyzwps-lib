package com.xyzwps.website.modules.user;

import com.xyzwps.lib.express.middleware.JsonParser;
import com.xyzwps.lib.express.middleware.router.NestRouter;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.Person;
import com.xyzwps.website.middleware.LogRequestCostMiddleware;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.function.Consumer;

@Singleton
public class UserRouter implements Consumer<NestRouter> {

    private static final Logger log = Logger.getLogger(LogRequestCostMiddleware.class);

    private final JsonParser json = new JsonParser(JSON.JM);

    private final UserHandler users;

    public UserRouter(UserHandler users) {
        this.users = users;
    }

    @Override
    public void accept(NestRouter nestRouter) {
        nestRouter
                .get("/{id}", users::getById)
                .use((ctx) -> {
                    log.infof(" > ready to get posts");
                    ctx.next();
                })
                .post("/{id}", json.json(Person.class), users::create);
    }
}
