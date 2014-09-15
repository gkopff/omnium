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

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link LogicalDateResolver}.
 */
public class LogicalDateResolverTest
{
  /**
   * Tests that obtaining the local date functions correctly.
   */
  @Test
  public void testLocalDate()
  {
    final LogicalDateResolver resolver = new LogicalDateResolver(LocalTime.of(4, 0, 0), ZoneId.of("Europe/Stockholm"));
    final Instant i1 = Instant.parse("2011-03-02T02:59:59.999Z");
    final Instant i2 = Instant.parse("2011-03-02T03:00:00.000Z");
    final Instant i3 = Instant.parse("2011-03-03T02:59:59.999Z");

    assertThat(resolver.localDate(i1), is(LocalDate.of(2011, 3, 1)));
    assertThat(resolver.localDate(i2), is(LocalDate.of(2011, 3, 2)));
    assertThat(resolver.localDate(i3), is(LocalDate.of(2011, 3, 2)));
  }
}