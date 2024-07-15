package com.xyzwps.website.modules.user.handler;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.SetCookie;
import com.xyzwps.website.common.JSON;
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


    public void sendRegisterVcode(HttpRequest req, HttpResponse resp, Filter.Next next) {
        var payload = req.json(SendRegisterVcodePayload.class, JSON.JM);
        // TODO: 检查是否已经注册
        validator.validate(payload);
        authenticationService.sendRegisterVcode(payload.getPhone());
        resp.sendJson(OK.INSTANCE);
    }

    public void register(HttpRequest req, HttpResponse resp, Filter.Next next) {
        var payload = req.json(SimpleRegisterPayload.class, JSON.JM);
        // TODO: 检查是否已经注册
        validator.validate(payload);
        authenticationService.register(payload.getPhone(), payload.getVcode());
        resp.sendJson(OK.INSTANCE);
    }

    public void login(HttpRequest req, HttpResponse resp, Filter.Next next) {
        var payload = req.json(LoginBasicPayload.class, JSON.JM);
        validator.validate(payload);

        var setCookies = resp.cookies();
        setCookies.add(new SetCookie("a", "b").path("/").secure(true));
        setCookies.add(new SetCookie("c", "d").path("/").httpOnly(true));
        resp.sendJson(payload);
    }
}
