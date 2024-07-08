package com.xyzwps.website.modules.user.service;

import com.xyzwps.website.modules.sms.model.VcodeScene;
import com.xyzwps.website.modules.sms.service.VcodeService;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

@Singleton
@JBossLog
@AllArgsConstructor
public class SimpleAuthenticationService {

    private final VcodeService vcodeService;

    private final UserService userService;

    public void sendRegisterVcode(String phone) {
        vcodeService.sendVcode(phone, VcodeScene.REGISTER);
    }

    public void register(String phone, String vcode) {
        vcodeService.verifyVcode(phone, VcodeScene.REGISTER, vcode);
        userService.createUserByPhone(phone);
    }

    public void login(String username, String password) {
        System.out.println("login");
    }
}
