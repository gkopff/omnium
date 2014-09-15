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
import java.util.function.BiFunction;
import java.util.function.Consumer;
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
   * Execute the block, returning the value.  If an exception is raised, return an absent value.
   * @param block The block to execute.
   * @param <T> The return value type.
   * @return The return value wrapped in an optional, or absent if an exception was raised.
   */
  public static <T> Optional<T> absentOnException(final Supplier<T> block)
  {
    return absentOnException(block, e -> {});
  }

  /**
   * Execute the block, returning the value.  If an exception is raised, return an absent value and
   * execute the {@code exceptionBlock} for its side-effects (typically logging a message).
   * @param block The block to execute.
   * @param exceptionBlock The exception block to execute.
   * @param <T> The return value type.
   * @return The return value wrapped in an optional, or absent if an exception was raised.
   */
  public static <T> Optional<T> absentOnException(final Supplier<T> block,
                                                  final Consumer<Exception> exceptionBlock)
  {
    try
    {
      return Optional.of(block.get());
    }
    catch (Exception e)
    {
      exceptionBlock.accept(e);
      return Optional.empty();
    }
  }

  /**
   * Execute the block, returning the value.  If an exception is raised, return an absent value.
   * @param block The block to execute.
   * @param <T> The return value type.
   * @return The return value (already an optional), or absent if an exception was raised.
   */
  public static <T> Optional<T> flatAbsentOnException(final Supplier<Optional<T>> block)
  {
    return flatAbsentOnException(block, e -> {});
  }

  /**
   * Execute the block, returning the value.  If an exception is raised, return an absent value and
   * execute the {@code exceptionBlock} for its side-effects (typically logging a message).
   * @param block The block to execute.
   * @param exceptionBlock The exception block to execute.
   * @param <T> The return value type.
   * @return The return value (already an optional), or absent if an exception was raised.
   */
  public static <T> Optional<T> flatAbsentOnException(final Supplier<Optional<T>> block,
                                                      final Consumer<Exception> exceptionBlock)
  {
    try
    {
      return block.get();
    }
    catch (Exception e)
    {
      exceptionBlock.accept(e);
      return Optional.empty();
    }
  }

  /**
   * Execute the block, returning the value.  If an exception is raised, return an absent value.
   * @param block The block to execute.
   * @param t A function argument.
   * @param <T> The return value type.
   * @return The return value wrapped in an optional, or absent if an exception was raised.
   */
  public static <T, R> Optional<R> absentOnException(final Function<T, R> block, final T t)
  {
    try
    {
      return Optional.of(block.apply(t));
    }
    catch (Exception e)
    {
      return Optional.empty();
    }
  }

  /**
   * Execute the block, returning the value.  If an exception is raised, return an absent value.
   * @param block The block to execute.
   * @param t A function argument.
   * @param <T> The return value type.
   * @return The return value (already an optional), or absent if an exception was raised.
   */
  public static <T, R> Optional<R> flatAbsentOnException(final Function<T, Optional<R>> block, final T t)
  {
    try
    {
      return block.apply(t);
    }
    catch (Exception e)
    {
      return Optional.empty();
    }
  }

  /**
   * Execute the block, returning the value.  If an exception is raised, return an absent value.
   * @param block The block to execute.
   * @param t The first function argument.
   * @param u The second function argument.
   * @param <T> The return value type.
   * @return The return value wrapped in an optional, or absent if an exception was raised.
   */
 public static <T, U, R> Optional<R> absentOnException(final BiFunction<T, U, R> block, final T t, final U u)
  {
    try
    {
      return Optional.of(block.apply(t, u));
    }
    catch (Exception e)
    {
      return Optional.empty();
    }
  }

  /**
   * Execute the block, returning the value.  If an exception is raised, return an absent value.
   * @param block The block to execute.
   * @param t The first function argument.
   * @param u The second function argument.
   * @param <T> The return value type.
   * @return The return value (already an optional), or absent if an exception was raised.
   */
  public static <T, U, R> Optional<R> flatAbsentOnException(final BiFunction<T, U, Optional<R>> block, final T t, final U u)
  {
    try
    {
      return block.apply(t, u);
    }
    catch (Exception e)
    {
      return Optional.empty();
    }
  }
}
