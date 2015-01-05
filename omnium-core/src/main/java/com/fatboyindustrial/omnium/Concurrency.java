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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Supplier;

/**
 * Utility methods for dealing with concurrency, threads, futures etc.
 */
@Immutable
public class Concurrency
{
  /**
   * Submits the given task to the supplied fork-join pool. We use this pool to execute parallel
   * stream tasks, as described here:
   * http://stackoverflow.com/a/22269778/1212960 and
   * http://blog.krecan.net/2014/03/18/how-to-specify-thread-pool-for-java-8-parallel-streams/ <p>
   *
   * Any interrupted or execution exception is thrown as a runtime exception. <p>
   *
   * Example:
   * <pre> {@code
   *   list = execUsingPool(pool, () -> list.stream().parallel().map(x -> ...).collect(...));
   * }</pre>
   * @param pool The thread pool to execute in.
   * @param block The block to execute.
   * @param <R> The type returned by {@code block}.
   * @return The value returned by {@code block}.
   */
  @SuppressWarnings("JavaDoc")    // JavaDoc is valid
  public static <R> R usingPool(final ForkJoinPool pool, final Supplier<R> block)
  {
    try
    {
      // We contort a supplier into a Callable so we don't have to deal with the checked exception externally.

      return pool.submit(block::get).get();
    }
    catch (InterruptedException | ExecutionException e)
    {
      throw new RuntimeException(e);
    }
  }

  /**
   * Submits the given task to the supplied fork-join pool. We use this pool to execute parallel
   * stream tasks, as described here:
   * http://stackoverflow.com/a/22269778/1212960 and
   * http://blog.krecan.net/2014/03/18/how-to-specify-thread-pool-for-java-8-parallel-streams/ <p>
   *
   * Any interrupted or execution exception is thrown as a runtime exception. <p>
   *
   * Example:
   * <pre> {@code
   *   execUsingPool(pool, () -> list.stream().parallel().forEach(x -> ...));
   * }</pre>
   * @param pool The thread pool to execute in.
   * @param block The block to execute.
   */
  @SuppressWarnings("JavaDoc")    // JavaDoc is valid
  public static void usingPool(final ForkJoinPool pool, final Runnable block)
  {
    usingPool(pool, (Supplier<Void>) () -> {
      block.run();
      return null;
    });
  }
}
