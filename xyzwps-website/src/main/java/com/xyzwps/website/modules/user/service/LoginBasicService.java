package com.xyzwps.website.modules.user.service;

import com.xyzwps.website.db.MainDatabase;
import jakarta.inject.Singleton;

@Singleton
public class LoginBasicService {

    private final MainDatabase maindb;

    public LoginBasicService(MainDatabase maindb) {
        this.maindb = maindb;
    }

    public void login(String username, String password) {



        System.out.println("login");
    }

}
