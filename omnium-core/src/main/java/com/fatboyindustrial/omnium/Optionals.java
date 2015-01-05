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

import javax.annotation.concurrent.Immutable;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility methods for dealing with optional values.
 */
@Immutable
public class Optionals
{
  /**
   * Converts a Guava optional to a JDK optional.
   * @param optional The Guava optional.
   * @param <T> The type contained in the optional.
   * @return The JDK optional.
   */
  public static <T> Optional<T> guavaToJdk(final com.google.common.base.Optional<T> optional)
  {
    return Optional.ofNullable(optional.orNull());
  }

  /**
   * Converts a JDK optional to a Guava optional.
   * @param optional The JDK optional.
   * @param <T> The type contained in the optional.
   * @return The Guava optional.
   */
  public static <T> com.google.common.base.Optional<T> jdkToGuava(final Optional<T> optional)
  {
    return com.google.common.base.Optional.fromNullable(optional.orElse(null));
  }

  /**
   * Executes {@code block}, returning the value as a present {@link Optional}.  If an exception is
   * raised, then an absent value is returned. This is designed to be used in situations like the
   * following, where an exception is routinely expected and no details about the exception need to
   * be retained: <pre> {@code
   *
   *   import static com.fatboyindustrial.omnium.Optionals.tryInvoke;
   *
   *   final Optional<Integer> result =
   *       tryInvoke(() -> Integer.parseInt(userInput));
   *
   * } </pre>
   * @param block The value-returning code block to execute.
   * @param <R> The return value type.
   * @return The result, wrapped in an {@link Optional}.
   */
  public static <R> Optional<R> tryInvoke(final Supplier<R> block)
  {
    return tryCall(block::get);
  }

  /**
   * Executes {@code block}, passing it {@code t}, returning the value as a present {@link Optional}.
   * If an exception is raised, then an absent value is returned.  For more details see {@link
   * #tryInvoke(Supplier)}.  Specifically, this version is used to invoke things like this: <pre> {@code
   *
   *   final Optional<Integer> result =
   *       tryInvoke(Integer::parseInt, userInput);
   *
   * } </pre>
   * @param <T> The parameter value type.
   * @param <R> The return value type.
   * @param t The parameter to pass to the code block.
   * @param block The value-returning code block to execute.
   * @return The result, wrapped in an {@link Optional}.
   */
  public static <T, R> Optional<R> tryInvoke(final T t, final Function<T, R> block)
  {
    return tryCall(() -> block.apply(t));
  }

  /**
   * Executes {@code block}, passing it {@code t} and {@code u}, returning the value as a present {@link Optional}.
   * If an exception is raised, then an absent value is returned.  For more details see {@link
   * #tryInvoke(Supplier)} and {@link #tryInvoke(Object, Function)}.
   * @param <T> The first parameter value type.
   * @param <U> The second parameter value type.
   * @param <R> The return value type.
   * @param t The first parameter to pass to the code block.
   * @param u The second parameter to pass to the code block.
   * @param block The value-returning code block to execute.
   * @return The result, wrapped in an {@link Optional}.
   */
  public static <T, U, R> Optional<R> tryInvoke(final T t, final U u, final BiFunction<T, U, R> block)
  {
    return tryCall(() -> block.apply(t, u));
  }

  /**
   * Executes {@code block}, returning the value as a present {@link Optional}.  If an exception is
   * raised, then an absent value is returned.  For more details see {@link #tryInvoke(Supplier)}.
   * @param block The value-returning code block to execute.
   * @param <R> The return value type.
   * @return The result, wrapped in an {@link Optional}.
   */
  public static <R> Optional<R> tryCall(final Callable<R> block)
  {
    try
    {
      return Optional.of(block.call());
    }
    catch (Exception e)
    {
      return Optional.empty();
    }
  }

  /**
   * Executes {@code block}, returning the {@link Optional} value.  If an exception is raised, then an
   * absent value is returned (i.e. the result is flat mapped).  For more details see {@link #tryInvoke(Supplier)}.
   * @param block The optional value returning code block to execute.
   * @param <R> The return value type.
   * @return The result.
   */
  public static <R> Optional<R> tryFlatInvoke(final Supplier<Optional<R>> block)
  {
    return tryFlatCall(block::get);
  }

  /**
   * Executes {@code block}, passing it {@code t}, returning the {@link Optional} value.
   * If an exception is raised, then an absent value is returned (i.e. the result is flat mapped).
   * For more details see {@link #tryFlatInvoke(Supplier)} and {@link #tryInvoke(Object, Function)}.
   * @param <T> The parameter value type.
   * @param <R> The return value type.
   * @param t The parameter to pass to the code block.
   * @param block The value-returning code block to execute.
   * @return The result, wrapped in an {@link Optional}.
   */
  public static <T, R> Optional<R> tryFlatInvoke(final T t, final Function<T, Optional<R>> block)
  {
    return tryFlatCall(() -> block.apply(t));
  }

  /**
   * Executes {@code block}, passing it {@code t} and {@code u}, returning the {@link Optional} value.
   * If an exception is raised, then an absent value is returned.  For more details see {@link
   * #tryFlatInvoke(Supplier)} and {@link #tryInvoke(Object, Function)}.
   * @param <T> The first parameter value type.
   * @param <U> The second parameter value type.
   * @param <R> The return value type.
   * @param t The first parameter to pass to the code block.
   * @param u The second parameter to pass to the code block.
   * @param block The optional value returning code block to execute.
   * @return The result, wrapped in an {@link Optional}.
   */
  public static <T, U, R> Optional<R> tryFlatInvoke(final T t, final U u, final BiFunction<T, U, Optional<R>> block)
  {
    return tryFlatCall(() -> block.apply(t, u));
  }

  /**
   * Executes {@code block}, returning the {@link Optional} value.  If an exception is raised, then an
   * absent value is returned (i.e. the result is flat mapped).  For more details see {@link #tryInvoke(Supplier)}.
   * @param block The value-returning code block to execute.
   * @param <R> The return value type.
   * @return The result.
   */
  public static <R> Optional<R> tryFlatCall(final Callable<Optional<R>> block)
  {
    try
    {
      return block.call();
    }
    catch (Exception e)
    {
      return Optional.empty();
    }
  }
}
