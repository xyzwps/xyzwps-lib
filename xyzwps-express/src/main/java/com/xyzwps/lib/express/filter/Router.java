package com.xyzwps.lib.express.filter;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.bedrock.Consumer3;
import com.xyzwps.lib.express.*;

import java.util.ArrayList;
import java.util.List;

/**
 * A router that uses a trie for routing requests.
 */
public class Router {

    private final UrlPathTrie<Filter> getTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Filter> postTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Filter> putTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Filter> deleteTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Filter> patchTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Filter> headTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Filter> optionsTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Filter> traceTrie = new UrlPathTrie<>();

    private final List<Api> apis = new ArrayList<>();

    private final Filter notFound;

    public Router(Filter notFound) {
        this.notFound = notFound;
    }

    public Router() {
        this(null);
    }

    private Filter use;

    public Router use(Filter filter) {
        if (filter == null) {
            return this;
        }

        if (use == null) {
            use = filter;
        } else {
            use = use.andThen(filter);
        }
        return this;
    }


    public Filter toFilter() {
        return (req, resp, next) -> {
            var up = UrlPath.of(req.path());
            var filter = switch (req.method()) {
                case GET -> getTrie.match(up);
                case POST -> postTrie.match(up);
                case PUT -> putTrie.match(up);
                case DELETE -> deleteTrie.match(up);
                case PATCH -> patchTrie.match(up);
                case HEAD -> headTrie.match(up);
                case OPTIONS -> optionsTrie.match(up);
                case TRACE -> traceTrie.match(up);
                case CONNECT -> throw new IllegalArgumentException("CONNECT method is not supported");
            };

            if (filter == null) {
                if (notFound != null) {
                    notFound.filter(req, resp, next);
                } else {
                    resp.status(HttpStatus.NOT_FOUND);
                    resp.headers().set(HttpHeaders.CONTENT_TYPE, "text/html");
                    resp.send("<html><head><title>Not Found</title></head><body>Not Found</body></html>".getBytes());
                }
            } else {
                filter.filter(req, resp, next);
            }
        };
    }

    public Router handle(HttpMethod method, String path, Filter filter) {
        Args.notNull(method, "Argument method cannot be null");
        Args.notNull(path, "Argument path cannot be null");
        Args.notNull(filter, "Argument filter cannot be null");

        var up = UrlPath.of(path);
        try {
            switch (method) {
                case GET -> getTrie.insert(up, use.andThen(filter));
                case POST -> postTrie.insert(up, use.andThen(filter));
                case PUT -> putTrie.insert(up, use.andThen(filter));
                case DELETE -> deleteTrie.insert(up, use.andThen(filter));
                case PATCH -> patchTrie.insert(up, use.andThen(filter));
                case HEAD -> headTrie.insert(up, use.andThen(filter));
                case OPTIONS -> optionsTrie.insert(up, use.andThen(filter));
                case TRACE -> traceTrie.insert(up, use.andThen(filter));
                case CONNECT -> throw new IllegalArgumentException("CONNECT method is not supported");
            }
        } catch (DuplicatePathException e) {
            throw new IllegalArgumentException("Duplicate route: " + method + " " + path);
        }
        apis.add(new Api(method, path));
        return this;
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

    public Router nest(String prefix, Nest builder) {
        var prefixPath = UrlPath.of(prefix);
        if (prefixPath.length() == 0) {
            throw new IllegalArgumentException("Invalid prefix: " + prefix);
        }
        if (prefixPath.hasStar2()) {
            throw new IllegalArgumentException("Invalid prefix: " + prefix + ". Prefix cannot be contains **.");
        }

        Args.notNull(builder, "Argument builder cannot be null");

        builder.forEach((method, path, filter) -> handle(method, concat(prefix, path), filter));
        return this;
    }

    private static String concat(String prefix, String path) {
        if (prefix == null || prefix.isEmpty()) {
            return path;
        }
        if (path == null || path.isEmpty()) {
            return prefix;
        }

        boolean prefixEndsWithSlash = prefix.endsWith("/");
        boolean pathStartsWithSlash = path.startsWith("/");
        if (prefixEndsWithSlash && pathStartsWithSlash) {
            return prefix + path.substring(1);
        } else if (!prefixEndsWithSlash && !pathStartsWithSlash) {
            return prefix + "/" + path;
        } else {
            return prefix + path;
        }
    }


    public static class Nest {

        private final List<Item> items = new ArrayList<>();

        public Nest handle(HttpMethod method, String path, Filter filter) {
            Args.notNull(method, "Argument method cannot be null");
            Args.ne(method, HttpMethod.CONNECT, "CONNECT method is not supported");
            Args.notNull(path, "Argument path cannot be null");
            Args.notNull(filter, "Argument filter cannot be null");
            items.add(new Item(method, path, filter));
            return this;
        }

        public Nest get(String path, Filter filter) {
            return handle(HttpMethod.GET, path, filter);
        }

        public Nest post(String path, Filter filter) {
            return handle(HttpMethod.POST, path, filter);
        }

        public Nest put(String path, Filter filter) {
            return handle(HttpMethod.PUT, path, filter);
        }

        public Nest delete(String path, Filter filter) {
            return handle(HttpMethod.DELETE, path, filter);
        }

        public Nest patch(String path, Filter filter) {
            return handle(HttpMethod.PATCH, path, filter);
        }

        public Nest head(String path, Filter filter) {
            return handle(HttpMethod.HEAD, path, filter);
        }

        public Nest options(String path, Filter filter) {
            return handle(HttpMethod.OPTIONS, path, filter);
        }

        public Nest trace(String path, Filter filter) {
            return handle(HttpMethod.TRACE, path, filter);
        }

        private void forEach(Consumer3<HttpMethod, String, Filter> consumer) {
            items.forEach(item -> consumer.accept(item.method, item.path, item.filter));
        }

        private record Item(HttpMethod method, String path, Filter filter) {
        }
    }

    public record Api(HttpMethod method, String path) {
    }
}
