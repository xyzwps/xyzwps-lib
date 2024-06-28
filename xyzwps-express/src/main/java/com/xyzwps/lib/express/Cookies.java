package com.xyzwps.lib.express;

import java.util.List;
import java.util.Map;

/**
 * Represents a collection of cookies in HTTP request. Read-only.
 */
public final class Cookies {

    private final Map<String, Cookie> cookies;

    public static final Cookies EMPTY = new Cookies(null);

    /**
     * Construct a new instance with a list of cookies.
     *
     * @param cookieList list of cookies; null tolerated
     */
    @SuppressWarnings("unchecked")
    public Cookies(List<Cookie> cookieList) {
        if (cookieList == null || cookieList.isEmpty()) {
            this.cookies = null;
        } else {
            final int size = cookieList.size();
            Map.Entry<String, Cookie>[] entries = new Map.Entry[size];
            for (int i = 0; i < size; i++) {
                entries[i] = Map.entry(cookieList.get(i).name(), cookieList.get(i));
            }
            this.cookies = Map.ofEntries(entries);
        }
    }


    /**
     * Get a cookie by name.
     *
     * @param name cookie name; null and empty tolerated
     * @return cookie; null if not found
     */
    public Cookie get(String name) {
        if (name == null || name.isBlank() || cookies == null) {
            return null;
        }
        return cookies.get(name);
    }

}
