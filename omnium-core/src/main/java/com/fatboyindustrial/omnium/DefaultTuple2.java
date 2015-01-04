/*
 * Copyright 2015 Greg Kopff
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

import java.io.Serializable;

/**
 * The default concrete implementation of a {@link Tuple2}, an immutable tuple of 2 elements. <p>
 *
 * The tuple is serialisable if the elements are.
 *
 * @param <A> The type of the first element.
 * @param <B> The type of the second element.
 */
class DefaultTuple2<A, B> implements Tuple2<A, B>, Serializable
{
  private static final long serialVersionUID = 1L;

  /** The first element. */
  private final A first;

  /** The second element. */
  private final B second;

  /**
   * Constructor.
   * @param first The first element.
   * @param second The second element.
   */
  private DefaultTuple2(final A first, final B second)
  {
    this.first = first;
    this.second = second;
  }

  /**
   * Creates a new tuple containing the given elements.
   * @param first The first element.
   * @param second The second element.
   * @param <A> The type of the first element.
   * @param <B> The type of the second element.
   * @return A new tuple.
   */
  public static <A, B> Tuple2<A, B> of(final A first, final B second)
  {
    Preconditions.checkNotNull(first, "first cannot be null");
    Preconditions.checkNotNull(second, "second cannot be null");

    return new DefaultTuple2<>(first, second);
  }

  /**
   * Gets the first element.
   * @return The first element.
   */
  @Override
  public A first()
  {
    return this.first;
  }

  /**
   * Gets the second element.
   * @return The second element.
   */
  @Override
  public B second()
  {
    return this.second;
  }

  /**
   * Gets the details of the tuple as a String.
   * @return A description of this tuple.
   */
  @Override
  public String toString()
  {
    return "(" +
           first() +
           ", " + second() +
           ')';
  }

  /**
   * Compares this tuple with another for equality.  Tuples are considered equal if their respective
   * elements are equal.
   * @param obj The other tuple.
   * @return True if they're equal false otherwise.
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null || getClass() != obj.getClass())
    {
      return false;
    }

    final DefaultTuple2<?, ?> that = (DefaultTuple2<?, ?>) obj;
    return this.first.equals(that.first) &&
           this.second.equals(that.second);
  }

  /**
   * Computes a hash code.
   * @return A hash code.
   */
  @Override
  public int hashCode()
  {
    int result = this.first.hashCode();
    result = 31 * result + this.second.hashCode();
    return result;
  }
}

