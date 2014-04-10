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

import com.google.common.base.Preconditions;
import org.slf4j.Logger;

import javax.annotation.concurrent.ThreadSafe;

/**
 * A thread that registers an uncaught exception handler and logs details of the exception using SLF4J.
 */
@ThreadSafe
public class LoggingThread extends Thread
{
  /** The logger to use. */
  private final Logger log;

  /**
   * Constructor.
   * @param threadName The name to give to this thread.
   * @param log The logger to use.
   * @param target The runnable code block to execute.
   */
  public LoggingThread(String threadName, Logger log, Runnable target)
  {
    super(Preconditions.checkNotNull(target, "target cannot be null"));
    this.setName(Preconditions.checkNotNull(threadName, "threadName cannot be null"));

    this.log = Preconditions.checkNotNull(log, "log cannot be null");

    this.setUncaughtExceptionHandler(
        (t, e) -> LoggingThread.this.log.warn("Uncaught exception in thread {}", t.getName(), e));
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
  }
}
