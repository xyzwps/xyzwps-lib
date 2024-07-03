package com.xyzwps.lib.express.middleware;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.express.*;

import java.util.ArrayList;
import java.util.List;


public class Router {

    private final List<Filter> used = new ArrayList<>();

    private final List<Item> items = new ArrayList<>();

    public Filter toFilter() {
        return (req, res, next) -> {
            var result = match(req.method(), UrlPath.parse(req.path()), 0);
            if (result instanceof MatchResult.Matched matched) {
                req.pathVariables().addAll(matched.pathVariables);
                matched.filter.filter(req, res, next);
            } else {
                res.status(HttpStatus.NOT_FOUND);
                res.headers().set(HttpHeaders.CONTENT_TYPE, "text/html");
                res.send("<html><head><title>Not Found</title></head><body>Not Found</body></html>".getBytes());
            }
        };
    }

    public sealed interface MatchResult {
        MatchResult NOT_MATCHED = NotMatched.INSTANCE;

        record Matched(
                List<Pair<String, String>> pathVariables,
                Filter filter
        ) implements MatchResult {
        }

        enum NotMatched implements MatchResult {
            INSTANCE
        }
    }

    public MatchResult match(HttpMethod method, UrlPath path, int start) {
        for (var item : items) {
            if (item instanceof Item.Handled handled) {
                if (handled.method != null && handled.method != method) {
                    continue; // method mismatch
                }
                if (handled.path.match(path, start) instanceof UrlPath.MatchResult.Matched matched) {
                    return new MatchResult.Matched(matched.pathVariables(), handled.filter);
                }
            } else if (item instanceof Item.Nested nested) {
                if (nested.prefix.prefixOf(path, start) instanceof UrlPath.MatchResult.Matched matched) {
                    if (nested.router.match(method, path, start + nested.prefix.length()) instanceof MatchResult.Matched nestedMatched) {
                        var pathVariables = new ArrayList<>(matched.pathVariables());
                        pathVariables.addAll(nestedMatched.pathVariables());
                        return new MatchResult.Matched(
                                pathVariables,
                                nested.filter.andThen(nestedMatched.filter)
                        );
                    }
                }
            }
        }
        return MatchResult.NOT_MATCHED;
    }

    /**
     * Use a filter for all routes added after this method invocation in this router.
     *
     * @param filter the filter to use; if null, it will be ignored
     * @return this router
     */
    public Router use(Filter filter) {
        if (filter != null) {
            used.add(filter);
        }
        return this;
    }

    /**
     * Handle a request with the given method and path.
     *
     * @param method  the HTTP method
     * @param path    the path
     * @param filter  the filter to use; if null, it will be ignored
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public Router handle(HttpMethod method, String path, Filter filter, Handler handler) {
        if (method == HttpMethod.CONNECT) {
            throw new IllegalArgumentException("CONNECT method is not supported");
        }

        if (filter == null && handler == null) {
            throw new IllegalArgumentException("filter and handler cannot be null");
        }

        var filters = new ArrayList<>(used);
        if (filter != null) {
            filters.add(filter);
        }
        if (handler != null) {
            filters.add(handler.toFilter());
        }

        items.add(new Item.Handled(method, UrlPath.parse(path), composeFilters(filters)));
        return this;
    }

    private Filter composeFilters(List<Filter> filters) {
        Filter filter = null;

        for (var f : filters) {
            if (f == null) {
                continue;
            }

            if (filter == null) {
                filter = f;
            } else {
                filter = filter.andThen(f);
            }
        }

        return filter == null ? Filter.EMPTY : filter;
    }

    /**
     * Handle a request with the given method and path.
     *
     * @param method  the HTTP method
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public Router handle(HttpMethod method, String path, Handler handler) {
        return handle(method, path, null, handler);
    }

    public Router handle(HttpMethod method, String path, Filter filter) {
        return handle(method, path, filter, null);
    }

    public Router get(String path, Handler handler) {
        return handle(HttpMethod.GET, path, handler);
    }

    public Router post(String path, Handler handler) {
        return handle(HttpMethod.POST, path, handler);
    }

    public Router put(String path, Handler handler) {
        return handle(HttpMethod.PUT, path, handler);
    }

    public Router delete(String path, Handler handler) {
        return handle(HttpMethod.DELETE, path, handler);
    }

    public Router patch(String path, Handler handler) {
        return handle(HttpMethod.PATCH, path, handler);
    }

    public Router head(String path, Handler handler) {
        return handle(HttpMethod.HEAD, path, handler);
    }

    public Router options(String path, Handler handler) {
        return handle(HttpMethod.OPTIONS, path, handler);
    }

    public Router trace(String path, Handler handler) {
        return handle(HttpMethod.TRACE, path, handler);
    }

    public Router get(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.GET, path, filter, handler);
    }

    public Router post(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.POST, path, filter, handler);
    }

    public Router put(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.PUT, path, filter, handler);
    }

    public Router delete(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.DELETE, path, filter, handler);
    }

    public Router patch(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.PATCH, path, filter, handler);
    }

    public Router head(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.HEAD, path, filter, handler);
    }

    public Router options(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.OPTIONS, path, filter, handler);
    }

    public Router trace(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.TRACE, path, filter, handler);
    }

    public Router get(String path, Filter filter) {
        return handle(HttpMethod.GET, path, filter);
    }

    public Router post(String path, Filter filter) {
        return handle(HttpMethod.POST, path, filter);
    }

    public Router put(String path, Filter filter) {
        return handle(HttpMethod.PUT, path, filter);
    }

    public Router delete(String path, Filter filter) {
        return handle(HttpMethod.DELETE, path, filter);
    }

    public Router patch(String path, Filter filter) {
        return handle(HttpMethod.PATCH, path, filter);
    }

    public Router head(String path, Filter filter) {
        return handle(HttpMethod.HEAD, path, filter);
    }

    public Router options(String path, Filter filter) {
        return handle(HttpMethod.OPTIONS, path, filter);
    }

    public Router trace(String path, Filter filter) {
        return handle(HttpMethod.TRACE, path, filter);
    }

    /**
     * Nest a router under a prefix.
     *
     * @param prefix the prefix; cannot be null or empty; cannot contain '**'
     * @param filter the filter to use; if null, it will be ignored
     * @param router the router to nest; cannot be null
     * @return this router
     */
    public Router nest(String prefix, Filter filter, Router router) {
        var prefixPath = UrlPath.parse(prefix);
        if (prefixPath.length() == 0) {
            throw new IllegalArgumentException("Invalid prefix: " + prefix);
        }
        if (prefixPath.hasStar2()) {
            throw new IllegalArgumentException("Invalid prefix: " + prefix + ". Prefix cannot be contains **.");
        }

        Args.notNull(router, "router cannot be null");

        var filters = new ArrayList<>(used);
        if (filter != null) {
            filters.add(filter);
        }

        items.add(new Item.Nested(prefixPath, composeFilters(filters), router));

        return this;
    }

    /**
     * Nest a router under a prefix.
     *
     * @param prefix the prefix; cannot be null or empty; cannot contain '**'
     * @param router the router to nest; cannot be null
     * @return this router
     */
    public Router nest(String prefix, Router router) {
        return nest(prefix, null, router);
    }

    private sealed interface Item {

        record Handled(HttpMethod method, UrlPath path, Filter filter) implements Item {
        }

        record Nested(UrlPath prefix, Filter filter, Router router) implements Item {
        }
    }

}
