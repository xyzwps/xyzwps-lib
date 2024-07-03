package com.xyzwps.lib.express;

public interface Filter {

    void filter(HttpRequest request, HttpResponse response, Next next);

    interface Next {
        void next(HttpRequest request, HttpResponse response);

        Next EMPTY = (request, response) -> {
        };

        static Next empty() {
            return EMPTY;
        }
    }

    default Filter andThen(final Filter after) {
        if (after == null) {
            return this;
        }

        return (request, response, next) -> this.filter(request, response, (req, res) -> after.filter(req, res, next));
    }


    Filter EMPTY = (request, response, next) -> next.next(request, response);

    static Filter empty() {
        return EMPTY;
    }

}
