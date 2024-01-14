package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.chain.Chain;
import com.xyzwps.lib.dollar.foreach.Traversable;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.xyzwps.lib.dollar.Dollar.*;

@State(Scope.Benchmark)
public class MapBenchmark {

    static final List<Integer> ARRAY_LIST = new ArrayList<>();
    static final List<Integer> LINKED_LIST = new LinkedList<>();

    @Setup
    public void setup() {
        ARRAY_LIST.clear();
        LINKED_LIST.clear();
        for (int i = 0; i < 50; i++) {
            ARRAY_LIST.add(i);
            LINKED_LIST.add(i);
        }
    }

    @Benchmark
    public void arrayList_seqMap(Blackhole blackhole) {
        Traversable<Integer> traversable = ARRAY_LIST::forEach;
        blackhole.consume(traversable.map(i -> i * 2).toList().size());
    }

    @Benchmark
    public void arrayList_dollarMap(Blackhole blackhole) {
        blackhole.consume($.map(ARRAY_LIST, i -> i * 2).size());
    }

    @Benchmark
    public void arrayList_streamMap(Blackhole blackhole) {
        blackhole.consume(ARRAY_LIST.stream().map(i -> i * 2).toList().size());
    }

    @Benchmark
    public void arrayList_chainMap(Blackhole blackhole) {
        blackhole.consume(Chain.from(ARRAY_LIST).map(i -> i * 2).toList().size());
    }

    @Benchmark
    public void linkedList_seqMap(Blackhole blackhole) {
        Traversable<Integer> traversable = LINKED_LIST::forEach;
        blackhole.consume(traversable.map(i -> i * 2).toList().size());
    }

    @Benchmark
    public void linkedList_dollarMap(Blackhole blackhole) {
        blackhole.consume($.map(LINKED_LIST, i -> i * 2).size());
    }

    @Benchmark
    public void linkedList_streamMap(Blackhole blackhole) {
        blackhole.consume(LINKED_LIST.stream().map(i -> i * 2).toList().size());
    }

    @Benchmark
    public void linkedList_chainMap(Blackhole blackhole) {
        blackhole.consume(Chain.from(LINKED_LIST).map(i -> i * 2).toList().size());
    }

}
