package com.xyzwps.website.modules.action;

import com.xyzwps.lib.express.SetCookie;
import com.xyzwps.lib.express.middleware.router.NestRouter;
import com.xyzwps.website.middleware.JsonHandlerFactory;
import jakarta.inject.Singleton;

import java.util.function.Consumer;

@Singleton
public class ActionRouter implements Consumer<NestRouter> {

    private final JsonHandlerFactory json;

    public ActionRouter(JsonHandlerFactory json) {
        this.json = json;
    }

    @Override
    public void accept(NestRouter router) {
        router.post("/login", json.create(LoginPayload.class, (ctx, loginPayload) -> {
            var setCookies = ctx.response().cookies();
            setCookies.add(new SetCookie("a", "b").path("/").secure(true));
            setCookies.add(new SetCookie("c", "d").path("/").httpOnly(true));
            return loginPayload;
        }));
    }
}

