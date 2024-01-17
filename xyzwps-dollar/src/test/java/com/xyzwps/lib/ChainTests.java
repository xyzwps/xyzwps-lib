package com.xyzwps.lib;

import com.xyzwps.lib.dollar.generator.GeneratorChainFactory;
import com.xyzwps.lib.dollar.generator.GeneratorMapEntryChainFactory;
import com.xyzwps.lib.dollar.iterator.IteratorChainFactory;
import com.xyzwps.lib.dollar.iterator.IteratorMapEntryChainFactory;
import com.xyzwps.lib.dollar.seq.SeqChainFactory;
import com.xyzwps.lib.dollar.seq.SeqMapEntryChainFactory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

class ChainTests {
    @Test
    void testGenerator() {
        new ChainCases(GeneratorChainFactory.INSTANCE, GeneratorMapEntryChainFactory.INSTANCE).test();
    }

    @Test
    void testSeq() {
        new ChainCases(SeqChainFactory.INSTANCE, SeqMapEntryChainFactory.INSTANCE).test();
    }

    @Test
    void testIterator() {
        new ChainCases(IteratorChainFactory.INSTANCE, IteratorMapEntryChainFactory.INSTANCE).test();
    }
}
