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
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;

import javax.annotation.concurrent.Immutable;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.UNORDERED;

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

  /**
   * Gets a collector that returns an {@link ImmutableSet}.
   * @param <T> The type of element in the set.
   * @return The collector.
   */
  public static <T> Collector<T, ImmutableSet.Builder<T>, ImmutableSet<T>> toImmutableSet()
  {
    return Collector.of(
        ImmutableSet::builder,
        (set, entry) -> set.add(entry),
        (builder1, builder2) -> builder1.addAll(builder2.build()),
        ImmutableSet.Builder::build,
        UNORDERED);
  }

  /**
   * Gets a collector that returns an {@link ImmutableSortedSet}.
   * @param <T> The type of element in the set.
   * @return The collector.
   */
  public static <T extends Comparable<T>>
  Collector<T, ImmutableSortedSet.Builder<T>, ImmutableSortedSet<T>> toImmutableSortedSet()
  {
    return Collector.of(
        ImmutableSortedSet::naturalOrder,
        (set, entry) -> set.add(entry),
        (builder1, builder2) -> builder1.addAll(builder2.build()),
        ImmutableSortedSet.Builder::build,
        UNORDERED);
  }

  /**
   * Gets a collector that returns an {@link ImmutableMap}.
   * @param <T> The original element type.
   * @param <K> The key type.
   * @param <V> The value type.
   * @param keyMapper Function to transform {@code T} to {@code K}.
   * @param valueMapper Function to transform {@code T} to {@code V}.
   * @return The collector.
   */
  public static <T, K, V> Collector<T, ImmutableMap.Builder<K, V>, ImmutableMap<K, V>> toImmutableMap(
      final Function<? super T, ? extends K> keyMapper,
      final Function<? super T, ? extends V> valueMapper)
  {
    return Collector.of(
        ImmutableMap::builder,
        (map, entry) -> map.put(keyMapper.apply(entry), valueMapper.apply(entry)),
        (builder1, builder2) -> builder1.putAll(builder2.build()),
        ImmutableMap.Builder::build,
        UNORDERED);
  }
}
