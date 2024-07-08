package com.xyzwps.lib.jdbc.model;

import com.xyzwps.lib.jdbc.Column;

import java.time.LocalDateTime;

public record PlayableCharacter(
        @Column long uid,
        String name,
        @Column(mapper = Region.RegionMapper.class)
        Region region,
        int age,
        boolean useSword,
        Gender gender,
        String remark,
        LocalDateTime createdAt
) {
}