package com.xyzwps.lib.express.commons;

import com.xyzwps.lib.express.Cookie;
import com.xyzwps.lib.express.Cookies;

import java.net.HttpCookie;

import static com.xyzwps.lib.dollar.Dollar.$;

public final class SimpleCookie implements Cookie {

    private final HttpCookie cookie;

    private SimpleCookie(HttpCookie cookie) {
        this.cookie = cookie;
    }

    public String name() {
        return cookie.getName();
    }

    public String value() {
        return cookie.getValue();
    }

    public static Cookies from(String str) {
        if (str == null || str.isBlank()) {
            return Cookies.EMPTY;
        } else {
            var cookies = HttpCookie.parse(str);
            return new Cookies($.map(cookies, SimpleCookie::new));
        }
    }
}
