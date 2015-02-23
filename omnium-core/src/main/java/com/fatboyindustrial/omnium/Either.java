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

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Represents a value of one of two possible types (a disjoint union).  Convention dictates that
 * <i>left</i> is used for failure and <i>right</i> is used for success.
 * @param <L> The left value type.
 * @param <R> The right value type.
 */
public abstract class Either<L, R>
{
  /**
   * Does this either hold a left value?
   * @return True if this object stores a left value, false otherwise.
   */
  public abstract boolean isLeft();

  /**
   * Does this either hold a right value?
   * @return True if this object stores a right value, false otherwise.
   */
  public final boolean isRight()
  {
    return ! isLeft();
  }

  /**
   * Gets the left value, raising an exception if this is a right value.
   * @return The left value.
   * @throws NoSuchElementException If this is a right value.
   */
  public abstract L left() throws NoSuchElementException;

  /**
   * Gets the right value, raising an exception if this is a left value.
   * @return The right value.
   * @throws NoSuchElementException If this is a left value.
   */
  public abstract R right() throws NoSuchElementException;

  /**
   * Applies the given function to the left value if there is one, or performs no operation if
   * this is a right value.
   * @param function The function to apply.
   * @param <M> The new left value type.
   * @return The resulting {@code Either}.
   */
  public abstract <M> Either<M, R> mapLeft(final Function<L, M> function);

  /**
   * Applies the given function to the right value if there is one, or performs no operation if
   * this is a left value.
   * @param function The function to apply.
   * @param <M> The new right value type.
   * @return The resulting {@code Either}.
   */
  public abstract <M> Either<L, M> mapRight(final Function<R, M> function);

  /**
   * Copies this {@code Either}'s left value and returns a new either with it.  The type of the right value
   * is inferred by the compiler.
   * @param <T> The type of the right value.
   * @return The resulting {@code Either}.
   * @throws NoSuchElementException If this is not a left value.
   */
  public <T> Either<L, T> copyLeft() throws NoSuchElementException
  {
    return Either.left(left());
  }

  /**
   * Copies this {@code Either}'s left value and returns a new either with it.  The type of the right value
   * is inferred by the compiler.
   * @param <T> The type of the right value.
   * @return The resulting {@code Either}.
   * @throws NoSuchElementException If this is not a right value.
   */
  public <T> Either<T, R> copyRight() throws NoSuchElementException
  {
    return Either.right(right());
  }

  /**
   * Executes the given code block if this is a left value, or does nothing if this is a right value.
   * @param block The code block to execute.
   */
  public abstract void ifLeft(final Consumer<L> block);

  /**
   * Executes the given code block if this is a right value, or does nothing if this is a right value.
   * @param block The code block to execute.
   */
  public abstract void ifRight(final Consumer<R> block);

  /**
   * Gets a left value.
   * @param left The value to store.
   * @param <L> The type of the left value.
   * @param <R> The type of the right value.
   * @return The {@code Either}.
   */
  public static <L, R> Either<L, R> left(L left)
  {
    Preconditions.checkNotNull(left, "left cannot be null");
    return new Left<>(left);
  }

  /**
   * Gets a right value.
   * @param right The value to store.
   * @param <L> The type of the left value.
   * @param <R> The type of the right value.
   * @return The {@code Either}.
   */
  public static <L, R> Either<L, R> right(R right)
  {
    Preconditions.checkNotNull(right, "right cannot be null");
    return new Right<>(right);
  }

  /**
   * Private constructor.
   */
  private Either()
  {
    ;
  }

  /**
   * Left value implementation.
   * @param <L> The left value type.
   * @param <R> The right value type.
   */
  private static class Left<L, R> extends Either<L, R>
  {
    /** Left value storage. */
    private final L left;

    /**
     * Private constructor.
     * @param left The left value.
     */
    private Left(final L left)
    {
      this.left = left;
    }

    /**
     * Does this either hold a left value?
     * @return Always true.
     */
    @Override
    public boolean isLeft()
    {
      return true;
    }

    /**
     * Gets the left value.
     * @return The left value.
     */
    @Override
    public L left()
    {
      return this.left;
    }

