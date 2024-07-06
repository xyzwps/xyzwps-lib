package com.xyzwps.website.modules.user.service;

import com.xyzwps.website.db.MainDatabase;
import com.xyzwps.website.modules.user.dao.UserDao;
import com.xyzwps.website.modules.user.entity.User;
import com.xyzwps.website.modules.user.entity.UserStatus;
import jakarta.inject.Singleton;
import lombok.AllArgsConstructor;
import lombok.extern.jbosslog.JBossLog;

import java.time.Instant;

@Singleton
@AllArgsConstructor
@JBossLog
public class UserService {

    private final MainDatabase mainDb;

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

        var id = mainDb.tx((tx) -> {
            var dao = tx.createDao(UserDao.class);
            return dao.insert(newUser);
        });
        log.infof("create user: %d", id);
    }
}
