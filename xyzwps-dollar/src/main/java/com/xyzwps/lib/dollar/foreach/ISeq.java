package com.xyzwps.lib.dollar.foreach;

import java.util.Objects;

/**
 * An immutable logical list.
 */
// TODO: 如何适应内存
// TODO: should be lazy
public interface ISeq<T> {
    /**
     * @return the first element in this seq or null if this is empty.
     */
    T first();

    /**
     * @return the rest part of this sequence except the first element,
     * or null if this is empty or has only one element
     */
    ISeq<T> rest();

    int count();

    static <T> T first(ISeq<T> seq) {
        return seq == null ? null : seq.first();
    }

    static <T> ISeq<T> rest(ISeq<T> seq) {
        return seq == null ? null : seq.rest();
    }

    static <T> ISeq<T> cons(T t, ISeq<T> seq) {
        Objects.requireNonNull(t);
        if (seq == null) {
            return new NoRestSeq<>(t);
        } else {
            return new ConsSeq<>(t, seq, seq.count() + 1);
        }
    }

    static <T> T next(ISeq<T> seq) {
        return first(rest(seq));
    }

    static <T> String string(ISeq<T> seq) {
        // TODO: 改成函数形式
        if (seq == null) return "()";

        var sb = new StringBuilder("(");
        ISeq<T> curr = seq;
        T currFirst = first(curr);
        if (currFirst == null) {
            sb.append(')');
            return sb.toString();
        }

        sb.append(currFirst);
        curr = rest(curr);

        while ((currFirst = first(curr)) != null) {
            sb.append(' ').append(currFirst);
            curr = rest(curr);
        }
        sb.append(')');
        return sb.toString();
    }

    /**
     * conjoin
     */
    @SafeVarargs
    static <T> ISeq<T> conj(ISeq<T> seq, T... elements) {
        var result = seq;
        for (T e : elements) {
            result = cons(e, result);
        }
        return result;
    }

    static <T> ISeq<T> into(ISeq<T> seq, ISeq<T> seq2) {
        var result = seq;
        var curr = seq2;
        T currFirst;
        while ((currFirst = first(curr)) != null) {
            result = cons(currFirst, result);
            curr = rest(curr);
        }
        return result;
    }

    static ISeq<Integer> range(int start, int end, int step) {
        return new IntegerRangeSeq(start, end, step);
    }

    static ISeq<Integer> range(int start, int end) {
        return range(start, end, 1);
    }

    static ISeq<Integer> range(int end) {
        return range(0, end, 1);
    }

    /**
     * Begins with a value <tt>x</tt> and continues forever, applying a function
     * <tt>f</tt> to each value to calculate the next.
     */
//    static <T> ISeq<T> iterate(Function<T, T> f, T x) {
//    TODO: iterate
//    }

        // TODO: cycle      This function takes a collection and cycles it infinitely.
        // TODO: interleave This functhion takes multiple collections and produces a new
        //                  collection that interleaves values from each collection until
        //                  one if the collections is exhausted.
        // TODO: interpose  This function returns a sequence with each of the elements of
        //                  the input collection segregated by a separator

        // TODO: get(At)
        // TODO: filter
        // TODO: takeWhile
        // TODO: dropWhile
        // TODO: join
        // TODO: split-at
        // TODO: split-with
        // TODO: every？
        // TODO: some
        // TODO: notEvery
        // TODO: notAny
        // TODO: map
        // TODO: reduce
        // TODO: reverse
        // TODO: sort
        // TODO: sortBy

        // TODO: take
        // TODO: repeat/2 repeat/1


    record NoRestSeq<T>(T first) implements ISeq<T> {
        @Override
        public ISeq<T> rest() {
            return null;
        }

        @Override
        public int count() {
            return 1;
        }
    }

    record ConsSeq<T>(T first, ISeq<T> rest, int count) implements ISeq<T> {
    }

    record IntegerRangeSeq(int start, int end, int step) implements ISeq<Integer> {

        // TODO: Increase | Decrease
        public IntegerRangeSeq {
            if (step == 0) {
                throw new IllegalArgumentException("Step cannot be zero!!");
            }
        }

        @Override
        public Integer first() {
            return end == start ? null : start;
        }

        @Override
        public ISeq<Integer> rest() {
            return end == start ? null : new IntegerRangeSeq(start + step, end, step);
        }

        @Override
        public int count() {
            return (end - start) / step;
        }
    }
}
