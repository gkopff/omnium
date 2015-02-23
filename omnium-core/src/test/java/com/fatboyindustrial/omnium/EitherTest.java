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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Either}.
 */
public class EitherTest
{
  /**
   * Tests that the expected non-null precondition check works.
   */
  @Test(expected =  NullPointerException.class)
  public void testPreconditionsNullLeft()
  {
    Either.left(null);
  }

  /**
   * Tests that the expected non-null precondition check works.
   */
  @Test(expected =  NullPointerException.class)
  public void testPreconditionsNullRight()
  {
    Either.right(null);
  }

  /**
   * Tests that a valid left value object can be constructed.
   */
  @Test
  public void testValidConstructionLeft()
  {
    final Either<String, Integer> sut = Either.left("value");

    assertThat(sut.isLeft(), is(true));
    assertThat(sut.isRight(), is(false));
    assertThat(sut.left(), is("value"));
  }

  /**
   * Tests that a valid right value object can be constructed.
   */
  @Test
  public void testValidConstructionRight()
  {
    final Either<String, Integer> sut = Either.right(42);

    assertThat(sut.isLeft(), is(false));
    assertThat(sut.isRight(), is(true));
    assertThat(sut.right(), is(42));
  }

  /**
   * Tests that the expected exception is raised if you attempt to get the value when one is not set.
   */
  @Test(expected = NoSuchElementException.class)
  public void testExceptionGetLeftOnRight()
  {
    Either.right("foo").left();
  }

  /**
   * Tests that the expected exception is raised if you attempt to get the value when one is not set.
   */
  @Test(expected = NoSuchElementException.class)
  public void testExceptionGetRightOnLeft()
  {
    Either.left("foo").right();
  }

  /**
   * Tests that the left value mapping forms a valid Either and that the original Either is unchanged
   * (in the case that the Either contains a left value).
   */
  @Test
  public void testMapLeftOnLeft()
  {
    final Either<String, Integer> original = Either.left("Space Cookies!");
    final Either<Character, Integer> transformed = original.mapLeft(str -> str.charAt(0));

    assertThat(original.isLeft(), is(true));
    assertThat(original.left(), is("Space Cookies!"));

    assertThat(transformed.isLeft(), is(true));
    assertThat(transformed.left(), is('S'));
  }

  /**
   * Tests that the left value mapping forms a valid Either and that the original Either is unchanged
   * (in the case that the Either contains a right value).
   */
  @Test
  public void testMapLeftOnRight()
  {
    final Either<String, Integer> original = Either.right(500);
    final Either<Character, Integer> transformed = original.mapLeft(str -> str.charAt(0));

    assertThat(original.isRight(), is(true));
    assertThat(original.right(), is(500));

    assertThat(transformed.isRight(), is(true));
    assertThat(transformed.right(), is(500));
  }

  /**
   * Tests that the right value mapping forms a valid Either and that the original Either is unchanged
   * (in the case that the Either contains a right value).
   */
  @Test
  public void testMapRightOnRight()
  {
    final Either<String, Integer> original = Either.right(500);
    final Either<String, String> transformed = original.mapRight(String::valueOf);

    assertThat(original.isRight(), is(true));
    assertThat(original.right(), is(500));

    assertThat(transformed.isRight(), is(true));
    assertThat(transformed.right(), is("500"));
  }

  /**
   * Tests that the right value mapping forms a valid Either and that the original Either is unchanged
   * (in the case that the Either contains a left value).
   */
  @Test
  public void testMapRightOnLeft()
  {
    final Either<String, Integer> original = Either.left("Space Cookies!");
    final Either<String, String> transformed = original.mapRight(String::valueOf);

    assertThat(original.isLeft(), is(true));
    assertThat(original.left(), is("Space Cookies!"));

    assertThat(transformed.isLeft(), is(true));
    assertThat(transformed.left(), is("Space Cookies!"));
  }

  /**
   * Tests that copying a left value of a left value object propagates the left value correctly.
   */
  @Test
  public void testCopyLeftOfLeft()
  {
    final Either<String, Integer> original = Either.left("Space Cookies!");
    final Either<String, String> copy = original.copyLeft();

    assertThat(copy.isLeft(), is(true));
    assertThat(copy.left(), is("Space Cookies!"));
  }

  /**
   * Tests that copying a left value of a right value object raises the expected exception.
   */
  @Test(expected = NoSuchElementException.class)
  public void testCopyLeftOfRight()
  {
    Either.left("Space Cookies!").copyRight();
  }

