package com.xyzwps.lib.express.middleware.router;

import com.xyzwps.lib.express.HttpMethod;
import com.xyzwps.lib.express.HttpMiddleware;

public class Trie {

    private final HPathTrie<HttpMiddleware> getTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> postTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> putTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> deleteTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> patchTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> headTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> optionsTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> traceTrie = new HPathTrie<>();
    private final HPathTrie<HttpMiddleware> allTrie = new HPathTrie<>();

    public Trie(Router router) {
        router.toHandlers("", HttpMiddleware.DO_NOTHING).forEach(handler -> {
            var trie = trie(handler.method());
            trie.insert(HPath.from(handler.url()), handler.middleware());
        });
    }

    public HPathTrie<HttpMiddleware> trie(HttpMethod method) {
        return switch (method) {
            case null -> allTrie;
            case GET -> getTrie;
            case POST -> postTrie;
            case PUT -> putTrie;
            case DELETE -> deleteTrie;
            case PATCH -> patchTrie;
            case HEAD -> headTrie;
            case OPTIONS -> optionsTrie;
            case TRACE -> traceTrie;
            default -> throw new IllegalArgumentException("Unsupported method: " + method);
        };
    }

}
