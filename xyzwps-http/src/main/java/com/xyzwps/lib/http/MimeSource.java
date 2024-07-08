package com.xyzwps.lib.http;

public enum MimeSource {
    IANA,
    APACHE,
    NGINX;

    static MimeSource from(String str) {
        if (str == null || str.isEmpty()) return null;
        return valueOf(str);
    }
}
