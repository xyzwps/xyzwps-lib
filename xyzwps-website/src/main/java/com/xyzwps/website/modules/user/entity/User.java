package com.xyzwps.website.modules.user.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Long id;
    private String phone;
    private String displayName;
    private String avatar;
    private String password;
    private String email;
    private UserStatus status;
    private Long createdAt;
    private Long updatedAt;
}
