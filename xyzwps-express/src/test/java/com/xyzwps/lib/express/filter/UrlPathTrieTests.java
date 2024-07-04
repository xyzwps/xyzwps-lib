package com.xyzwps.lib.express.filter;

import com.xyzwps.lib.express.UrlPath;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UrlPathTrieTests {

    @Test
    void rootMatch() {
        var trie = new UrlPathTrie<String>();

        assertNull(trie.match(UrlPath.of("/")));

        trie.insert(UrlPath.ROOT, "root");
        assertEquals("root", trie.match(UrlPath.of("/")));

        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.ROOT, "root2"));
    }

    @Test
    void textMatch() {
        var trie = new UrlPathTrie<String>();

        assertNull(trie.match(UrlPath.of("/a")));

        trie.insert(UrlPath.of("/a"), "a");
        assertEquals("a", trie.match(UrlPath.of("/a")));

        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.of("/a"), "a2"));


        assertNull(trie.match(UrlPath.of("/a/b/c")));

        trie.insert(UrlPath.of("/a/b/c"), "abc");
        assertEquals("abc", trie.match(UrlPath.of("/a/b/c")));
        assertEquals("a", trie.match(UrlPath.of("/a")));

        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.of("/a/b/c"), "abc2"));


        assertNull(trie.match(UrlPath.of("/a/b")));

        trie.insert(UrlPath.of("/a/b"), "ab");
        assertEquals("ab", trie.match(UrlPath.of("/a/b")));
        assertEquals("a", trie.match(UrlPath.of("/a")));
        assertEquals("abc", trie.match(UrlPath.of("/a/b/c")));

        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.of("/a/b"), "ab2"));


        assertNull(trie.match(UrlPath.of("/a/b/c/d")));


        assertNull(trie.match(UrlPath.of("/hello/world")));
        assertNull(trie.match(UrlPath.of("/hello")));

        trie.insert(UrlPath.of("/hello/world"), "hello world");
        assertEquals("hello world", trie.match(UrlPath.of("/hello/world")));
        assertNull(trie.match(UrlPath.of("/hello")));
    }

    @Test
    void placeHolderMatch() {
        var trie = new UrlPathTrie<String>();

        assertNull(trie.match(UrlPath.of("/users/1")));

        trie.insert(UrlPath.of("/users/:id"), "get user by id");
        assertEquals("get user by id", trie.match(UrlPath.of("/users/1")));
        assertEquals("get user by id", trie.match(UrlPath.of("/users/2")));
        assertNull(trie.match(UrlPath.of("/users")));

        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.of("/users/*"), "user star"));


        assertNull(trie.match(UrlPath.of("/users/1/orders/2")));

        trie.insert(UrlPath.of("/users/:userId/orders/:orderId"), "get order by user id and order id");
        assertEquals("get order by user id and order id", trie.match(UrlPath.of("/users/ok/orders/ok")));
        assertEquals("get order by user id and order id", trie.match(UrlPath.of("/users/2/orders/3")));
        assertNull(trie.match(UrlPath.of("/users/1/orders")));
        assertNull(trie.match(UrlPath.of("/users/orders/2")));

        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.of("/users/*/orders/*"), "order star"));

        trie.insert(UrlPath.of("/users/ok/orders/ok"), "ok jia");
        assertEquals("ok jia", trie.match(UrlPath.of("/users/ok/orders/ok")));
        assertEquals("get order by user id and order id", trie.match(UrlPath.of("/users/2/orders/3")));
        assertEquals("get order by user id and order id", trie.match(UrlPath.of("/users/ok/orders/3")));
        assertEquals("get order by user id and order id", trie.match(UrlPath.of("/users/2/orders/ok")));
    }

    @Test
    void star2Match() {
        var trie = new UrlPathTrie<String>();

        assertNull(trie.match(UrlPath.of("/a/b/c/d")));

        trie.insert(UrlPath.of("/a/**"), "star2");
        assertEquals("star2", trie.match(UrlPath.of("/a")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t")));

        trie.insert(UrlPath.of("/a"), "a");
        assertEquals("a", trie.match(UrlPath.of("/a")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t")));

        assertNull(trie.match(UrlPath.ROOT));

        trie.insert(UrlPath.of("/**"), "root star2");
        assertEquals("a", trie.match(UrlPath.of("/a")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g")));
        assertEquals("star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t")));
        assertEquals("root star2", trie.match(UrlPath.of("/b")));
        assertEquals("root star2", trie.match(UrlPath.ROOT));

        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.of("/**"), ""));
        assertThrows(DuplicatePathException.class, () -> trie.insert(UrlPath.of("/a/**"), ""));

        trie.insert(UrlPath.of("/a/:b/**"), "a:b/star2");
        assertEquals("a", trie.match(UrlPath.of("/a")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/b")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/b/c")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/b/c/d")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/b/c/d/e")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/b/c/d/e/f")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t")));
        assertEquals("root star2", trie.match(UrlPath.of("/b")));
        assertEquals("root star2", trie.match(UrlPath.ROOT));

        trie.insert(UrlPath.of("/a/b/**"), "ab/star2");
        assertEquals("a", trie.match(UrlPath.of("/a")));
        assertEquals("ab/star2", trie.match(UrlPath.of("/a/b")));
        assertEquals("ab/star2", trie.match(UrlPath.of("/a/b/c")));
        assertEquals("ab/star2", trie.match(UrlPath.of("/a/b/c/d")));
        assertEquals("ab/star2", trie.match(UrlPath.of("/a/b/c/d/e")));
        assertEquals("ab/star2", trie.match(UrlPath.of("/a/b/c/d/e/f")));
        assertEquals("ab/star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g")));
        assertEquals("ab/star2", trie.match(UrlPath.of("/a/b/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t")));
        assertEquals("root star2", trie.match(UrlPath.of("/b")));
        assertEquals("root star2", trie.match(UrlPath.ROOT));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/c")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/c/c")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/c/c/d")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/c/c/d/e")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/c/c/d/e/f")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/c/c/d/e/f/g")));
        assertEquals("a:b/star2", trie.match(UrlPath.of("/a/c/c/d/e/f/g/h/i/j/k/l/m/n/o/p/q/r/s/t")));
    }

}
