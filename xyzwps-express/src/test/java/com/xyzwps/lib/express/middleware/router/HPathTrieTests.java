package com.xyzwps.lib.express.middleware.router;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HPathTrieTests {

    @Test
    void test() {
        var trie = new HPathTrie<String>();

        trie.insert(HPath.ROOT, "root");
        trie.insert(HPath.from("/a/b/c"), "/a/b/c");
        trie.insert(HPath.from("/a/b/d"), "/a/b/d");
        trie.insert(HPath.from("/a/b"), "/a/b");
        trie.insert(HPath.from("/a"), "/a");
        trie.insert(HPath.from("/{a}/b/c"), "/{a}/b/c");
        var ex1 = assertThrows(IllegalArgumentException.class, () -> trie.insert(HPath.from("/*/b/c"), "/*/b/c"));
        assertEquals("Duplicate path: /*/b/c", ex1.getMessage());
        trie.insert(HPath.from("/a/*/c"), "/a/*/c");
        trie.insert(HPath.from("/a/*/d"), "/a/*/d");

        assertNull(trie.find("/tom/b/c/d"));
        assertEquals("/a/b/c", trie.find("/a/b/c"));
        assertEquals("/a/b/d", trie.find("/a/b/d"));
        assertEquals("/a/b", trie.find("/a/b"));
        assertEquals("/a", trie.find("/a"));

        assertEquals("/{a}/b/c", trie.find("/x/b/c"));
        assertEquals("/{a}/b/c", trie.find("/b/b/c"));
        assertEquals("/a/*/c", trie.find("/a/y/c"));

        assertEquals("/a/*/d", trie.find("/a/y/d"));
        assertNull(trie.find("/a/y/e"));
        assertNull(trie.find("/a/xx"));
        assertNull(trie.find("/a/xx/xx"));

        assertNull(trie.find("/k/xx"));
        assertNull(trie.find("/k/xx/xx"));
        assertNull(trie.find("/k"));
    }
}
