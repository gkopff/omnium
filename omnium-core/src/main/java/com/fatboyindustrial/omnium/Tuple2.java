/*
 * Copyright 2015 Greg Kopff
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.fatboyindustrial.omnium;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import javax.annotation.concurrent.Immutable;
import java.util.Map;
import java.util.function.Function;

/**
 * An immutable tuple of 2 elements.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 */
@Immutable
public interface Tuple2<A, B>
{
  /**
   * Gets the first element.
   * @return The first element.
   */
  public A first();

  /**
   * Gets the second element.
   * @return The second element.
   */
  public B second();

  /**
   * Creates a new tuple containing the given elements.
   * @param first The first element.
   * @param second The second element.
   * @param <A> The type of the first element.
   * @param <B> The type of the second element.
   * @return A new tuple.
   */
  public static <A, B> Tuple2<A, B> of(final A first, final B second)
  {
    Preconditions.checkNotNull(first, "first cannot be null");
    Preconditions.checkNotNull(second, "second cannot be null");

    return DefaultTuple2.of(first, second);
  }

  /**
   * Creates a new tuple from the given map entry.  The key is assigned to the first element and
   * the value is assigned to the second element.
   * @param entry The map entry.
   * @param <A> The type of the map key and first element.
   * @param <B> The type of the map value and second element.
   * @return A new tuple.
   */
  public static <A, B> Tuple2<A, B> from(final Map.Entry<A, B> entry)
  {
    Preconditions.checkNotNull(entry, "entry cannot be null");

    return of(entry.getKey(), entry.getValue());
  }

  /**
   * Applies the given function to the first element, returning a new tuple with the result.
   * @param function The function to apply to the first element.
   * @param <C> The type of the transformed first element.
   * @return A new tuple.
   */
  default public <C> Tuple2<C, B> mapFirst(final Function<A, C> function)
  {
    Preconditions.checkNotNull(function, "function cannot be null");

    return of(function.apply(first()), second());
  }

  /**
   * Applies the given function to the second element, returning a new tuple with the result.
   * @param function The function to apply to the second element.
   * @param <C> The type of the transformed second element.
   * @return A new tuple.
   */
  default public <C> Tuple2<A, C> mapSecond(final Function<B, C> function)
  {
    Preconditions.checkNotNull(function, "function cannot be null");

    return of(first(), function.apply(second()));
  }

  /**
   * Reverses the element order of the tuple, returning a new tuple with the result.
   * @return A new tuple.
   */
  default public Tuple2<B, A> reverse()
  {
    return of(second(), first());
  }

  /**
   * Gets this tuple as an immutable map, using the first element as the key and the second element
   * as the value.
   * @return An immutable map.
   */
  default public ImmutableMap<A, B> asImmutableMap()
  {
    return ImmutableMap.of(first(), second());
  }
}

