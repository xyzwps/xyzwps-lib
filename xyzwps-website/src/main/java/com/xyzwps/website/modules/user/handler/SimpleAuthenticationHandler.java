package com.xyzwps.website.modules.user.handler;

import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.SetCookie;
import com.xyzwps.website.common.OK;
import com.xyzwps.website.modules.user.payload.LoginBasicPayload;
import com.xyzwps.website.modules.user.payload.SendRegisterVcodePayload;
import com.xyzwps.website.modules.user.payload.SimpleRegisterPayload;
import com.xyzwps.website.modules.user.service.SimpleAuthenticationService;
import io.avaje.validation.Validator;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;

@Singleton
@AllArgsConstructor
public class SimpleAuthenticationHandler {

    private final Validator validator;
    private final SimpleAuthenticationService authenticationService;


    public Object sendRegisterVcode(HttpRequest req, HttpResponse resp, SendRegisterVcodePayload payload) {
        validator.validate(payload);
        authenticationService.sendRegisterVcode(payload.getPhone());
        return OK.INSTANCE;
    }

    public Object register(HttpRequest req, HttpResponse resp, SimpleRegisterPayload payload) {
        validator.validate(payload);
        authenticationService.register(payload.getPhone(), payload.getVcode());
        return OK.INSTANCE;
    }

    public Object login(HttpRequest req, HttpResponse resp, LoginBasicPayload payload) {
        validator.validate(payload);

        var setCookies = resp.cookies();
        setCookies.add(new SetCookie("a", "b").path("/").secure(true));
        setCookies.add(new SetCookie("c", "d").path("/").httpOnly(true));
        return payload;
    }
}
