package com.xyzwps.website.modules.user;

import com.xyzwps.lib.ap.*;
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

import java.util.Map;

@Singleton
@API("/api/users")
@AllArgsConstructor
public class UserApis {

    private final Validator validator;
    private final SimpleAuthenticationService authenticationService;

    @POST("/register/simple/vcode")
    public OK sendRegisterVcode(@Body SendRegisterVcodePayload payload) {
        validator.validate(payload);
        // TODO: 检查是否已经注册
        authenticationService.sendRegisterVcode(payload.getPhone());
        return OK.INSTANCE;
    }

    @POST("/register/simple")
    public OK register(@Body SimpleRegisterPayload payload) {
        validator.validate(payload);
        // TODO: 检查是否已经注册
        authenticationService.register(payload.getPhone(), payload.getVcode());
        return OK.INSTANCE;
    }

    @POST("/login/simple")
    public LoginBasicPayload login(@Body LoginBasicPayload payload, HttpResponse resp) {
        validator.validate(payload);

        var setCookies = resp.cookies();
        setCookies.add(new SetCookie("a", "b").path("/").secure(true));
        setCookies.add(new SetCookie("c", "d").path("/").httpOnly(true));
        return payload;
    }

    @GET("/:id")
    public Map<String, Object> getById(@PathParam("id") int id) {
        return Map.of("id", id);
    }

    @POST("/:id")
    public Map<String, Object> create(@PathParam("id") int id) {
        return Map.of("id", id);
    }
}
