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

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.Test;

import java.util.stream.Collector;
import java.util.stream.Stream;

import static com.fatboyindustrial.omnium.ImmutableCollectors.toImmutableMap;
import static java.util.stream.Collector.Characteristics.UNORDERED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link ImmutableMap} tests for {@link ImmutableCollectors}.
 */
public class ImmutableCollectorsImmutableMapTest
{
  /**
   * Tests that characteristics are reported correctly.
   */
  @Test
  public void testCharacteristics()
  {
    final Collector<Character, ImmutableMap.Builder<String, Integer>, ImmutableMap<String, Integer>> collector =
        toImmutableMap(
            String::valueOf,
            c -> (int) c);

    assertThat(collector.characteristics(), is(ImmutableSet.of(UNORDERED)));
  }

  /**
   * Tests that the supplier produces an initial state immutable map builder.
   */
  @Test
  public void testSupplier()
  {
    final Collector<Character, ImmutableMap.Builder<String, Integer>, ImmutableMap<String, Integer>> collector =
        toImmutableMap(
            String::valueOf,
            c -> (int) c);

    assertThat(collector.supplier().get().build(), is(ImmutableMap.of()));
  }

  /**
   * Tests that the accumulator behaves as expected.
   */
  @Test
  public void testAccumulator()
  {
    final Collector<Character, ImmutableMap.Builder<String, Integer>, ImmutableMap<String, Integer>> collector =
        toImmutableMap(
            String::valueOf,
            c -> (int) c);

    final ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();

    collector.accumulator().accept(builder, 'a');
    assertThat(builder.build(), is(ImmutableMap.of("a", 0x61)));
    collector.accumulator().accept(builder, 'b');
    assertThat(builder.build(), is(ImmutableMap.of("a", 0x61, "b", 0x62)));
  }

  /**
   * Tests that the combiner correctly combines two builders.
   */
  @Test
  public void testCombiner()
  {
    final Collector<Character, ImmutableMap.Builder<String, Integer>, ImmutableMap<String, Integer>> collector =
        toImmutableMap(
            String::valueOf,
            c -> (int) c);

    final ImmutableMap.Builder<String, Integer> b1 = ImmutableMap.builder();
    b1.put("a", 0x61);
    b1.put("c", 0x63);

    final ImmutableMap.Builder<String, Integer> b2 = ImmutableMap.builder();
    b1.put("b", 0x62);
    b1.put("d", 0x64);

    final ImmutableMap.Builder<String, Integer> combined = collector.combiner().apply(b1, b2);

    assertThat(combined.build(), is(ImmutableMap.of(
        "a", 0x61,
        "b", 0x62,
        "c", 0x63,
        "d", 0x64)));
  }

  /**
   * Tests that the finisher correctly produces an immutable map.
   */
  @Test
  public void testFinisher()
  {
    final Collector<Character, ImmutableMap.Builder<String, Integer>, ImmutableMap<String, Integer>> collector =
        toImmutableMap(
            String::valueOf,
            c -> (int) c);

    final ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
    builder.put("a", 0x61);
    builder.put("c", 0x63);
    builder.put("b", 0x62);
    builder.put("d", 0x64);

    assertThat(collector.finisher().apply(builder), is(ImmutableMap.of(
        "a", 0x61,
        "b", 0x62,
        "c", 0x63,
        "d", 0x64)));
  }

  /**
   * Tests that use with a stream works as expected.
   */
  @Test
  public void testStream()
  {
    final Stream<String> stream = Stream.of("one", "two", "three", "four");

    assertThat(
        stream.collect(toImmutableMap(String::toUpperCase, String::length)),
        is(ImmutableMap.of(
            "ONE", 3,
            "TWO", 3,
            "THREE", 5,
            "FOUR", 4)));
  }
}