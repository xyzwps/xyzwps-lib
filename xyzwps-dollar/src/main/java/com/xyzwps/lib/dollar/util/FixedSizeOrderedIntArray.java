package com.xyzwps.lib.dollar.util;

import com.xyzwps.lib.dollar.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Size-fixed ordered int array.
 */
public class FixedSizeOrderedIntArray {
    private final int cap;
    private final Direction direction;
    private int count;
    private final int[] array;

    /**
     * Constructor.
     *
     * @param cap       array size
     * @param direction order direction
     */
    public FixedSizeOrderedIntArray(final int cap, Direction direction) {
        if (cap < 1) {
            throw new IllegalArgumentException("cap cannot be less than 1");
        }
        this.cap = cap;
        this.direction = Objects.requireNonNull(direction);
        this.count = 0;
        this.array = new int[cap];
    }

    /**
     * Add an int value in-order.
     *
     * @param v to be added
     */
    public void add(int v) {
        int at = findInsertAt(v);
        if (count < cap) {
            if (at < count) {
                System.arraycopy(array, at, array, at + 1, count - at);
            }
            array[at] = v;
            count++;
        } else {
            if (at == count) {
                return;
            }
            System.arraycopy(array, at, array, at + 1, count - at - 1);
            array[at] = v;
        }
    }

    private int findInsertAt(int v) {
        if (count == 0) {
            return 0;
        }

        switch (direction) {
            case ASC -> {
                for (int i = 0; i < count; i++) {
                    if (array[i] > v) return i; // TODO: 改成二分搜索
                }
            }
            case DESC -> {
                for (int i = 0; i < count; i++) {
                    if (array[i] < v) return i; // TODO: 改成二分搜索
                }
            }
            default -> throw new IllegalStateException("Impossible");
        }

        return count;
    }

    /**
     * Create a new list containing all array elements
     *
     * @return new list containing all array elements
     */
    public List<Integer> toList() {
        var list = new ArrayList<Integer>();
        for (int i = 0; i < count; i++) {
            list.add(array[i]);
        }
        return list;
    }
}
