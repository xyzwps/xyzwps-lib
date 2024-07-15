package com.xyzwps.website.modules;

import com.xyzwps.lib.express.Filter;
import com.xyzwps.lib.express.HttpRequest;
import com.xyzwps.lib.express.HttpResponse;
import com.xyzwps.lib.express.HttpStatus;
import com.xyzwps.lib.express.filter.Router;
import com.xyzwps.website.common.JSON;
import com.xyzwps.website.filter.RouterMaker;
import io.avaje.validation.ConstraintViolation;
import io.avaje.validation.ConstraintViolationException;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Singleton
@Slf4j
public class IndexRouter extends Router {

    public IndexRouter(List<RouterMaker> routerMakers) {
        this.use(this::handleError);
        routerMakers.forEach(maker -> maker.make(this));
    }

    private void handleError(HttpRequest req, HttpResponse resp, Filter.Next next) {
        try {
            next.next(req, resp);
        } catch (Exception ex) {
            log.error("Error in request", ex);
            switch (ex) {
                case ConstraintViolationException e -> {
                    var message = e.violations().stream().findFirst()
                            .map(ConstraintViolation::message).orElse("Constraint violation");
                    sendJson(resp, HttpStatus.BAD_REQUEST, new ErrorResponse(message));
                }
                case NullPointerException ignored ->
                        sendJson(resp, HttpStatus.INTERNAL_SERVER_ERROR, Map.of("error", "NPE"));
                default -> {
                    var message = ex.getMessage();
                    sendJson(resp, HttpStatus.INTERNAL_SERVER_ERROR,
                            new ErrorResponse(message == null ? "Internal server error!" : message));
                }
            }
        }
    }

    record ErrorResponse(String msg) {
    }

    private void sendJson(HttpResponse resp, HttpStatus status, Object json) {
        resp.status(status);
        resp.headers().set("Content-Type", "application/json");
        resp.send(JSON.stringify(json).getBytes());
    }
}
