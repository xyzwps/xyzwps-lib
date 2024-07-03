package com.xyzwps.website.modules.user.handler;

import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.SetCookie;
import com.xyzwps.website.modules.user.payload.LoginBasicPayload;
import jakarta.inject.Singleton;

@Singleton
public class LoginBasicHandler {

    public Object login(HttpRequest req, HttpResponse resp, LoginBasicPayload payload) {
        var setCookies = resp.cookies();
        setCookies.add(new SetCookie("a", "b").path("/").secure(true));
        setCookies.add(new SetCookie("c", "d").path("/").httpOnly(true));
        return payload;
    }
}
