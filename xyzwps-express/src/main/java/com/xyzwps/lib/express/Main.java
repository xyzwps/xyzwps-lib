package com.xyzwps.lib.express;

import com.xyzwps.lib.express.middleware.Router;

public class Main {
    public static void main(String[] args) {

        var userRouter = new Router()
                .get("/{id}", (req, resp, next) -> {
                    resp.status(200).header("Content-Type", "application/json");
                    resp.send("{\"msg\":\"get user\"}".getBytes());
                })
                .get("/{id}/posts", (req, resp, next) -> {
                    resp.status(200).header("Content-Type", "application/json");
                    resp.send("{\"msg\":\"get user posts\"}".getBytes());
                });

        var router = new Router()
                .get("/hello/world", (req, resp, next) -> {
                    resp.status(200).header("Content-Type", "application/json");
                    resp.send("[\"Hello\":\"World\"]".getBytes());
                })
                .nest("/users", userRouter);

        new Server()
                .use((req, resp, next) -> {
                    System.out.printf("-> %s %s \n", req.method(), req.url());
                    next.call();
                })
                .use(router.routes())
                .listen(3000);
    }
}
