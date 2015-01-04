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

import javax.annotation.concurrent.Immutable;

/**
 * Static utility methods for working with exceptions. <p>
 *
 * The {@code unchecked} family of methods are designed to work like this:  <pre> {@code
 *
 *   import static com.fatboyindustrial.omnium.Exceptions.unchecked;
 *
 *   list.stream()
 *       .forEach(value -> unchecked(() -> {
 *         <code that can raise a checked exception>
 *       }));
 *
 * } </pre>
 */
@Immutable
public class Exceptions
{
  /**
   * Executes the given code block, which can raise a checked exception, re-throwing the exception
   * as a runtime exception if one occurs. <p>
   * @param b The code block.
   * @throws RuntimeException If any exception occurs.
   */
  public static void unchecked(final RaisingOperation b) throws RuntimeException
  {
    try
    {
      b.execute();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Executes the given code block, which can raise a checked exception, re-throwing the exception
   * as a runtime exception if one occurs.
   * @param b The code block.
   * @param <R> The return type.
   * @return The value returned by the code block.
   * @throws RuntimeException If any exception occurs.
   */
  public static <R> R unchecked(final RaisingSupplier<R> b) throws RuntimeException
  {
    try
    {
      return b.execute();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Executes the given function, which can raise a checked exception, re-throwing the exception
   * as a runtime exception if one occurs.
   * @param t The function parameter.
   * @param f The function.
   * @param <T> The parameter type.
   * @param <R> The return type.
   * @return The value returned by the function.
   * @throws RuntimeException If any exception occurs.
   */
  public static <T, R> R unchecked(final T t, final RaisingFunction<T, R> f) throws RuntimeException
  {
    try
    {
      return f.apply(t);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Executes the given bi-function, which can raise a checked exception, re-throwing the exception
   * as a runtime exception if one occurs.
   * @param t The first function parameter.
   * @param u The first function parameter.
   * @param f The function.
   * @param <T> The first parameter type.
   * @param <U> The second parameter type.
   * @param <R> The return type.
   * @return The value returned by the bi-function.
   * @throws RuntimeException If any exception occurs.
   */
  public static <T, U, R> R unchecked(final T t, final U u, final RaisingBiFunction<T, U, R> f) throws RuntimeException
  {
    try
    {
      return f.apply(t, u);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * A code block that can throw a checked exception.
   */
  @FunctionalInterface
  public static interface RaisingOperation
  {
    /**
     * Executes the code block.
     * @throws Exception If there's an exception.
     */
    public void execute() throws Exception;
  }

  /**
   * A code block that can throw a checked exception.
   * @param <R> The return type.
   */
  @FunctionalInterface
  public static interface RaisingSupplier< R>
  {
    /**
     * Executes the code block.
     * @return The result of the code block.
     * @throws Exception If there's an exception.
     */
    public R execute() throws Exception;
  }

  /**
   * A function that can throw a checked exception.
   * @param <T> The parameter type.
   * @param <R> The return type.
   */
  @FunctionalInterface
  public static interface RaisingFunction<T, R>
  {
    /**
     * Applies this function to the given argument.
     * @param t The function argument
     * @return The function result
     * @throws Exception If there's an exception.
     */
    public R apply(T t) throws Exception;
  }

  /**
   * A bi-function that can throw a checked exception.
   * @param <T> The first parameter type.
   * @param <U> The second parameter type.
   * @param <R> The return type.
   */
  @FunctionalInterface
  public static interface RaisingBiFunction<T, U, R>
  {
    /**
     * Applies this function to the given arguments.
     * @param t The first function argument
     * @param u The second function argument
     * @return The function result
     * @throws Exception If there's an exception.
     */
    public R apply(T t, U u) throws Exception;
  }
}
