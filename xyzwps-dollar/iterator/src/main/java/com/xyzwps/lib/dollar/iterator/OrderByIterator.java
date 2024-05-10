package com.xyzwps.lib.dollar.iterator;

import com.xyzwps.lib.dollar.Direction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.function.Function;

import static com.xyzwps.lib.dollar.util.Comparators.ascComparator;
import static com.xyzwps.lib.dollar.util.Comparators.descComparator;

class OrderByIterator<T, K extends Comparable<K>> implements Iterator<T> {

    private final Iterator<T> up;
    private final Function<T, K> toKey;
    private final Direction direction;

    OrderByIterator(Iterator<T> up, Function<T, K> toKey, Direction direction) {
        this.up = up;
        this.toKey = toKey;
        this.direction = direction;
    }

    private Iterator<T> sorted;

    private void tryToSort() {
        if (sorted == null) {
            var list = new ArrayList<T>();
            while (up.hasNext()) {
                list.add(up.next());
            }
            Comparator<T> comparator = direction == Direction.DESC ? descComparator(toKey) : ascComparator(toKey);
            list.sort(comparator);
            sorted = list.iterator();
        }
    }

    @Override
    public boolean hasNext() {
        tryToSort();
        return sorted.hasNext();
    }

    @Override
    public T next() {
        tryToSort();
        return sorted.next();
    }
}
