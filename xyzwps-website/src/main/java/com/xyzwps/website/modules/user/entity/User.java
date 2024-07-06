package com.xyzwps.website.modules.user.entity;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String phone;
    private String displayName;
    private String avatar;
    private String password;
    private String email;
    private UserStatus status;
    private Instant createTime;
    private Instant updateTime;
}
