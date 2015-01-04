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

import com.fatboyindustrial.omnium.Exceptions.RaisingBiFunction;
import com.fatboyindustrial.omnium.Exceptions.RaisingFunction;
import com.fatboyindustrial.omnium.Exceptions.RaisingOperation;
import com.fatboyindustrial.omnium.Exceptions.RaisingSupplier;
import org.junit.Test;

import java.io.IOException;

import static com.fatboyindustrial.omnium.Exceptions.unchecked;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Tests for {@link Exceptions}.
 */
public class ExceptionsTest
{
  /**
   * Tests that {@link Exceptions#unchecked(RaisingOperation)} executes correctly.
   */
  @Test
  public void testUncheckedRaisingOperation()
  {
    try
    {
      unchecked((RaisingOperation) () -> {
        throw new IOException("File not found");
      });

      fail("Expected exception not thrown");
    }
    catch (RuntimeException e)
    {
      assertThat(e.getCause().getClass().equals(IOException.class), is(true));
    }
  }

  /**
   * Tests that {@link Exceptions#unchecked(RaisingSupplier)} executes correctly.
   */
  @Test
  public void testUncheckedRaisingSupplier()
  {
    try
    {
      //noinspection UnusedDeclaration
      final String foo = unchecked((RaisingSupplier<String>) () -> {
        throw new IOException("File not found");
      });

      fail("Expected exception not thrown");
    }
    catch (RuntimeException e)
    {
      assertThat(e.getCause().getClass().equals(IOException.class), is(true));
    }
  }

  /**
   * Tests that {@link Exceptions#unchecked(Object,RaisingFunction)} executes correctly.
   */
  @Test
  public void testUncheckedRaisingFunction()
  {
    try
    {
      //noinspection UnusedDeclaration
      final String foo = unchecked("Space cookies!", (RaisingFunction<String, String>) (input) -> {
        throw new IOException("File not found");
      });

      fail("Expected exception not thrown");
    }
    catch (RuntimeException e)
    {
      assertThat(e.getCause().getClass().equals(IOException.class), is(true));
    }
  }

  /**
   * Tests that {@link Exceptions#unchecked(Object,Object,RaisingBiFunction)} executes correctly.
   */
  @Test
  public void testUncheckedRaisingBiFunction()
  {
    try
    {
      //noinspection UnusedDeclaration
      final String foo = unchecked("Space cookies!", 42, (RaisingBiFunction<String, Integer, String>) (i1, i2) -> {
        throw new IOException("File not found");
      });

      fail("Expected exception not thrown");
    }
    catch (RuntimeException e)
    {
      assertThat(e.getCause().getClass().equals(IOException.class), is(true));
    }
  }
}