package com.xyzwps.lib.express.filter;

import com.xyzwps.lib.bedrock.Args;
import com.xyzwps.lib.bedrock.Consumer3;
import com.xyzwps.lib.express.*;
import com.xyzwps.lib.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * A router that uses a trie for routing requests.
 */
public class Router {

    private final UrlPathTrie<Api> getTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Api> postTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Api> putTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Api> deleteTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Api> patchTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Api> headTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Api> optionsTrie = new UrlPathTrie<>();
    private final UrlPathTrie<Api> traceTrie = new UrlPathTrie<>();

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

    public void filter(HttpRequest req, HttpResponse resp, Filter.Next next) {
        var up = UrlPath.of(req.path());
        var api = switch (req.method()) {
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

        if (api == null) {
            if (notFound != null) {
                notFound.filter(req, resp, next);
            } else {
                resp.status(HttpStatus.NOT_FOUND);
                resp.headers().set(HttpHeaders.CONTENT_TYPE, "text/html");
                resp.send(String.format("<html><head><title>Not Found: %s %s</title></head><body>Not Found</body></html>", req.method(), req.path()).getBytes());
            }
        } else {
            final UrlPath ap = api.path();
            final int len = Math.min(ap.length(), up.length());
            for (int i = 0; i < len; i++) {
                if (ap.get(i) instanceof UrlSegment.Param param) {
                    req.pathVariables().add(param.name(), up.get(i).text());
                }
            }
            api.filter.filter(req, resp, next);
        }
    }

    public Router handle(HttpMethod method, String path, Filter filter, Handler handler) {
        return handle(method, path, filter.andThen(handler.toFilter()));
    }

    public Router handle(HttpMethod method, String path, Handler handler) {
        return handle(method, path, handler.toFilter());
    }

    public Router handle(HttpMethod method, String path, Filter filter) {
        Args.notNull(method, "Argument method cannot be null");
        Args.notNull(path, "Argument path cannot be null");
        Args.notNull(filter, "Argument filter cannot be null");

        var up = UrlPath.of(path);
        var api = new Api(method, up, use.andThen(filter));
        try {
            switch (method) {
                case GET -> getTrie.insert(up, api);
                case POST -> postTrie.insert(up, api);
                case PUT -> putTrie.insert(up, api);
                case DELETE -> deleteTrie.insert(up, api);
                case PATCH -> patchTrie.insert(up, api);
                case HEAD -> headTrie.insert(up, api);
                case OPTIONS -> optionsTrie.insert(up, api);
                case TRACE -> traceTrie.insert(up, api);
                case CONNECT -> throw new IllegalArgumentException("CONNECT method is not supported");
            }
        } catch (DuplicatePathException e) {
            throw new IllegalArgumentException("Duplicate route: " + method + " " + path);
        }
        apis.add(api);
        return this;
    }

    public Router get(String path, Filter filter) {
        return handle(HttpMethod.GET, path, filter);
    }

    public Router get(String path, Handler handler) {
        return handle(HttpMethod.GET, path, handler);
    }

    public Router get(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.GET, path, filter, handler);
    }

    public Router post(String path, Filter filter) {
        return handle(HttpMethod.POST, path, filter);
    }

    public Router post(String path, Handler handler) {
        return handle(HttpMethod.POST, path, handler);
    }

    public Router post(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.POST, path, filter, handler);
    }

    public Router put(String path, Filter filter) {
        return handle(HttpMethod.PUT, path, filter);
    }

    public Router put(String path, Handler handler) {
        return handle(HttpMethod.PUT, path, handler);
    }

    public Router put(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.PUT, path, filter, handler);
    }

    public Router delete(String path, Filter filter) {
        return handle(HttpMethod.DELETE, path, filter);
    }

    public Router delete(String path, Handler handler) {
        return handle(HttpMethod.DELETE, path, handler);
    }

    public Router delete(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.DELETE, path, filter, handler);
    }

    public Router patch(String path, Filter filter) {
        return handle(HttpMethod.PATCH, path, filter);
    }

    public Router patch(String path, Handler handler) {
        return handle(HttpMethod.PATCH, path, handler);
    }

    public Router patch(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.PATCH, path, filter, handler);
    }

    public Router head(String path, Filter filter) {
        return handle(HttpMethod.HEAD, path, filter);
    }

    public Router head(String path, Handler handler) {
        return handle(HttpMethod.HEAD, path, handler);
    }

    public Router head(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.HEAD, path, filter, handler);
    }

    public Router options(String path, Filter filter) {
        return handle(HttpMethod.OPTIONS, path, filter);
    }

    public Router options(String path, Handler handler) {
        return handle(HttpMethod.OPTIONS, path, handler);
    }

    public Router options(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.OPTIONS, path, filter, handler);
    }

    public Router trace(String path, Filter filter) {
        return handle(HttpMethod.TRACE, path, filter);
    }

    public Router trace(String path, Handler handler) {
        return handle(HttpMethod.TRACE, path, handler);
    }

    public Router trace(String path, Filter filter, Handler handler) {
        return handle(HttpMethod.TRACE, path, filter, handler);
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

    private record Api(HttpMethod method, UrlPath path, Filter filter) {
    }
}
