package com.xyzwps.lib.express;

import com.xyzwps.lib.bedrock.Args;

public final class RequestExecutors {
    public static void runOnVirtualThread(Runnable runnable) {
        Args.notNull(runnable, "runnable cannot be null");
        Thread.startVirtualThread(runnable);
    }
}
