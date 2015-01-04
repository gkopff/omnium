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
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static com.fatboyindustrial.omnium.Times.fromNow;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Times}.
 */
public class TimesTest
{
  /**
   * Tests that the fromNow() method correctly formats the time differences.
   */
  @Test
  public void testFromNow()
  {
    final Clock clock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
    final Instant now = Instant.now(clock);

    assertThat(fromNow(clock, now), is("now"));

    assertThat(fromNow(clock, now.minusMillis(1)), is("1 ms ago"));
    assertThat(fromNow(clock, now.minusMillis(2)), is("2 ms ago"));
    assertThat(fromNow(clock, now.minusMillis(999)), is("999 ms ago"));
    assertThat(fromNow(clock, now.minusMillis(1000)), is("1 sec ago"));
    assertThat(fromNow(clock, now.minusMillis(1001)), is("1 sec ago"));
    assertThat(fromNow(clock, now.minusMillis(1999)), is("1 sec ago"));
    assertThat(fromNow(clock, now.minusMillis(2000)), is("2 secs ago"));
    assertThat(fromNow(clock, now.minusMillis(2001)), is("2 secs ago"));
    assertThat(fromNow(clock, now.minusMillis(2999)), is("2 secs ago"));

    assertThat(fromNow(clock, now.minusSeconds(59)), is("59 secs ago"));
    assertThat(fromNow(clock, now.minusSeconds(60)), is("1 min ago"));
    assertThat(fromNow(clock, now.minusSeconds(119)), is("2 mins ago"));
    assertThat(fromNow(clock, now.minusSeconds(120)), is("2 mins ago"));

    assertThat(fromNow(clock, now.minus(2, ChronoUnit.MINUTES)), is("2 mins ago"));
    assertThat(fromNow(clock, now.minus(140, ChronoUnit.SECONDS)), is("2½ mins ago"));
    assertThat(fromNow(clock, now.minus(150, ChronoUnit.SECONDS)), is("2½ mins ago"));
    assertThat(fromNow(clock, now.minus(166, ChronoUnit.SECONDS)), is("3 mins ago"));
    assertThat(fromNow(clock, now.minus(180, ChronoUnit.SECONDS)), is("3 mins ago"));
    assertThat(fromNow(clock, now.minus(195, ChronoUnit.SECONDS)), is("3 mins ago"));

    assertThat(fromNow(clock, now.minus(15, ChronoUnit.MINUTES)), is("15 mins ago"));
    assertThat(fromNow(clock, now.minus(30, ChronoUnit.MINUTES)), is("30 mins ago"));
    assertThat(fromNow(clock, now.minus(45, ChronoUnit.MINUTES)), is("45 mins ago"));

    assertThat(fromNow(clock, now.minus(60, ChronoUnit.MINUTES)), is("1 hour ago"));
    assertThat(fromNow(clock, now.minus(140, ChronoUnit.MINUTES)), is("2½ hours ago"));
    assertThat(fromNow(clock, now.minus(150, ChronoUnit.MINUTES)), is("2½ hours ago"));
    assertThat(fromNow(clock, now.minus(166, ChronoUnit.MINUTES)), is("3 hours ago"));
    assertThat(fromNow(clock, now.minus(180, ChronoUnit.MINUTES)), is("3 hours ago"));
    assertThat(fromNow(clock, now.minus(195, ChronoUnit.MINUTES)), is("3 hours ago"));

    assertThat(fromNow(clock, now.minus(24, ChronoUnit.HOURS)), is("1 day ago"));
    assertThat(fromNow(clock, now.minus(36, ChronoUnit.HOURS)), is("1½ days ago"));
    assertThat(fromNow(clock, now.minus(43, ChronoUnit.HOURS)), is("2 days ago"));
    assertThat(fromNow(clock, now.minus(48, ChronoUnit.HOURS)), is("2 days ago"));

    assertThat(fromNow(clock, now.minus(7, ChronoUnit.DAYS)), is("1 week ago"));
    assertThat(fromNow(clock, now.minus(11, ChronoUnit.DAYS)), is("1½ weeks ago"));
    assertThat(fromNow(clock, now.minus(14, ChronoUnit.DAYS)), is("2 weeks ago"));

    assertThat(fromNow(clock, now.plusMillis(1)), is("in 1 ms"));
    assertThat(fromNow(clock, now.plusMillis(2)), is("in 2 ms"));
    assertThat(fromNow(clock, now.plusMillis(999)), is("in 999 ms"));
    assertThat(fromNow(clock, now.plusMillis(1000)), is("in 1 sec"));
    assertThat(fromNow(clock, now.plusMillis(1001)), is("in 1 sec"));
    assertThat(fromNow(clock, now.plusMillis(1999)), is("in 1 sec"));
    assertThat(fromNow(clock, now.plusMillis(2000)), is("in 2 secs"));
    assertThat(fromNow(clock, now.plusMillis(2001)), is("in 2 secs"));
    assertThat(fromNow(clock, now.plusMillis(2999)), is("in 2 secs"));

    assertThat(fromNow(clock, now.plusSeconds(59)), is("in 59 secs"));
    assertThat(fromNow(clock, now.plusSeconds(60)), is("in 1 min"));
    assertThat(fromNow(clock, now.plusSeconds(119)), is("in 2 mins"));
    assertThat(fromNow(clock, now.plusSeconds(120)), is("in 2 mins"));

    assertThat(fromNow(clock, now.plus(2, ChronoUnit.MINUTES)), is("in 2 mins"));
    assertThat(fromNow(clock, now.plus(140, ChronoUnit.SECONDS)), is("in 2½ mins"));
    assertThat(fromNow(clock, now.plus(150, ChronoUnit.SECONDS)), is("in 2½ mins"));
    assertThat(fromNow(clock, now.plus(166, ChronoUnit.SECONDS)), is("in 3 mins"));
    assertThat(fromNow(clock, now.plus(180, ChronoUnit.SECONDS)), is("in 3 mins"));
    assertThat(fromNow(clock, now.plus(195, ChronoUnit.SECONDS)), is("in 3 mins"));

    assertThat(fromNow(clock, now.plus(15, ChronoUnit.MINUTES)), is("in 15 mins"));
    assertThat(fromNow(clock, now.plus(30, ChronoUnit.MINUTES)), is("in 30 mins"));
    assertThat(fromNow(clock, now.plus(45, ChronoUnit.MINUTES)), is("in 45 mins"));

    assertThat(fromNow(clock, now.plus(60, ChronoUnit.MINUTES)), is("in 1 hour"));
    assertThat(fromNow(clock, now.plus(140, ChronoUnit.MINUTES)), is("in 2½ hours"));
    assertThat(fromNow(clock, now.plus(150, ChronoUnit.MINUTES)), is("in 2½ hours"));
    assertThat(fromNow(clock, now.plus(166, ChronoUnit.MINUTES)), is("in 3 hours"));
    assertThat(fromNow(clock, now.plus(180, ChronoUnit.MINUTES)), is("in 3 hours"));
    assertThat(fromNow(clock, now.plus(195, ChronoUnit.MINUTES)), is("in 3 hours"));

    assertThat(fromNow(clock, now.plus(24, ChronoUnit.HOURS)), is("in 1 day"));
    assertThat(fromNow(clock, now.plus(36, ChronoUnit.HOURS)), is("in 1½ days"));
    assertThat(fromNow(clock, now.plus(43, ChronoUnit.HOURS)), is("in 2 days"));
    assertThat(fromNow(clock, now.plus(48, ChronoUnit.HOURS)), is("in 2 days"));

    assertThat(fromNow(clock, now.plus(7, ChronoUnit.DAYS)), is("in 1 week"));
    assertThat(fromNow(clock, now.plus(11, ChronoUnit.DAYS)), is("in 1½ weeks"));
    assertThat(fromNow(clock, now.plus(14, ChronoUnit.DAYS)), is("in 2 weeks"));
  }
}
