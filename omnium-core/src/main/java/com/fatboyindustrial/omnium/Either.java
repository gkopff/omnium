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

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Optional;

/**
 * Represents either a valid value, or an error condition.
 *
 * @param <VALUE> The valid object.
 * @param <ERROR> The error object.
 */
@ThreadSafe
public class Either<VALUE, ERROR>
{
  /** The valid object. */
  private final Optional<VALUE> value;

  /** The error object. */
  private final Optional<ERROR> error;

  /**
   * Constructor.
   * @param value The valid object, or absent if an error is present.
   * @param error The error object, or absent if a valid object is present.
   * @throws IllegalArgumentException If both are present or both are absent.
   */
  @VisibleForTesting
  protected Either(Optional<VALUE> value, Optional<ERROR> error) throws IllegalArgumentException
  {
    this.value = Preconditions.checkNotNull(value, "value cannot be null");
    this.error = Preconditions.checkNotNull(error, "error cannot be null");

    Preconditions.checkArgument(! (this.value.isPresent() && this.error.isPresent()),
                                "Both value and error cannot be present");

    Preconditions.checkArgument(! (! this.value.isPresent() && ! this.error.isPresent()),
                                "Both value and error cannot be absent");
  }

  /**
   * Creates an object that contains a valid value.
   * @param value The valid value.
   * @param <VALUE> The type of the value.
   * @param <ERROR> The type of the error.
   * @return The object.
   */
  public static <VALUE, ERROR> Either<VALUE, ERROR> value(VALUE value)
  {
    return new Either<>(Optional.of(value), Optional.<ERROR>empty());
  }

  /**
   * Creates an object that contains an error.
   * @param error The error.
   * @param <VALUE> The type of the value.
   * @param <ERROR> The type of the error.
   * @return The object.
   */
  public static <VALUE, ERROR> Either<VALUE, ERROR> error(ERROR error)
  {
    return new Either<>(Optional.<VALUE>empty(), Optional.of(error));
  }

  /**
   * Is an error set?
   * @return True if there is an error, or false if there is a valid value.
   */
  public boolean isError()
  {
    return this.error.isPresent();
  }

  /**
   * Gets the valid value.
   * @return The value.
   * @throws IllegalStateException If a valid value is not present.
   */
  public VALUE getValue() throws IllegalStateException
  {
    if (! this.value.isPresent())
    {
      throw new IllegalStateException("value is not present");
    }

    return this.value.get();
  }

  /**
   * Gets the error value.
   * @return The error.
   * @throws IllegalStateException If an error is not present.
   */
  public ERROR getError() throws IllegalStateException
  {
    if (! this.error.isPresent())
    {
      throw new IllegalStateException("error is not present");
    }

    return this.error.get();
  }
}
