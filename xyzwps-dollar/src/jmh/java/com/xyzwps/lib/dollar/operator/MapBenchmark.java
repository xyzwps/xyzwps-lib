package com.xyzwps.lib.dollar.operator;

import com.xyzwps.lib.dollar.seq.SeqChainFactory;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.List;

import static com.xyzwps.lib.dollar.Dollar.*;

@State(Scope.Benchmark)
public class MapBenchmark {

    static final List<Integer> ARRAY_LIST = new ArrayList<>();

    @Setup
    public void setup() {
        ARRAY_LIST.clear();
        for (int i = 0; i < 500000; i++) {
            ARRAY_LIST.add(i);
        }
    }

    static final int MEASURE_ITR = 7;
    static final int WARM_UP_ITR = 3;

    @Benchmark
    @Warmup(iterations = WARM_UP_ITR)
    @Measurement(iterations = MEASURE_ITR)
    public void seqMap(Blackhole blackhole) {
        var seq = SeqChainFactory.INSTANCE.from(ARRAY_LIST);
        blackhole.consume(seq.map(i -> i * 2 + 1).filter(i -> i % 3 == 0).size());
    }

    /*

    @Benchmark
    @Warmup(iterations = WARM_UP_ITR)
    @Measurement(iterations = MEASURE_ITR)
    public void generatorMap(Blackhole blackhole) {
        var gen = GeneratorChainFactory.INSTANCE.from(ARRAY_LIST);
        blackhole.consume(gen.map(i -> i * 2 + 1).filter(i -> i % 3 == 0).size());
    }

    @Benchmark
    @Warmup(iterations = WARM_UP_ITR)
    @Measurement(iterations = MEASURE_ITR)
    public void iteratorMap(Blackhole blackhole) {
        var itr = IteratorChainFactory.INSTANCE.from(ARRAY_LIST);
        blackhole.consume(itr.map(i -> i * 2 + 1).filter(i -> i % 3 == 0).size());
    }
    */

    @Benchmark
    @Warmup(iterations = WARM_UP_ITR)
    @Measurement(iterations = MEASURE_ITR)
    public void dollarMap(Blackhole blackhole) {
        blackhole.consume($.filter($.map(ARRAY_LIST, i -> i * 2 + 1), i -> i % 3 == 0).size());
    }

    @Benchmark
    @Warmup(iterations = WARM_UP_ITR)
    @Measurement(iterations = MEASURE_ITR)
    public void streamMap(Blackhole blackhole) {
        blackhole.consume(ARRAY_LIST.stream().map(i -> i * 2 + 1).filter(i -> i % 3 == 0).count());
    }
}
