package com.xyzwps.website.modules.user.entity;

/**
 * User account status.
 */
public enum UserStatus {
    /**
     * User account is active.
     */
    ACTIVE,
    /**
     * User account is locked by administrator.
     */
    LOCKED,
    /**
     * User account is disabled by administrator.
     */
    DISABLED,
    /**
     * User account is closed by him/her-self.
     */
    CLOSED,
}
