package com.xyzwps.lib.dollar.util;

/**
 * Throw when executing an unreachable branch.
 */
public class Unreachable extends RuntimeException {

    /**
     * Default constructor is ok.
     */
    public Unreachable() {
        super("The branch is unreachable. Maybe a BUG.");
    }
}