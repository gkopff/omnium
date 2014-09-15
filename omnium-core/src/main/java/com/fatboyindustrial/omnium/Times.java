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
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Utility methods for dealing with dates and times.
 */
@Immutable
public class Times
{
  /**
   * Describes the given time as the delta from the current instant. <p>
   *
   * The description is approximate, with varying error based on how far away the time is to now.
   * For example, if the time is more than an hour away (but less than a day away) it is reported
   * in half hour increments.
   * @param time The time to describe.
   * @return The description.
   */
  public static String fromNow(final Instant time)
  {
    Preconditions.checkNotNull(time, "time cannot be null");
    return fromNow(Clock.systemDefaultZone(), time);
  }

  /**
   * Describes the given time as the delta from the current instant. <p>
   *
   * The description is approximate, with varying error based on how far away the time is to now.
   * For example, if the time is more than an hour away (but less than a day away) it is reported
   * in half hour increments.
   * @param clock The clock used to get the current time.
   * @param time The time to describe.
   * @return The description.
   */
  @SuppressWarnings("MagicNumber")
  public static String fromNow(final Clock clock, final Instant time)
  {
    Preconditions.checkNotNull(clock, "clock cannot be null");
    Preconditions.checkNotNull(time, "time cannot be null");

    final Instant now = Instant.now(clock).truncatedTo(ChronoUnit.MILLIS);

    if (time.equals(now))
    {
      return "now";
    }

    final boolean past = time.isBefore(now);
    final Duration duration = past ? Duration.between(time, now)
                                   : Duration.between(now, time);
    final String narrative;

    if (weeks(duration) > 0)
    {
      final int weeks = weeks(duration);
      final int days = days(duration.minus(weeks * 7, ChronoUnit.DAYS));
      narrative = format("week", weeks, days / 7f);
    }
    else if (days(duration) > 0)
    {
      final int days = days(duration);
      final int hours = hours(duration.minus(days, ChronoUnit.DAYS));
      narrative = format("day", days, hours / 24f);
    }
    else if (hours(duration) > 0)
    {
      final int hours = hours(duration);
      final int minutes = minutes(duration.minus(hours, ChronoUnit.HOURS));
      narrative = format("hour", hours, minutes / 60f);
    }
    else if (minutes(duration) > 0)
    {
      final int minutes = minutes(duration);
      final int seconds = seconds(duration.minus(minutes, ChronoUnit.MINUTES));
      narrative = format("minute", minutes, seconds / 60f);
    }
    else if (seconds(duration) > 0)
    {
      narrative = format("second", seconds(duration), 0f);           // we don't care about half a second
    }
    else
    {
      narrative = duration.toMillis() + " ms";
    }

    return past ? (narrative + " ago")
                : ("in " + narrative);
  }

  /**
   * Computes the number of whole seconds in the duration.
   * @param duration The duration.
   * @return The number of seconds.
   */
  private static int seconds(final Duration duration)
  {
    return (int) duration.get(ChronoUnit.SECONDS);
  }

  /**
   * Computes the number of whole minutes in the duration.
   * @param duration The duration.
   * @return The number of minutes.
   */
  private static int minutes(final Duration duration)
  {
    return (int) duration.toMinutes();
  }

  /**
   * Computes the number of whole hours in the duration.
   * @param duration The duration.
   * @return The number of hours.
   */
  private static int hours(final Duration duration)
  {
    return (int) duration.toHours();
  }

  /**
   * Computes the number of whole days in the duration.
   * @param duration The duration.
   * @return The number of days.
   */
  private static int days(final Duration duration)
  {
    return (int) duration.toDays();
  }

  /**
   * Computes the number of whole weeks in the duration.
   * @param duration The duration.
   * @return The number of weeks.
   */
  private static int weeks(final Duration duration)
  {
    return days(duration) / 7;
  }

  /**
   * Formats the value by applying the correctly pluralised noun and adding a fractional part (to
   * the granularity of one quarter of the unit).
   * @param noun The time unit.
   * @param units The number of whole units.
   * @param fractionalPart The fractional units.
   * @return A descriptive string.
   */
  @SuppressWarnings("MagicNumber")
  private static String format(final String noun, final int units, final float fractionalPart)
  {
    final int adjustedUnits;
    final boolean halfAddition;

    if (fractionalPart > 0.75f)                  // closer to the next whole unit than the half
    {
      adjustedUnits = units + 1;
      halfAddition = false;
    }
    else if (fractionalPart > 0.25f)             // closer to plus half than the whole units
    {
      adjustedUnits = units;
      halfAddition = true;
    }
    else                                         // closest to the original whole units
    {
      adjustedUnits = units;
      halfAddition = false;
    }

    return String.format("%d%s %s",
                         adjustedUnits,
                         halfAddition ? "½" : "",
                         pluralise(noun, adjustedUnits + (halfAddition ? 1 : 0)));
  }

  /**
   * Turns the noun into a pluralise (simply by adding 's') if the count is greater than 1.
   * @param noun The time unit.
   * @param count The number of units.
   */
  private static String pluralise(final String noun, final int count)
  {
    return noun + (count > 1 ? "s" : "");
  }
}
