package com.xyzwps.lib.express;

public record AndThenFilter(Filter current, Filter then) implements Filter {
    public AndThenFilter {
        if (current == null || then == null) {
            throw new IllegalArgumentException("f1 and f2 must not be null");
        }
    }

    @Override
    public void filter(HttpRequest request, HttpResponse response, Next next) {
        current.filter(request, response, (req, res) -> then.filter(req, res, next));
    }
}
