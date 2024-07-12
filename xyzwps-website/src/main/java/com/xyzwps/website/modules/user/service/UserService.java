package com.xyzwps.website.modules.user.service;

import com.xyzwps.website.modules.user.dao.UserDao;
import com.xyzwps.website.modules.user.entity.User;
import com.xyzwps.website.modules.user.entity.UserStatus;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
@Singleton
@AllArgsConstructor
public class UserService {

    private final UserDao userDao;

    public void createUserByPhone(String phone) {
        var newUser = User.builder()
                .phone(phone)
                .displayName("xx")
                .avatar("")
                .password(null)
                .email(null)
                .status(UserStatus.ACTIVE)
                .createTime(Instant.now())
                .updateTime(Instant.now())
                .build();
        var id = userDao.insert(newUser);
        log.info("create user: {}", id);
    }
}
