package com.xyzwps.lib.express;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new Server(3000).run();
    }
}
