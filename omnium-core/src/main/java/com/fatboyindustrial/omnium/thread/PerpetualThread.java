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

package com.fatboyindustrial.omnium.thread;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;

import javax.annotation.concurrent.GuardedBy;
import javax.annotation.concurrent.ThreadSafe;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A thread that should continue to execute for the entire life-time of the application.  Thread deaths
 * (due to completion of the {@code run} method or due to an uncaught exception) are registered with a
 * singleton {@link PerpetualThreadRegistry}.  The {@code PerpetualThreadRegistry} tracks the reason for
 * a thread death so that reporting tools can extract this information.
 */
@ThreadSafe
public class PerpetualThread extends Thread
{
  /** The logger to use. */
  private final Logger log;

  /**
   * Constructor.
   * @param threadName The name to give to this thread.
   * @param log The logger to use.
   * @param target The runnable code block to execute.
   */
  public PerpetualThread(String threadName, Logger log, Runnable target)
  {
    super(Preconditions.checkNotNull(target, "target cannot be null"));
    this.setName(Preconditions.checkNotNull(threadName, "threadName cannot be null"));

    this.log = Preconditions.checkNotNull(log, "log cannot be null");

    super.setUncaughtExceptionHandler((t, e) -> {
      log.warn("Uncaught exception in perpetual thread '{}'", t.getName(), e);
      PerpetualThreadRegistry.get().addObituary(t, Optional.of(e));
    });
  }

  /**
   * Sets the uncaught exception handler for this thread.
   * @param handler The handler
   * @throws UnsupportedOperationException Perpetual threads cannot have custom uncaught exception handlers.
   */
  @Override
  public final void setUncaughtExceptionHandler(UncaughtExceptionHandler handler)
  {
    throw new UnsupportedOperationException("Perpetual threads cannot have custom uncaught exception handlers");
  }

  /**
   * If this thread was constructed using a separate {@link Runnable} run object, then that {@code Runnable}
   * object's run method is called; otherwise, this method does nothing and returns.
   */
  @Override
  public void run()
  {
    this.log.trace("run(): thread spawned");

    super.run();

    this.log.trace("run(): thread exited");

    PerpetualThreadRegistry.get().addObituary(this, Optional.absent());
  }

  /**
   * A singleton registry of perpetual threads, including details of any that have died.
   */
  @SuppressWarnings("WeakerAccess")
  @ThreadSafe
  public static class PerpetualThreadRegistry
  {
    /** Singleton instance. */
    private static final PerpetualThreadRegistry INSTANCE = new PerpetualThreadRegistry();

    /** Dead thread details. */
    @GuardedBy("this")
    private final Map<Thread, Optional<Throwable>> obituaries;

    /**
     * Constructor.
     */
    private PerpetualThreadRegistry()
    {
      this.obituaries = new HashMap<>();
    }

    /**
     * Gets the singleton instance.
     * @return The perpetual thread registry.
     */
    public static PerpetualThreadRegistry get()
    {
      return INSTANCE;
    }

    /**
     * Gets the details of the dead threads as a single string, suitable for reporting to a monitoring
     * interface.
     * @return The obituaries, rendered as a String, or {@link Optional#absent()} if there are no deaths.
     */
    public synchronized Optional<String> getNotice()
    {
      if (this.obituaries.isEmpty())
      {
        return Optional.absent();
      }

      //noinspection ThrowableResultOfMethodCallIgnored
      return Optional.of(this.obituaries.entrySet().stream().map(
          (entry) -> entry.getKey().getName() + " " +
                     (entry.getValue().isPresent() ? "died from " + entry.getValue().get()
                                                   : "completed the run() method")
      ).collect(Collectors.joining("; ")));
    }

    /**
     * Adds an obituary for the given thread.
     * @param thread The thread that died.
     * @param throwable An optional exception that caused the death.
     */
    private synchronized void addObituary(Thread thread, Optional<Throwable> throwable)
    {
      this.obituaries.put(thread, throwable);
    }
  }
}
