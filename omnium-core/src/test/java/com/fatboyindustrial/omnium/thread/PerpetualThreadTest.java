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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests the {@link PerpetualThread}.
 */
public class PerpetualThreadTest
{
  private static final Logger LOG = LoggerFactory.getLogger(PerpetualThreadTest.class);

  /**
   * Tests that the expected exception is raised if we try to override the uncaught exception handler.
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testSetUncaughtExceptionHandler()
  {
    new PerpetualThread("name", LOG, () -> {}).setUncaughtExceptionHandler((t, e) -> {
    });
  }

  /**
   * Tests that getting the notice when no threads have died returns absent.
   */
  @Test
  public void testGetEmptyNotice()
  {
    final Optional<String> result = PerpetualThread.PerpetualThreadRegistry.get().getNotice();

    assertThat(result, is(Optional.<String>empty()));
  }

  /**
   * Tests that getting the notice when a thread has died returns the correct value.
   */
  @Test
  public void testGetPresentNotice() throws InterruptedException
  {
    final Thread t = new PerpetualThread("testGetPresentNotice", LOG, () -> {});
    t.start();
    t.join();

    final Optional<String> result = PerpetualThread.PerpetualThreadRegistry.get().getNotice();
    assertThat(result.isPresent(), is(true));
  }
}
