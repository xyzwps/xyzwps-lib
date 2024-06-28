package com.xyzwps.lib.express;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static com.xyzwps.lib.bedrock.DateTimeUtils.RFC1123;

public final class SetCookie {

    private final String name;
    private final String value;
    private Instant date;
    private String domain;
    private boolean secure;
    private boolean httpOnly;
    private String path;
    private Integer maxAge;
    private SameSite sameSite;

    SetCookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String name() {
        return name;
    }

    public String value() {
        return value;
    }

    public Instant date() {
        return date;
    }

    public SetCookie date(Instant date) {
        this.date = date;
        return this;
    }

    public SetCookie date(LocalDateTime localDateTime) {
        this.date = localDateTime == null ? null : ZonedDateTime.of(localDateTime, ZoneId.systemDefault()).toInstant();
        return this;
    }

    public String domain() {
        return domain;
    }

    public SetCookie domain(String domain) {
        this.domain = domain;
        return this;
    }

    public boolean secure() {
        return secure;
    }

    public SetCookie secure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public boolean httpOnly() {
        return httpOnly;
    }

    public SetCookie httpOnly(boolean httpOnly) {
        this.httpOnly = httpOnly;
        return this;
    }

    public String path() {
        return path;
    }

    public SetCookie path(String path) {
        this.path = path;
        return this;
    }

    public Integer maxAge() {
        return maxAge;
    }

    public SetCookie maxAge(Integer maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public SameSite sameSite() {
        return sameSite;
    }

    public SetCookie sameSite(SameSite sameSite) {
        this.sameSite = sameSite;
        return this;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append(name).append('=').append(value);
        if (date != null) {
            sb.append("; Expires=").append(RFC1123.instantToString(date));
        }
        if (domain != null) {
            sb.append("; Domain=").append(domain);
        }
        if (secure) {
            sb.append("; Secure");
        }
        if (httpOnly) {
            sb.append("; HttpOnly");
        }
        if (path != null) {
            sb.append("; Path=").append(path);
        }
        if (maxAge != null) {
            sb.append("; Max-Age=").append(maxAge);
        }
        if (sameSite != null) {
            sb.append("; SameSite=").append(sameSite);
        }
        return sb.toString();
    }
}
