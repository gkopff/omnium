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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Either}.
 */
public class EitherTest
{
  /**
   * Tests that the expected non-null precondition check works.
   */
  @Test(expected =  NullPointerException.class)
  public void testPreconditionsNullValue()
  {
    new Either<String, String>(null, Optional.<String>empty());
  }

  /**
   * Tests that the expected non-null precondition check works.
   */
  @Test(expected =  NullPointerException.class)
  public void testPreconditionsNullError()
  {
    new Either<String, String>(Optional.<String>empty(), null);
  }

  /**
   * Tests that the expected valid arguments precondition check works.
   */
  @Test(expected =  IllegalArgumentException.class)
  public void testPreconditionsBothPresent()
  {
    new Either<>(Optional.of("value"), Optional.of("error"));
  }

  /**
   * Tests that the expected valid arguments precondition check works.
   */
  @Test(expected =  IllegalArgumentException.class)
  public void testPreconditionsBothAbsent()
  {
    new Either<>(Optional.<String>empty(), Optional.<String>empty());
  }

  /**
   * Tests that a valid value object can be constructed.
   */
  @Test
  public void testValidConstruction()
  {
    final Either<String, Integer> sut = Either.value("value");

    assertThat(sut.isError(), is(false));
    assertThat(sut.getValue(), is("value"));
  }

  /**
   * Tests that an error object can be constructed.
   */
  @Test
  public void testErrorConstruction()
  {
    final Either<String, Integer> sut = Either.error(-1);

    assertThat(sut.isError(), is(true));
    assertThat(sut.getError(), is(-1));
  }

  /**
   * Tests that the expected exception is raised if you attempt to get the value when one is not set.
   */
  @Test(expected = IllegalStateException.class)
  public void testExceptionErrorPresentGetValue()
  {
    Either.error(-1).getValue();
  }

  /**
   * Tests that the expected exception is raised if you attempt to get the error when one is not set.
   */
  @Test(expected = IllegalStateException.class)
  public void testExceptionValuePresentGetError()
  {
    Either.value("value").getError();
  }
}
