package com.xyzwps.lib.express.filter;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.dollar.Pair;
import com.xyzwps.lib.express.*;

import java.util.ArrayList;
import java.util.List;


/**
 * A router that can be used to handle requests.
 */
public class SimpleRouter {

    private final List<Filter> used = new ArrayList<>();

    private final List<Item> items = new ArrayList<>();

    /**
     * Create a new Filter that will handle requests based on the added routes.
     *
     * @return the filter
     */
    public Filter toFilter() {
        return toFilter(null);
    }

    /**
     * Create a new Filter that will handle requests based on the added routes.
     *
     * @param notFoundFilter the filter to use when no route is matched; if null, a default 404 response will be sent
     * @return the filter
     */
    public Filter toFilter(Filter notFoundFilter) {
        return (req, res, next) -> {
            var result = match(req.method(), UrlPath.of(req.path()), 0);
            if (result instanceof MatchResult.Matched matched) {
                req.pathVariables().addAll(matched.pathVariables);
                matched.filter.filter(req, res, next);
            } else {
                if (notFoundFilter == null) {
                    res.status(HttpStatus.NOT_FOUND);
                    res.headers().set(HttpHeaders.CONTENT_TYPE, "text/html");
                    res.send("<html><head><title>Not Found</title></head><body>Not Found</body></html>".getBytes());
                } else {
                    notFoundFilter.filter(req, res, next);
                }
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
                    if (nested.simpleRouter.match(method, path, start + nested.prefix.length()) instanceof MatchResult.Matched nestedMatched) {
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
    public SimpleRouter use(Filter filter) {
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
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter handle(HttpMethod method, String path, Filter filter, Handler handler) {
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

        items.add(new Item.Handled(method, UrlPath.of(path), composeFilters(filters)));
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
    public SimpleRouter handle(HttpMethod method, String path, Handler handler) {
        return handle(method, path, null, handler);
    }

    /**
     * Handle a request with the given method and path.
     *
     * @param method the HTTP method
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter handle(HttpMethod method, String path, Filter filter) {
        return handle(method, path, filter, null);
    }

    /**
     * Handle a GET request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter get(String path, Handler handler) {
        return handle(HttpMethod.GET, path, handler);
    }

    /**
     * Handle a POST request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter post(String path, Handler handler) {
        return handle(HttpMethod.POST, path, handler);
    }

    /**
     * Handle a PUT request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter put(String path, Handler handler) {
        return handle(HttpMethod.PUT, path, handler);
    }

    /**
     * Handle a DELETE request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter delete(String path, Handler handler) {
        return handle(HttpMethod.DELETE, path, handler);
    }

    /**
     * Handle a PATCH request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter patch(String path, Handler handler) {
        return handle(HttpMethod.PATCH, path, handler);
    }

    /**
     * Handle a HEAD request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter head(String path, Handler handler) {
        return handle(HttpMethod.HEAD, path, handler);
    }

    /**
     * Handle a OPTIONS request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter options(String path, Handler handler) {
        return handle(HttpMethod.OPTIONS, path, handler);
    }

    /**
     * Handle a TRACE request with the given path.
     *
     * @param path    the path
     * @param handler the handler to use; cannot be null
     * @return this router
     */
    public SimpleRouter trace(String path, Handler handler) {
        return handle(HttpMethod.TRACE, path, handler);
    }

    /**
     * Handle a GET request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter get(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.GET, path, filter, handler);
    }

    /**
     * Handle a POST request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter post(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.POST, path, filter, handler);
    }

    /**
     * Handle a PUT request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter put(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.PUT, path, filter, handler);
    }

    /**
     * Handle a DELETE request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter delete(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.DELETE, path, filter, handler);
    }

    /**
     * Handle a PATCH request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter patch(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.PATCH, path, filter, handler);
    }

    /**
     * Handle a HEAD request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter head(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.HEAD, path, filter, handler);
    }

    /**
     * Handle a OPTIONS request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter options(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.OPTIONS, path, filter, handler);
    }

    /**
     * Handle a TRACE request with the given path.
     *
     * @param path    the path
     * @param filter  the filter to use; filter and handler cannot be both null
     * @param handler the handler to use; filter and handler cannot be both null
     * @return this router
     */
    public SimpleRouter trace(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.TRACE, path, filter, handler);
    }


    /**
     * Handle a GET request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter get(String path, Filter filter) {
        return handle(HttpMethod.GET, path, filter);
    }

    /**
     * Handle a POST request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter post(String path, Filter filter) {
        return handle(HttpMethod.POST, path, filter);
    }

    /**
     * Handle a PUT request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter put(String path, Filter filter) {
        return handle(HttpMethod.PUT, path, filter);
    }

    /**
     * Handle a DELETE request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter delete(String path, Filter filter) {
        return handle(HttpMethod.DELETE, path, filter);
    }

    /**
     * Handle a PATCH request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter patch(String path, Filter filter) {
        return handle(HttpMethod.PATCH, path, filter);
    }

    /**
     * Handle a HEAD request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter head(String path, Filter filter) {
        return handle(HttpMethod.HEAD, path, filter);
    }

    /**
     * Handle a OPTIONS request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter options(String path, Filter filter) {
        return handle(HttpMethod.OPTIONS, path, filter);
    }

    /**
     * Handle a TRACE request with the given path.
     *
     * @param path   the path
     * @param filter the filter to use; cannot be null
     * @return this router
     */
    public SimpleRouter trace(String path, Filter filter) {
        return handle(HttpMethod.TRACE, path, filter);
    }

    /**
     * Nest a router under a prefix.
     *
     * @param prefix the prefix; cannot be null or empty; cannot contain '**'
     * @param filter the filter to use; if null, it will be ignored
     * @param simpleRouter the router to nest; cannot be null
     * @return this router
     */
    public SimpleRouter nest(String prefix, Filter filter, SimpleRouter simpleRouter) {
        var prefixPath = UrlPath.of(prefix);
        if (prefixPath.length() == 0) {
            throw new IllegalArgumentException("Invalid prefix: " + prefix);
        }
        if (prefixPath.hasStar2()) {
            throw new IllegalArgumentException("Invalid prefix: " + prefix + ". Prefix cannot be contains **.");
        }

        Args.notNull(simpleRouter, "router cannot be null");

        var filters = new ArrayList<>(used);
        if (filter != null) {
            filters.add(filter);
        }

        items.add(new Item.Nested(prefixPath, composeFilters(filters), simpleRouter));

        return this;
    }

    /**
     * Nest a router under a prefix.
     *
     * @param prefix the prefix; cannot be null or empty; cannot contain '**'
     * @param simpleRouter the router to nest; cannot be null
     * @return this router
     */
    public SimpleRouter nest(String prefix, SimpleRouter simpleRouter) {
        return nest(prefix, null, simpleRouter);
    }

    private sealed interface Item {

        record Handled(HttpMethod method, UrlPath path, Filter filter) implements Item {
        }

        record Nested(UrlPath prefix, Filter filter, SimpleRouter simpleRouter) implements Item {
        }
    }

}
