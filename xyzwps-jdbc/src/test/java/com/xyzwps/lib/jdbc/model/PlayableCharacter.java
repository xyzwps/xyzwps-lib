package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.jdbc.Column;

import java.time.LocalDateTime;

public record PlayableCharacter(
        @Column(name = "uid") long id,
        String name,
        int age,
        boolean useSword,
        Gender gender,
        String remark,
        LocalDateTime createdAt
) {
}