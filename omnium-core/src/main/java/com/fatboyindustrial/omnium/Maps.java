/*
 * Copyright 2014 Greg Kopff
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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

import javax.annotation.concurrent.Immutable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Utility methods for dealing with maps.
 */
@Immutable
public class Maps
{
  /**
   * Transforms the given map by applying a function to each of the keys.
   * @param input The input map.
   * @param transform The transformation function to apply to the key.
   * @param <K1> The original key type.
   * @param <V> The map value type.
   * @param <K2> The transformed key type.
   * @return The new map (backed by {@link HashMap}).
   */
  public static <K1, V, K2> Map<K2, V> keyTransform(final Map<K1, V> input,
                                                    final Function<K1, K2> transform)
  {
    final HashMap<K2, V> result = new HashMap<>();
    input.forEach((key, value) -> result.put(transform.apply(key), value));
    return result;
  }

  /**
   * Transforms the given map by applying a function to each of the keys.
   * @param input The input map.
   * @param transform The transformation function to apply to the key.
   * @param <K1> The original key type.
   * @param <V> The map value type.
   * @param <K2> The transformed key type.
   * @return The new map.
   */
  public static <K1, V, K2> HashMap<K2, V> keyTransform(final HashMap<K1, V> input,
                                                        final Function<K1, K2> transform)
  {
    final HashMap<K2, V> result = new HashMap<>();
    input.forEach((key, value) -> result.put(transform.apply(key), value));
    return result;
  }

  /**
   * Transforms the given map by applying a function to each of the keys.
   * @param input The input map.
   * @param transform The transformation function to apply to the key.
   * @param <K1> The original key type.
   * @param <V> The map value type.
   * @param <K2> The transformed key type.
   * @return The new map.
   */
  public static <K1, V, K2> ImmutableMap<K2, V> keyTransform(final ImmutableMap<K1, V> input,
                                                             final Function<K1, K2> transform)
  {
    final ImmutableMap.Builder<K2, V> builder = ImmutableMap.builder();
    input.forEach((key, value) -> builder.put(transform.apply(key), value));
    return builder.build();
  }

  /**
   * Transforms the given map by applying a function to each of the keys.
   * @param input The input map.
   * @param transform The transformation function to apply to the key.
   * @param <K1> The original key type.
   * @param <V> The map value type.
   * @param <K2> The transformed key type.
   * @return The new map.
   */
  public static <K1 extends Comparable<K1>, V, K2 extends Comparable<K2>> ImmutableSortedMap<K2, V>
      keyTransform(final ImmutableSortedMap<K1, V> input, final Function<K1, K2> transform)
  {
    final ImmutableSortedMap.Builder<K2, V> builder = ImmutableSortedMap.naturalOrder();
    input.forEach((key, value) -> builder.put(transform.apply(key), value));
    return builder.build();
  }

  /**
   * Returns an immutable map for which the {@link Map#values} are the given elements in the given
   * order, and each key is the product of invoking a supplied function on its corresponding value.
   *
   * @param values The values to use when constructing the {@code Map}.
   * @param keyFunction The function used to produce the key for each value.
   * @param <K> The key type.
   * @param <V> The value type.
   * @return A map mapping the result of evaluating the function {@code keyFunction} on each value
   *         in the input collection to that value.
   * @throws IllegalArgumentException If {@code keyFunction} produces the same key for more than
   *                                  one value in the input collection.
   */
  public static <K, V> ImmutableMap<K, V> uniqueIndex(final Iterable<V> values,
                                                      final Function<? super V, K> keyFunction)
  {
    return com.google.common.collect.Maps.uniqueIndex(values, keyFunction::apply);
  }

  /**
   * Returns a view of a map where each value is transformed by a function. All
   * other properties of the map, such as iteration order, are left intact.
   * @param fromMap The input map.
   * @param function The transformation function to apply to the value.
   * @param <K> The key type.
   * @param <V1> The original value type.
   * @param <V2> The new value type.
   * @return A map with values transformed by the function.
   * @see com.google.common.collect.Maps#transformValues(Map, com.google.common.base.Function)
   */
  public static <K, V1, V2> Map<K, V2> transformValues(final Map<K, V1> fromMap,
                                                       final Function<? super V1, V2> function)
  {
    return com.google.common.collect.Maps.transformValues(fromMap, function::apply);
  }
}
