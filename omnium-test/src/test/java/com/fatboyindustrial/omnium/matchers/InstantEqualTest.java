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

package com.fatboyindustrial.omnium.matchers;

import org.hamcrest.Matcher;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.ReadableInstant;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link InstantEqual}.
 */
public class InstantEqualTest
{
  /**
   * Tests that the matcher correctly matches identical object references.
   */
  @Test
  public void testMatchExactObjects()
  {
    final DateTime dt = DateTime.now();
    final Matcher<ReadableInstant> matcher = InstantEqual.equalInstant(dt);

    assertThat(matcher.matches(dt), is(true));
  }

  /**
   * Tests that the matcher correctly matches equivalent objects (same timezone).
   */
  @Test
  public void testMatchEquivalentObjects()
  {
    final DateTime dt1 = DateTime.now();
    final DateTime dt2 = new DateTime(dt1);
    final Matcher<ReadableInstant> matcher = InstantEqual.equalInstant(dt1);

    assertThat(matcher.matches(dt2), is(true));
  }

  /**
   * Tests that the matcher correctly matches the same instant even if the timezone is different.
   */
  @Test
  public void testMatchSameInstantDifferentTimeZone()
  {
    final DateTime dt1 = DateTime.now().withZone(DateTimeZone.forID("Australia/Brisbane"));
    final DateTime dt2 = new DateTime(dt1).withZone(DateTimeZone.forID("UTC"));
    final Matcher<ReadableInstant> matcher = InstantEqual.equalInstant(dt1);

    assertThat(matcher.matches(dt2), is(true));
  }

  /**
   * Tests that the matcher doesn't match when instants are different.
   */
  @Test
  public void testNotMatchDifferentInstant()
  {
    final DateTime dt1 = DateTime.now();
    final DateTime dt2 = new DateTime(dt1).plusMillis(1);
    final Matcher<ReadableInstant> matcher = InstantEqual.equalInstant(dt1);

    assertThat(matcher.matches(dt2), is(false));
  }

  /**
   * Tests that the matcher doesn't match when two different classes are equated.
   */
  @Test
  public void testDifferentClasses()
  {
    final DateTime dt1 = DateTime.now();
    final String s = "not a DateTime";
    final Matcher<ReadableInstant> matcher = InstantEqual.equalInstant(dt1);

    assertThat(matcher.matches(s), is(false));
  }
}
