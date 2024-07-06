package com.xyzwps.website.modules.sms.service;

import com.xyzwps.website.modules.sms.model.VcodeScene;
import jakarta.inject.Singleton;

@Singleton
public class VcodeService {

    public void sendVcode(String phone, VcodeScene scene) {
        // TODO: 发送频率流速控制等
        System.out.println("send vcode to " + phone);
    }

    public boolean verifyVcode(String phone, VcodeScene scene, String vcode) {
        // TODO: 验证码有效期等
        return true;
    }
}
