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
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.fatboyindustrial.omnium.Optionals.tryCall;
import static com.fatboyindustrial.omnium.Optionals.tryFlatCall;
import static com.fatboyindustrial.omnium.Optionals.tryFlatInvoke;
import static com.fatboyindustrial.omnium.Optionals.tryInvoke;
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
  public void testTryInvokeSupplier()
  {
    final String garbage = "garbage";
    final String valid = "22";

    assertThat(tryInvoke(() -> Integer.parseInt(garbage)), is(Optional.empty()));
    assertThat(tryInvoke(() -> Integer.parseInt(valid)), is(Optional.of(22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testTryInvokeFunction()
  {
    final String garbage = "garbage";
    final String valid = "22";

    assertThat(tryInvoke(garbage, Integer::parseInt), is(Optional.empty()));
    assertThat(tryInvoke(valid, Integer::parseInt), is(Optional.of(22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testTryInvokeBiFunction()
  {
    final String garbage = "garbage";
    final String valid = "22";

    assertThat(tryInvoke(garbage, 16, Integer::parseInt), is(Optional.empty()));
    assertThat(tryInvoke(valid, 16, Integer::parseInt), is(Optional.of(0x22)));
  }

  /**
   * Tests that present values are returned as present, and exceptions are returned as absent.
   */
  @Test
  public void testTryCall()
  {
    final String garbage = "garbage";
    final String valid = "java.lang.String";

    assertThat(tryCall(() -> Class.forName(garbage)), is(Optional.empty()));
    assertThat(tryCall(() -> Class.forName(valid)), is(Optional.of(String.class)));
  }

  /**
   * Tests that values are returned as-is, and exceptions are returned as absent.
   */
  @Test
  public void testTryFlatInvokeSupplier()
  {
    final Supplier<Optional<String>> returnsAbsent =
        () -> Stream.of("one", "two")
            .filter(str -> str.length() > 5)
            .findAny();

    final Supplier<Optional<String>> returnsValid =
        () -> Stream.of("one", "two")
            .filter(str -> str.equals("one"))
            .findAny();

    final Supplier<Optional<Integer>> throwsException =
        () -> Stream.of("one", "two")
            .map(Integer::parseInt)
            .findFirst();

    assertThat(tryFlatInvoke(returnsAbsent), is(Optional.empty()));
    assertThat(tryFlatInvoke(throwsException), is(Optional.empty()));

    assertThat(tryFlatInvoke(returnsValid), is(Optional.of("one")));
  }

  /**
   * Tests that present values are returned as-is, and exceptions are returned as absent.
   */
  @Test
  public void testTryFlatInvokeFunction()
  {
    final Function<Integer, Optional<String>> returnsAbsent =
        arg -> Stream.of("one", "two")
            .filter(str -> str.length() > arg)
            .findAny();

    final Function<Integer, Optional<String>> returnsValid =
        arg -> Stream.of("one", "two")
            .filter(str -> str.equals("one"))
            .findAny();

    final Function<Integer, Optional<Integer>> throwsException =
        arg -> Stream.of("one", "two")
            .map(Integer::parseInt)
            .findFirst();

    assertThat(tryFlatInvoke(5, returnsAbsent), is(Optional.empty()));
    assertThat(tryFlatInvoke(5, throwsException), is(Optional.empty()));

    assertThat(tryFlatInvoke(5, returnsValid), is(Optional.of("one")));
  }

  /**
   * Tests that present values are returned as-is, and exceptions are returned as absent.
   */
  @Test
  public void testTryFlatInvokeBiFunction()
  {
    final BiFunction<Integer, Integer, Optional<String>> returnsAbsent =
        (arg1, arg2) -> Stream.of("one", "two")
            .filter(str -> str.length() > arg1 + arg2)
            .findAny();

    final BiFunction<Integer, Integer, Optional<String>> returnsValid =
        (arg1, arg2) -> Stream.of("one", "two")
            .filter(str -> str.equals("one"))
            .findAny();

    final BiFunction<Integer, Integer, Optional<Integer>> throwsException =
        (arg1, arg2) -> Stream.of("one", "two")
            .map(Integer::parseInt)
            .findFirst();

    assertThat(tryFlatInvoke(2, 3, returnsAbsent), is(Optional.empty()));
    assertThat(tryFlatInvoke(2, 3, throwsException), is(Optional.empty()));

    assertThat(tryFlatInvoke(2, 3, returnsValid), is(Optional.of("one")));
  }

  /**
   * Tests that values are returned as-is, and exceptions are returned as absent.
   */
  @Test
  public void testTryFlatCall()
  {
    final String garbage = "garbage";
    final String valid = "java.lang.String";

    final Callable<Optional<Class<?>>> returnsAbsent = Optional::empty;
    final Callable<Optional<Class<?>>> returnsValid = () -> Optional.of(Class.forName(valid));
    final Callable<Optional<Class<?>>> throwsException = () -> Optional.of(Class.forName(garbage));

    assertThat(tryFlatCall(returnsAbsent), is(Optional.empty()));
    assertThat(tryFlatCall(throwsException), is(Optional.empty()));

    assertThat(tryFlatCall(returnsValid), is(Optional.of(String.class)));
  }
}
