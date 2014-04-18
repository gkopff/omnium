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

import com.google.common.base.Preconditions;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import javax.annotation.concurrent.Immutable;
import java.time.OffsetDateTime;

/**
 * A Hamcrest matcher that matches {@code java.time} {@link OffsetDateTime}s based on their millisecond
 * instant.  This allows two {@code OffsetDateTime} instances to be compared for equality based on the
 * instant, rather than also matching the timezone.
 */
@SuppressWarnings("WeakerAccess")
@Immutable
public class OffsetDateTimeEqual extends BaseMatcher<OffsetDateTime> implements Matcher<OffsetDateTime>
{
  /** The particular instant in time that is expected to match. */
  private final OffsetDateTime instant;

  /**
   * Constructor.
   * @param instant The expected instant in time.
   */
  public OffsetDateTimeEqual(OffsetDateTime instant)
  {
    this.instant = Preconditions.checkNotNull(instant, "instant cannot be null");
  }

  /**
   * Creates a matcher.
   * @param instant The expected instant in time.
   * @return The matcher.
   */
  @Factory
  public static Matcher<OffsetDateTime> equalOffsetDateTime(OffsetDateTime instant)
  {
    Preconditions.checkNotNull(instant, "instant cannot be null");
    return new OffsetDateTimeEqual(instant);
  }

  /**
   * Evaluates the matcher for argument <var>item</var>. <p/>
   *
   * This method matches against Object, instead of the generic type T. This is
   * because the caller of the Matcher does not know at runtime what the type is
   * (because of type erasure with Java generics). It is down to the implementations
   * to check the correct type.
   *
   * @param item the object against which the matcher is evaluated.
   * @return <code>true</code> if <var>item</var> matches, otherwise <code>false</code>.
   * @see BaseMatcher
   */
  @Override
  public boolean matches(final Object item)
  {
    Preconditions.checkNotNull(item, "item cannot be null");

    if (! (item instanceof OffsetDateTime))
    {
      return false;
    }

    final OffsetDateTime actual = (OffsetDateTime) item;
    return this.instant.isEqual(actual);
  }

  /**
   * Generates a description of the object.  The description may be part of a
   * a description of a larger object of which this is just a component, so it
   * should be worded appropriately.
   * @param description The description to be built or appended to.
   */
  @Override
  public void describeTo(final Description description)
  {
    Preconditions.checkNotNull(description, "description cannot be null");
    description.appendValue(this.instant);
  }
}
