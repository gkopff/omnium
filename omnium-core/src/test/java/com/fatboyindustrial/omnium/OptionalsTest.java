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

import org.junit.Test;

import java.util.Optional;

import static com.fatboyindustrial.omnium.Optionals.absentOnException;
import static com.fatboyindustrial.omnium.Optionals.flatAbsentOnException;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Optional}.
 */
public class OptionalsTest
{
  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testAbsentOnExceptionSupplier()
  {
    final String absent = "garbage";
    final String present = "22";

    assertThat(absentOnException(() -> Integer.parseInt(absent)), is(Optional.empty()));
    assertThat(absentOnException(() -> Integer.parseInt(present)), is(Optional.of(22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @SuppressWarnings("Convert2MethodRef")
  @Test
  public void testFlatAbsentOnExceptionSupplier()
  {
    final String absent = "garbage";
    final String present = "22";

    assertThat(flatAbsentOnException(() -> Optional.of(Integer.parseInt(absent))), is(Optional.empty()));
    assertThat(flatAbsentOnException(() -> Optional.empty()), is(Optional.empty()));
    assertThat(flatAbsentOnException(() -> Optional.of(Integer.parseInt(present))), is(Optional.of(22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testAbsentOnExceptionFunction()
  {
    final String absent = "garbage";
    final String present = "22";

    assertThat(absentOnException(Integer::parseInt, absent), is(Optional.empty()));
    assertThat(absentOnException(Integer::parseInt, present), is(Optional.of(22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testFlatAbsentOnExceptionFunction()
  {
    final String absent = "garbage";
    final String present = "22";

    assertThat(flatAbsentOnException(str -> Optional.of(Integer.parseInt(str)), absent), is(Optional.empty()));
    assertThat(flatAbsentOnException(str -> Optional.empty(), present), is(Optional.empty()));
    assertThat(flatAbsentOnException(str -> Optional.of(Integer.parseInt(str)), present), is(Optional.of(22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testAbsentOnExceptionBiFunction()
  {
    final String absent = "garbage";
    final String present = "22";

    assertThat(absentOnException(Integer::parseInt, absent, 16), is(Optional.empty()));
    assertThat(absentOnException(Integer::parseInt, present, 16), is(Optional.of(0x22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testFlatAbsentOnExceptionBiFunction()
  {
    final String absent = "garbage";
    final String present = "22";

    assertThat(flatAbsentOnException((str, radix) -> Optional.of(Integer.parseInt(str, radix)), absent, 16), is(Optional.empty()));
    assertThat(flatAbsentOnException((str, radix) -> Optional.empty(), present, 16), is(Optional.empty()));
    assertThat(flatAbsentOnException((str, radix) -> Optional.of(Integer.parseInt(str, radix)), present, 16), is(Optional.of(0x22)));
  }
}