  /**
   * Tests that copying a right value of a right value object propagates the left value correctly.
   */
  @Test
  public void testCopyRightOfRight()
  {
    final Either<String, Integer> original = Either.right(42);
    final Either<Integer, Integer> copy = original.copyRight();

    assertThat(copy.isRight(), is(true));
    assertThat(copy.right(), is(42));
  }

  /**
   * Tests that copying a right value of a left value object raises the expected exception.
   */
  @Test(expected = NoSuchElementException.class)
  public void testCopyRightOfLeft()
  {
    Either.right("Space Cookies!").copyLeft();
  }

  /**
   * Tests that the block is supplied the correct value for a left value object invoking ifLeft().
   */
  @Test
  public void testIfLeftOnLeft()
  {
    final Either<String, Integer> original = Either.left("Space Cookies!");
    final AtomicBoolean invoked = new AtomicBoolean(false);

    original.ifLeft(str -> {
      assertThat(str, is("Space Cookies!"));
      invoked.set(true);
    });

    assertThat(invoked.get(), is(true));
  }

  /**
   * Tests that the block is not invoked when a left value object has ifRight() invoked on it.
   */
  @Test
  public void testIfRightOnLet()
  {
    final Either<String, Integer> original = Either.left("Space Cookies!");
    final AtomicBoolean invoked = new AtomicBoolean(false);

    original.ifRight(integer -> invoked.set(true));

    assertThat(invoked.get(), is(false));
  }

  /**
   * Tests that the block is supplied the correct value for a right value object invoking ifRight().
   */
  @Test
  public void testIfRightOnRight()
  {
    final Either<String, Integer> original = Either.right(42);
    final AtomicBoolean invoked = new AtomicBoolean(false);

    original.ifRight(integer -> {
      assertThat(integer, is(42));
      invoked.set(true);
    });

    assertThat(invoked.get(), is(true));
  }

  /**
   * Tests that the block is not invoked when a right value object has ifLeft() invoked on it.
   */
  @Test
  public void testIfLeftOnRight()
  {
    final Either<String, Integer> original = Either.right(42);
    final AtomicBoolean invoked = new AtomicBoolean(false);

    original.ifLeft(integer -> invoked.set(true));

    assertThat(invoked.get(), is(false));
  }

  /**
   * Tests that equals and hash code behave correctly.
   */
  @SuppressWarnings({ "ObjectEqualsNull", "EqualsBetweenInconvertibleTypes" })
  @Test
  public void testEqualsAndHashCode()
  {
    final Either<String, Integer> l1 = Either.left("Space Cookies!");
    final Either<String, Integer> sameAsL1 = Either.left("Space Cookies!");
    final Either<String, Integer> l2 = Either.left("Infinite improbability");
    final Either<String, Integer> r1 = Either.right(42);
    final Either<Character, Integer> r2 = Either.right(42).copyRight();

    assertThat(l1, is(l1));

    assertThat(l1, is(sameAsL1));
    assertThat(sameAsL1, is(l1));
    assertThat(l1.hashCode(), is(sameAsL1.hashCode()));

    assertThat(l1, is(not(l2)));
    assertThat(l2, is(not(l1)));

    assertThat(l1, is(not(r1)));
    assertThat(r1, is(not(l1)));

    assertThat(r1, is(r2));
    assertThat(r2, is(r1));
    assertThat(r1.hashCode(), is(r2.hashCode()));

    assertThat(r1.equals(null), is(false));
    assertThat(r1.equals(""), is(false));
  }

  /**
   * Tests that serialisation and deserialisation works.
   */
  @SuppressWarnings("unchecked")
  @Test
  public void testSerialisation() throws Exception
  {
    final Either<String, Integer> originalLeft = Either.left("Space Cookies!");
    final Either<String, Integer> originalRight = Either.right(42);

    final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    final ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);

    oos.writeObject(originalLeft);
    oos.writeObject(originalRight);
    oos.close();

    final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    final ObjectInputStream ois = new ObjectInputStream(byteArrayInputStream);

    final Either<String, Integer> reconstitutedLeft = (Either<String, Integer>) ois.readObject();
    final Either<String, Integer> reconstitutedRight = (Either<String, Integer>) ois.readObject();

    ois.close();

    assertThat(originalLeft, is(reconstitutedLeft));
    assertThat(originalRight, is(reconstitutedRight));
  }
}
