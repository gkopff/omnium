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

import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;

import static com.fatboyindustrial.omnium.matchers.OffsetDateTimeEqual.equalOffsetDateTime;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link DelegatingClock}.
 */
public class DelegatingClockTest
{
  /** Arbitrary instant in time used for testing. */
  private static final Instant INSTANT_1 = Instant.parse("2011-03-02T20:30:00.000Z");

  /** ... and the next day. */
  private static final Instant INSTANT_2 = Instant.parse("2011-03-03T20:30:00.000Z");

  /**
   * Tests that setting the delegate clock works.
   */
  @Test
  public void testSetDelegate()
  {
    final Clock fixed1 = Clock.fixed(INSTANT_1, ZoneId.of("Australia/Sydney"));
    final Clock fixed2 = Clock.fixed(INSTANT_2, ZoneId.of("Australia/Sydney"));

    final DelegatingClock clock = new DelegatingClock(fixed1);
    assertThat(clock.instant(), is(INSTANT_1));

    clock.setDelegate(fixed2);
    assertThat(clock.instant(), is(INSTANT_2));
  }

  /**
   * Tests that using the delegate clock works when used to create {@link OffsetDateTime}s.
   */
  @Test
  public void testUseDelegateOffsetDateTime()
  {
    final Clock fixed1 = Clock.fixed(INSTANT_1, ZoneId.of("Australia/Sydney"));
    final Clock fixed2 = Clock.fixed(INSTANT_2, ZoneId.of("Australia/Sydney"));

    final DelegatingClock clock = new DelegatingClock(fixed1);
    assertThat(OffsetDateTime.now(clock),
               is(equalOffsetDateTime(OffsetDateTime.ofInstant(INSTANT_1, ZoneId.of("Australia/Sydney")))));

    clock.setDelegate(fixed2);
    assertThat(OffsetDateTime.now(clock),
               is(equalOffsetDateTime(OffsetDateTime.ofInstant(INSTANT_2, ZoneId.of("Australia/Sydney")))));
  }
}
