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

import com.google.common.collect.ImmutableSortedSet;
import org.junit.Test;

import java.util.stream.Collector;
import java.util.stream.Stream;

import static com.fatboyindustrial.omnium.ImmutableCollectors.toImmutableSortedSet;
import static java.util.stream.Collector.Characteristics.UNORDERED;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link ImmutableSortedSet} tests for {@link ImmutableCollectors}.
 */
public class ImmutableCollectorsImmutableSortedSetTest
{
  /**
   * Tests that characteristics are reported correctly.
   */
  @Test
  public void testCharacteristics()
  {
    final Collector<String, ImmutableSortedSet.Builder<String>, ImmutableSortedSet<String>> collector =
        toImmutableSortedSet();

    assertThat(collector.characteristics(), is(ImmutableSortedSet.of(UNORDERED)));
  }

  /**
   * Tests that the supplier produces an initial state immutable sorted set builder.
   */
  @Test
  public void testSupplier()
  {
    final Collector<String, ImmutableSortedSet.Builder<String>, ImmutableSortedSet<String>> collector =
        toImmutableSortedSet();

    assertThat(collector.supplier().get().build(), is(ImmutableSortedSet.of()));
  }

  /**
   * Tests that the accumulator behaves as expected.
   */
  @Test
  public void testAccumulator()
  {
    final Collector<String, ImmutableSortedSet.Builder<String>, ImmutableSortedSet<String>> collector =
        toImmutableSortedSet();

    final ImmutableSortedSet.Builder<String> builder = ImmutableSortedSet.naturalOrder();

    collector.accumulator().accept(builder, "one");
    assertThat(builder.build(), is(ImmutableSortedSet.of("one")));
    collector.accumulator().accept(builder, "two");
    assertThat(builder.build(), is(ImmutableSortedSet.of("one", "two")));
  }

  /**
   * Tests that the combiner correctly combines two builders.
   */
  @Test
  public void testCombiner()
  {
    final Collector<String, ImmutableSortedSet.Builder<String>, ImmutableSortedSet<String>> collector =
        toImmutableSortedSet();

    final ImmutableSortedSet.Builder<String> b1 = ImmutableSortedSet.naturalOrder();
    b1.add("b1.one");
    b1.add("b1.two");

    final ImmutableSortedSet.Builder<String> b2 = ImmutableSortedSet.naturalOrder();
    b2.add("b2.one");
    b2.add("b2.two");

    final ImmutableSortedSet.Builder<String> combined = collector.combiner().apply(b1, b2);

    assertThat(combined.build(), is(ImmutableSortedSet.of("b1.one", "b1.two", "b2.one", "b2.two")));
  }

  /**
   * Tests that the finisher correctly produces an immutable sorted set.
   */
  @Test
  public void testFinisher()
  {
    final Collector<String, ImmutableSortedSet.Builder<String>, ImmutableSortedSet<String>> collector =
        toImmutableSortedSet();

    final ImmutableSortedSet.Builder<String> builder = ImmutableSortedSet.naturalOrder();
    builder.add("one");
    builder.add("two");
    builder.add("three");

    assertThat(collector.finisher().apply(builder), is(ImmutableSortedSet.of("one", "two", "three")));
  }

  /**
   * Tests that use with a stream works as expected.
   */
  @Test
  public void testStream()
  {
    final Stream<String> stream = Stream.of("one", "two", "three", "four");

    assertThat(stream.collect(toImmutableSortedSet()), is(ImmutableSortedSet.of("one", "two", "three", "four")));
  }
}