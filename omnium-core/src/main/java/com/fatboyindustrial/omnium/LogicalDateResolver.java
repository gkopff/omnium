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

import javax.annotation.concurrent.Immutable;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Utility for resolving what the 'logical date' for a given instant and timezone is. <p>
 *
 * A logical date is one where the day boundaries are not considered to be midnight-to-midnight,
 * but rather some other "business" derived value (such as 4:00 am).
 */
@Immutable
public class LogicalDateResolver
{
  /** The local start of day. */
  private final LocalTime startOfDay;

  /** The local timezone. */
  private final ZoneId timezone;

  /**
   * Constructor.
   * @param startOfDay The local start of day.
   * @param timezone The local timezone.
   */
  public LogicalDateResolver(final LocalTime startOfDay, final ZoneId timezone)
  {
    this.startOfDay = Preconditions.checkNotNull(startOfDay, "startOfDay cannot be null");
    this.timezone = Preconditions.checkNotNull(timezone, "timezone cannot be null");
  }

  /**
   * Gets the logical local date (not the actual local date) for the given instant.
   * @param instant The instant to get the date for.
   * @return The logical local date.
   */
  public LocalDate localDate(final Instant instant)
  {
    Preconditions.checkNotNull(instant, "instant cannot be null");

    final ZonedDateTime zonedInstant = instant.atZone(this.timezone);
    final LocalDate localDate = zonedInstant.toLocalDate();
    final LocalTime localTime = zonedInstant.toLocalTime();

    return localTime.compareTo(this.startOfDay) >= 0 ? localDate : localDate.minusDays(1);
  }
}
