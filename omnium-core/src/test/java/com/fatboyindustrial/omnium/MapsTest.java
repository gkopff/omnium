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
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for the {@link Maps} utility methods.
 */
public class MapsTest
{
  /**
   * Tests that the key transformation works for a generic {@link Map}.
   */
  @Test
  public void testMapKeyTransform()
  {
    final Map<String, String> input = new HashMap<>(ImmutableMap.of("1", "one", "2", "two"));
    final Map<Integer, String> expected = new HashMap<>(ImmutableMap.of(1, "one", 2, "two"));
    final Map<Integer, String> result = Maps.keyTransform(input, Integer::parseInt);

    assertThat(result, is(expected));
  }

  /**
   * Tests that the key transformation works for a {@link HashMap}.
   */
  @Test
  public void testHashMapKeyTransform()
  {
    final HashMap<String, String> input = new HashMap<>(ImmutableMap.of("1", "one", "2", "two"));
    final HashMap<Integer, String> expected = new HashMap<>(ImmutableMap.of(1, "one", 2, "two"));
    final HashMap<Integer, String> result = Maps.keyTransform(input, Integer::parseInt);

    assertThat(result, is(expected));
  }

  /**
   * Tests that the key transformation works for an {@link ImmutableMap}.
   */
  @Test
  public void testImmutableMapKeyTransform()
  {
    final ImmutableMap<String, String> input = ImmutableMap.of("1", "one", "2", "two");
    final ImmutableMap<Integer, String> expected = ImmutableMap.of(1, "one", 2, "two");
    final ImmutableMap<Integer, String> result = Maps.keyTransform(input, Integer::parseInt);

    assertThat(result, is(expected));
  }

  /**
   * Tests that the key transformation works for an {@link ImmutableSortedMap}.
   */
  @Test
  public void testImmutableSortedMapKeyTransform()
  {
    final ImmutableSortedMap<String, String> input = ImmutableSortedMap.of("1", "one", "2", "two");
    final ImmutableSortedMap<Integer, String> expected = ImmutableSortedMap.of(1, "one", 2, "two");
    final ImmutableSortedMap<Integer, String> result = Maps.keyTransform(input, Integer::parseInt);

    assertThat(result, is(expected));
  }
}