package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.express.*;

import java.util.Base64;
import java.util.function.BiPredicate;

public final class BasicAuth implements Filter {

    private final BiPredicate<String, String> usernamePasswordChecker;

    public BasicAuth(BiPredicate<String, String> usernamePasswordChecker) {
        this.usernamePasswordChecker = Args.notNull(usernamePasswordChecker, "Basic auth checker cannot be null");
    }

    private static void unauthorized(HttpResponse resp) {
        resp.status(HttpStatus.UNAUTHORIZED);
        resp.headers().set("WWW-Authenticate", "Basic realm=\"Invalid credential.\"");
    }

    @Override
    public void filter(HttpRequest req, HttpResponse resp, Next next) {
        var value = req.header(HttpHeaders.AUTHORIZATION);
        if (value == null) {
            unauthorized(resp);
            return;
        }

        if (value.startsWith("Basic ")) {
            var credential = value.substring(6);
            var segments = new String(Base64.getMimeDecoder().decode(credential)).split(":", 2);
            if (segments.length != 2) {
                unauthorized(resp);
                return;
            }

            String username = segments[0], password = segments[1];
            if (usernamePasswordChecker.test(username, password)) {
                next.next(req, resp);
                return;
            }
        }

        unauthorized(resp);
    }
}
