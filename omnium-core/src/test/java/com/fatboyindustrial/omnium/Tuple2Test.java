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
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Tuple2} and {@link DefaultTuple2}.
 */
public class Tuple2Test
{
  /**
   * Tests that the static factory method fields are round-tripped.
   */
  @Test
  public void testRoundTrip()
  {
    final Tuple2<String, Integer> tuple = Tuple2.of("Zaphod", 42);

    assertThat(tuple.first(), is("Zaphod"));
    assertThat(tuple.second(), is(42));
  }

  /**
   * Tests that a tuple is correctly constructed from a Map.Entry.
   */
  @Test
  public void testFromMapEntry()
  {
    final Map.Entry<String, Integer> entry = ImmutableMap.of("Zaphod", 42).entrySet().iterator().next();
    final Tuple2<String, Integer> tuple = Tuple2.from(entry);

    assertThat(tuple.first(), is("Zaphod"));
    assertThat(tuple.second(), is(42));
  }

  /**
   * Tests that the mapping function applied to the first element produces the expected new tuple and
   * that the first tuple is unchanged.
   */
  @Test
  public void testMapFirst()
  {
    final Tuple2<String, Integer> original = Tuple2.of("Zaphod", 42);
    final Tuple2<String, Integer> derived = original.mapFirst(String::toUpperCase);

    assertThat(original.first(), is("Zaphod"));
    assertThat(original.second(), is(42));

    assertThat(derived.first(), is("ZAPHOD"));
    assertThat(derived.second(), is(42));
  }

  /**
   * Tests that the mapping function applied to the second element produces the expected new tuple and
   * that the original tuple is unchanged.
   */
  @Test
  public void testMapSecond()
  {
    final Tuple2<String, Integer> original = Tuple2.of("Zaphod", 42);
    final Tuple2<String, Integer> derived = original.mapSecond(i -> i * -1);

    assertThat(original.first(), is("Zaphod"));
    assertThat(original.second(), is(42));

    assertThat(derived.first(), is("Zaphod"));
    assertThat(derived.second(), is(-42));
  }

  /**
   * Tests that the reverse method produces a new tuple with the elements swapped, and that original
   * tuple is left unchanged.
   */
  @Test
  public void testReverse()
  {
    final Tuple2<String, Integer> original = Tuple2.of("Zaphod", 42);
    final Tuple2<Integer, String> derived = original.reverse();

    assertThat(original.first(), is("Zaphod"));
    assertThat(original.second(), is(42));

    assertThat(derived.first(), is(42));
    assertThat(derived.second(), is("Zaphod"));
  }

  /**
   * Tests that the tuple can be converted to an immutable map.
   */
  @Test
  public void testAsImmutableMap()
  {
    final Tuple2<String, Integer> tuple = Tuple2.of("Zaphod", 42);

    assertThat(tuple.asImmutableMap(), is(ImmutableMap.of("Zaphod", 42)));
  }

  /**
   * Tests that the equals and hash code implementations are sane.
   */
  @SuppressWarnings({ "ObjectEqualsNull", "EqualsBetweenInconvertibleTypes" })
  @Test
  public void testEqualsAndHashCode()
  {
    final Tuple2<String, Integer> t1 = Tuple2.of("Zaphod", 42);
    final Tuple2<String, Integer> sameAsT1 = Tuple2.of("Zaphod", 42);
    final Tuple2<Character, Double> totallyDifferent = Tuple2.of('Z', 42.0);
    final Tuple2<String, Integer> sameFirst = Tuple2.of("Zaphod", 99);
    final Tuple2<String, Integer> sameSecond = Tuple2.of("Beeblebrox", 42);

    assertThat(t1, is(t1));
    assertThat(t1, is(sameAsT1));
    assertThat(sameAsT1, is(t1));
    assertThat(t1.hashCode(), is(sameAsT1.hashCode()));

    assertThat(t1, is(not(sameFirst)));
    assertThat(sameFirst, is(not(t1)));

    assertThat(t1, is(not(sameSecond)));
    assertThat(sameSecond, is(not(t1)));

    assertThat(t1, is(not(totallyDifferent)));
    assertThat(totallyDifferent, is(not(t1)));

    assertThat(t1.equals("A string"), is(false));
    assertThat(t1.equals(null), is(false));
  }

  /**
   * Tests that that toString method executes without exception.
   */
  @Test
  public void testToString()
  {
    final Tuple2<String, Integer> tuple = Tuple2.of("Zaphod", 42);

    assertThat(tuple.toString(), is("(Zaphod, 42)"));
  }

  /**
   * Tests that the tuple can be serialised and deserialised.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testSerialisation() throws Exception
  {
    final Tuple2<String, Integer> original = Tuple2.of("Zaphod", 42);

    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);

    oos.writeObject(original);
    oos.close();

    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    final ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);

    final Tuple2<String, Integer> reconstituted = (Tuple2<String, Integer>) ois.readObject();

    assertThat(original, is(reconstituted));
  }
}