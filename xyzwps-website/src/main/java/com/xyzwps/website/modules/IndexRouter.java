package com.xyzwps.website.modules;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.modules.conf.ConfRouter;
import com.xyzwps.website.modules.debug.DebugRouter;
import com.xyzwps.website.modules.user.UserRouter;
import io.avaje.validation.ConstraintViolation;
import io.avaje.validation.ConstraintViolationException;
import jakarta.inject.Singleton;
import lombok.extern.jbosslog.JBossLog;

import java.util.Map;

@Singleton
@JBossLog
public class IndexRouter extends Router {

    public IndexRouter(ConfRouter confRouter,
                       DebugRouter debugRouter,
                       UserRouter userRouter) {

        this.use(this::handleError)
                .get("/api/hello/world", (req, resp) -> {
                    resp.ok();
                    resp.headers().set("Content-Type", "application/json");
                    resp.send("[\"Hello\",\"World\"]".getBytes());
                })
                .nest("/api/conf", confRouter)
                .nest("/api/debug", debugRouter)
                .nest("/api/users", userRouter);
    }

    private void handleError(HttpRequest req, HttpResponse resp, Filter.Next next) {
        try {
            next.next(req, resp);
        } catch (Exception ex) {
            log.errorf(ex, "Error in request");
            switch (ex) {
                case ConstraintViolationException e -> {
                    var message = e.violations().stream().findFirst().map(ConstraintViolation::message).orElse("Constraint violation");
                    sendJson(resp, HttpStatus.BAD_REQUEST, new ErrorResponse(message));
                }
                case NullPointerException ignored ->
                        sendJson(resp, HttpStatus.INTERNAL_SERVER_ERROR, Map.of("error", "NPE"));
                default -> {
                    var message = ex.getMessage();
                    sendJson(resp, HttpStatus.INTERNAL_SERVER_ERROR, new ErrorResponse(message == null ? "Internal server error!" : message));
                }
            }
        }
    }

    record ErrorResponse(String error) {
    }

    private void sendJson(HttpResponse resp, HttpStatus status, Object json) {
        resp.status(status);
        resp.headers().set("Content-Type", "application/json");
        resp.send(JSON.stringify(json).getBytes());
    }
}
