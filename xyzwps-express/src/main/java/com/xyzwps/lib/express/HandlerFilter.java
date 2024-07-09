package com.xyzwps.lib.express;

public record HandlerFilter(Handler handler) implements Filter {
    public HandlerFilter {
        if (handler == null) {
            throw new IllegalArgumentException("handler cannot be null");
        }
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, Next next) {
        handler.handle(request, response);
    }
}
