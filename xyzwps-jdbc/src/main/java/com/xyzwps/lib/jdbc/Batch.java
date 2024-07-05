package com.xyzwps.lib.jdbc;

import java.lang.annotation.*;

/**
 * Batching executing, which requires that
 * <ol>
 *  <li>method returns void</li>
 *  <li>method accepts a {@link java.util.List}</li>
 * </ol>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Batch {
}
