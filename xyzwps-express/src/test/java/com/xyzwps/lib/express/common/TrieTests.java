package com.xyzwps.lib.express.common;

import com.xyzwps.lib.dollar.util.Counter;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class TrieTests {

    @Test
    void trie() {
        var t = new Trie<Integer>();
        /*
         * 1 /auth/login
         * 2 /auth/register
         * 3 /home
         * 4 /
         * 5 /auth/oauth/authorize/wx
         * 6 /auth/oauth/authorize/ali
         * 7 /auth/oauth/authorize/ms
         */

        /*
         * add
         */

        t.add(SegmentedPath.of("auth", "login"), 1);
        t.add(SegmentedPath.of("auth", "register"), 2);
        t.add(SegmentedPath.of("home"), 3);
        t.add(SegmentedPath.of(), 4);
        t.add(SegmentedPath.of("auth", "oauth", "authorize", "wx"), 5);
        t.add(SegmentedPath.of("auth", "oauth", "authorize", "ali"), 6);
        t.add(SegmentedPath.of("auth", "oauth", "authorize", "ms"), 7);

        /*
         * get
         */

        assertEquals(1, t.get(SegmentedPath.of("auth", "login")).orElseThrow());
        assertEquals(2, t.get(SegmentedPath.of("auth", "register")).orElseThrow());
        assertEquals(3, t.get(SegmentedPath.of("home")).orElseThrow());
        assertEquals(4, t.get(SegmentedPath.of()).orElseThrow());
        assertEquals(4, t.get(null).orElseThrow());
        assertEquals(5, t.get(SegmentedPath.of("auth", "oauth", "authorize", "wx")).orElseThrow());
        assertEquals(6, t.get(SegmentedPath.of("auth", "oauth", "authorize", "ali")).orElseThrow());
        assertEquals(7, t.get(SegmentedPath.of("auth", "oauth", "authorize", "ms")).orElseThrow());

        /*
         * get nothing
         */

        assertTrue(t.get(SegmentedPath.of("auth")).isEmpty());
        assertTrue(t.get(SegmentedPath.of("auth", "oauth")).isEmpty());
        assertTrue(t.get(SegmentedPath.of("auth", "oauth", "authorize")).isEmpty());
        assertTrue(t.get(SegmentedPath.of("auth", "oauth", "authorize", "wx", "v2")).isEmpty());

        // getOrSetDefault
        {
            Supplier<Integer> defaultSupplier = () -> 100;

            assertEquals(5, t.getOrSetDefault(SegmentedPath.of("auth", "oauth", "authorize", "wx"), defaultSupplier));
            assertEquals(5, t.get(SegmentedPath.of("auth", "oauth", "authorize", "wx")).orElseThrow());

            assertTrue(t.get(SegmentedPath.of("auth", "oauth", "authorize", "wx", "v2")).isEmpty());
            assertEquals(100, t.getOrSetDefault(SegmentedPath.of("auth", "oauth", "authorize", "wx", "v2"), defaultSupplier));
            assertEquals(100, t.get(SegmentedPath.of("auth", "oauth", "authorize", "wx", "v2")).orElseThrow());

            assertEquals("Default supplier cannot produce a null", assertThrows(NullPointerException.class,
                    () -> t.getOrSetDefault(SegmentedPath.of("auth", "oauth", "authorize", "wx", "v3"), () -> null)).getMessage());
        }

        // iterate
        {
            var holder = new HashMap<String, Integer>();
            var counter = new Counter(0);
            t.iterate((segments, value) -> {
                counter.incrAndGet();
                holder.put("/" + String.join("/", segments), value);
            });
            assertEquals(8, counter.get());
            assertEquals(1, holder.get("/auth/login"));
            assertEquals(2, holder.get("/auth/register"));
            assertEquals(3, holder.get("/home"));
            assertEquals(4, holder.get("/"));
            assertEquals(5, holder.get("/auth/oauth/authorize/wx"));
            assertEquals(6, holder.get("/auth/oauth/authorize/ali"));
            assertEquals(7, holder.get("/auth/oauth/authorize/ms"));
            assertEquals(100, holder.get("/auth/oauth/authorize/wx/v2"));
        }

        // add trie
        {
            var t2 = new Trie<Integer>();
            t2.add(SegmentedPath.of("insert"), 201);
            t2.add(SegmentedPath.of("update"), 202);
            t2.add(SegmentedPath.of("delete"), 203);
            t2.add(SegmentedPath.of("select", "all"), 204);
            t2.add(SegmentedPath.of("select", "by-id"), 205);
            t.addTrie(SegmentedPath.of("post"), t2);

            // --

            var holder = new HashMap<String, Integer>();
            var counter = new Counter(0);
            t.iterate((segments, value) -> {
                counter.incrAndGet();
                holder.put("/" + String.join("/", segments), value);
            });
            assertEquals(13, counter.get());
            assertEquals(1, holder.get("/auth/login"));
            assertEquals(2, holder.get("/auth/register"));
            assertEquals(3, holder.get("/home"));
            assertEquals(4, holder.get("/"));
            assertEquals(5, holder.get("/auth/oauth/authorize/wx"));
            assertEquals(6, holder.get("/auth/oauth/authorize/ali"));
            assertEquals(7, holder.get("/auth/oauth/authorize/ms"));
            assertEquals(100, holder.get("/auth/oauth/authorize/wx/v2"));

            assertEquals(201, holder.get("/post/insert"));
            assertEquals(202, holder.get("/post/update"));
            assertEquals(203, holder.get("/post/delete"));
            assertEquals(204, holder.get("/post/select/all"));
            assertEquals(205, holder.get("/post/select/by-id"));
        }

        // add trie failed
        {
            var t2 = new Trie<Integer>();
            t2.add(SegmentedPath.of("insert"), 201);
            assertEquals("Duplicated path '/post/insert'", assertThrows(IllegalArgumentException.class, () -> t.addTrie(SegmentedPath.of("post"), t2)).getMessage());
        }

    }
}
