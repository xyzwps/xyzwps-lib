package com.xyzwps.website.modules.user.handler;

import com.xyzwps.lib.express.HttpContext;
import com.xyzwps.lib.express.SetCookie;
import com.xyzwps.website.modules.user.payload.LoginBasicPayload;
import jakarta.inject.Singleton;

@Singleton
public class LoginBasicHandler {

    public Object login(HttpContext ctx, LoginBasicPayload payload) {
        var setCookies = ctx.response().cookies();
        setCookies.add(new SetCookie("a", "b").path("/").secure(true));
        setCookies.add(new SetCookie("c", "d").path("/").httpOnly(true));
        return payload;
    }
}
