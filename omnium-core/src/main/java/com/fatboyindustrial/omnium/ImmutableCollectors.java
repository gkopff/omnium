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

import com.google.common.collect.ImmutableList;

import javax.annotation.concurrent.Immutable;
import java.util.stream.Collector;

/**
 * A set of collectors that return Guava immutable collections.
 */
@Immutable
public class ImmutableCollectors
{
  /**
   * Gets a collector that returns an {@link ImmutableList}.
   * @param <T> The type of element in the list.
   * @return The collector.
   */
  public static <T> Collector<T, ImmutableList.Builder<T>, ImmutableList<T>> toImmutableList()
  {
    return Collector.of(
        ImmutableList::builder,
        (list, entry) -> list.add(entry),
        (builder1, builder2) -> builder1.addAll(builder2.build()),
        ImmutableList.Builder::build);
  }
}
