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

import javax.annotation.concurrent.ThreadSafe;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

/**
 * A {@link Clock} implementation that delegates to an underlying clock. <p>
 *
 * This is most useful to use with {@code FixedClock}, by changing the fixed clock source
 * underneath. <p>
 *
 * This class is final and thread-safe; however despite the JDK requirements, it is not immutable
 * since the delegate can be changed (albeit in a thread-safe way).
 */
@ThreadSafe
public final class DelegatingClock extends Clock
{
  /** The clock used to service requests. */
  private Clock delegate;

  /**
   * Constructor.
   * @param delegate The clock used to service requests.
   */
  public DelegatingClock(Clock delegate)
  {
    this.delegate = Preconditions.checkNotNull(delegate, "delegate cannot be null");
  }

  /**
   * Updates the delegate clock.
   * @param delegate The new delegate.
   */
  public synchronized void setDelegate(Clock delegate)
  {
    this.delegate = Preconditions.checkNotNull(delegate, "delegate cannot be null");
  }

  /**
   * Gets the time-zone being used to create dates and times. <p>
   *
   * A clock will typically obtain the current instant and then convert that
   * to a date or time using a time-zone. This method returns the time-zone used.
   *
   * @return The time-zone being used to interpret instants, not null
   */
  @Override
  public synchronized ZoneId getZone()
  {
    return this.delegate.getZone();
  }

  /**
   * Returns a copy of this clock with a different time-zone. <p>
   *
   * A clock will typically obtain the current instant and then convert that
   * to a date or time using a time-zone. This method returns a clock with
   * similar properties but using a different time-zone.
   *
   * @param zone The time-zone to change to, not null.
   * @return A clock based on this clock with the specified time-zone, not null.
   */
  @Override
  public synchronized Clock withZone(final ZoneId zone)
  {
    return new DelegatingClock(this.delegate.withZone(zone));
  }

  /**
   * Gets the current instant of the clock. <p>
   *
   * This returns an instant representing the current instant as defined by the clock.
   *
   * @return the current instant from this clock, not null.
   */
  @Override
  public synchronized Instant instant()
  {
    return this.delegate.instant();
  }
}