    /**
     * Gets the right value, raising an exception if this is a left value.
     * @return The right value.
     * @throws NoSuchElementException Always.
     */
    @Override
    public R right() throws NoSuchElementException
    {
      throw new NoSuchElementException();
    }

    /**
     * Applies the given function to the left value if there is one, or performs no operation if
     * this is a right value.
     * @param function The function to apply.
     * @param <M> The new left value type.
     * @return The resulting {@code Either}.
     */
    @Override
    public <M> Either<M, R> mapLeft(final Function<L, M> function)
    {
      return new Left<>(function.apply(this.left));
    }

    /**
     * Applies the given function to the right value if there is one, or performs no operation if
     * this is a left value.
     * @param function The function to apply.
     * @param <M> The new right value type.
     * @return The resulting {@code Either}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <M> Either<L, M> mapRight(final Function<R, M> function)
    {
      return (Either<L, M>) this;
    }

    /**
     * Executes the given code block.
     * @param block The code block to execute.
     */
    @Override
    public void ifLeft(final Consumer<L> block)
    {
      block.accept(this.left);
    }

    /**
     * Does nothing - this is a left value.
     * @param block Ignored.
     */
    @Override
    public void ifRight(final Consumer<R> block)
    {
      ;
    }

    /**
     * Compares this value with another for equality.
     * @param obj The other object.
     * @return True if the other object is a left value and the underlying values are equal.
     */
    @Override
    public boolean equals(Object obj)
    {
      if (obj instanceof Left<?, ?>)
      {
        final Left<?, ?> other = (Left<?, ?>) obj;
        return this.left.equals(other.left);
      }
      return false;
    }

    /**
     * Generates a hash code.
     * @return The hash code.
     */
    @Override
    public int hashCode()
    {
      return 17 * this.left.hashCode();
    }
  }

  /**
   * Right value implementation.
   * @param <L> The left value type.
   * @param <R> The right value type.
   */
  private static class Right<L, R> extends Either<L, R>
  {
    /** Right value storage. */
    private final R right;

    /**
     * Private constructor.
     * @param right The right value.
     */
    private Right(final R right)
    {
      this.right = right;
    }

    /**
     * Does this either hold a left value?
     * @return Always false.
     */
    @Override
    public boolean isLeft()
    {
      return false;
    }

    /**
     * Gets the left value, raising an exception if this is a left value.
     * @return The left value.
     * @throws NoSuchElementException Always.
     */
    @Override
    public L left() throws NoSuchElementException
    {
      throw new NoSuchElementException();
    }

    /**
     * Gets the right value.
     * @return The right value.
     */
    @Override
    public R right()
    {
      return this.right;
    }

    /**
     * Applies the given function to the left value if there is one, or performs no operation if
     * this is a right value.
     * @param function The function to apply.
     * @param <M> The new left value type.
     * @return The resulting {@code Either}.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <M> Either<M, R> mapLeft(final Function<L, M> function)
    {
      return (Either<M, R>) this;
    }

    /**
     * Applies the given function to the right value if there is one, or performs no operation if
     * this is a left value.
     * @param function The function to apply.
     * @param <M> The new right value type.
     * @return The resulting {@code Either}.
     */
    @Override
    public <M> Either<L, M> mapRight(final Function<R, M> function)
    {
      return new Right<>(function.apply(this.right));
    }

    /**
     * Does nothing - this is a right value.
     * @param block Ignored.
     */
    @Override
    public void ifLeft(final Consumer<L> block)
    {
      ;
    }

    /**
     * Executes the given code block.
     * @param block The code block to execute.
     */
    @Override
    public void ifRight(final Consumer<R> block)
    {
      block.accept(this.right);
    }

    /**
     * Compares this value with another for equality.
     * @param obj The other object.
     * @return True if the other object is a right value and the underlying values are equal.
     */
    @Override
    public boolean equals(Object obj)
    {
      if (obj instanceof Right<?, ?>)
      {
        final Right<?, ?> other = (Right<?, ?>) obj;
        return this.right.equals(other.right);
      }
      return false;
    }

    /**
     * Generates a hash code.
     * @return The hash code.
     */
    @Override
    public int hashCode()
    {
      return 31 * this.right.hashCode();
    }
  }
}
