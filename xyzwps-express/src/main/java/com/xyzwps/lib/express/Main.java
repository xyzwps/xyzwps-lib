package com.xyzwps.lib.express;

import com.xyzwps.lib.express.middleware.Router;

public class Main {
    public static void main(String[] args) {

        var router = new Router();

        new Server()
                .use((req, resp, next) -> {
                    System.out.printf("-> %s %s \n", req.method(), req.url());
                    resp.status(200)
                            .header("Content-Type", "application/json");
                    resp.send("[\"Hello\":\"World\"]".getBytes());
                })
//                .use(router.routes())routes
                .listen(3000);
    }
}
