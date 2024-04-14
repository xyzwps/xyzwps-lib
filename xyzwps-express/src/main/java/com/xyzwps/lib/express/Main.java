package com.xyzwps.lib.express;

public class Main {
    public static void main(String[] args) {
        new Server()
                .use((req, resp, next) -> {
                    System.out.printf("-> %s %s\n", req.method(), req.url());
                    resp.status(200)
                            .header("Content-Type", "application/json");
                    resp.send("[\"Hello\":\"World\"]");
                })
                .listen(3000);
    }
}
