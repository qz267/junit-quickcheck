/*
 The MIT License

 Copyright (c) 2010-2014 Paul R. Holser, Jr.

 Permission is hereby granted, free of charge, to any person obtaining
 a copy of this software and associated documentation files (the
 "Software"), to deal in the Software without restriction, including
 without limitation the rights to use, copy, modify, merge, publish,
 distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to
 the following conditions:

 The above copyright notice and this permission notice shall be
 included in all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package com.pholser.junit.quickcheck.random;

import java.math.BigInteger;
import java.util.Random;

import com.pholser.junit.quickcheck.internal.Ranges;

import static com.pholser.junit.quickcheck.internal.Ranges.*;

/**
 * A source of randomness, fed to {@link com.pholser.junit.quickcheck.generator.Generator}s so they can produce
 * random values for theory parameters.
 */
public class SourceOfRandomness {
    private final Random delegate;

    /**
     * Makes a new source of randomness.
     *
     * @param delegate a JDK source of randomness, to which the new instance will delegate
     */
    public SourceOfRandomness(Random delegate) {
        this.delegate = delegate;
    }

    /**
     * @return a uniformly distributed boolean value
     * @see java.util.Random#nextBoolean()
     */
    public boolean nextBoolean() {
        return delegate.nextBoolean();
    }

    /**
     * @param bytes a byte array to fill with random values
     * @see java.util.Random#nextBytes(byte[])
     */
    public void nextBytes(byte[] bytes) {
        delegate.nextBytes(bytes);
    }

    /**
     * Gives an array of a given length containing random bytes.
     *
     * @param count the desired length of the random byte array
     * @return random bytes
     * @see java.util.Random#nextBytes(byte[])
     */
    public byte[] nextBytes(int count) {
        byte[] buffer = new byte[count];
        delegate.nextBytes(buffer);
        return buffer;
    }

    /**
     * @return a uniformly distributed random {@code double} value in the interval {@code [0.0, 1.0)}
     * @see java.util.Random#nextDouble()
     */
    public double nextDouble() {
        return delegate.nextDouble();
    }

    /**
     * @return a uniformly distributed random {@code float} value in the interval {@code [0.0, 1.0)}
     * @see java.util.Random#nextFloat()
     */
    public float nextFloat() {
        return delegate.nextFloat();
    }

    /**
     * @return a Gaussian-distributed random double value
     * @see java.util.Random#nextGaussian()
     */
    public double nextGaussian() {
        return delegate.nextGaussian();
    }

    /**
     * @return a uniformly distributed random {@code int} value
     * @see java.util.Random#nextInt()
     */
    public int nextInt() {
        return delegate.nextInt();
    }

    /**
     * @param n upper bound
     * @return a uniformly distributed random {@code int} value in the interval {@code [0, n)}
     * @see java.util.Random#nextInt(int)
     */
    public int nextInt(int n) {
        return delegate.nextInt(n);
    }

    /**
     * @return a uniformly distributed random {@code long} value
     * @see java.util.Random#nextLong()
     */
    public long nextLong() {
        return delegate.nextLong();
    }

    /**
     * @param seed value with which to seed this source of randomness
     * @see java.util.Random#setSeed(long)
     */
    public void setSeed(long seed) {
        delegate.setSeed(seed);
    }

    /**
     * Gives a random {@code byte} value, uniformly distributed across the interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    public byte nextByte(byte min, byte max) {
        return (byte) nextLong(min, max);
    }

    /**
     * Gives a random {@code char} value, uniformly distributed across the interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    public char nextChar(char min, char max) {
        checkRange(Ranges.Type.CHARACTER, min, max);

        return (char) nextLong(min, max);
    }

    /**
     * <p>Gives a random {@code double} value in the interval {@code [min, max]}.</p>
     *
     * <p>This naive implementation takes a random {@code double} value from {@link Random#nextDouble()} and
     * scales/shifts the value into the desired interval. This may give surprising results for large ranges.</p>
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    public double nextDouble(double min, double max) {
        int comparison = checkRange(Ranges.Type.FLOAT, min, max);
        return comparison == 0 ? min : min + (max - min) * nextDouble();
    }

    /**
     * <p>Gives a random {@code float} value in the interval {@code [min, max]}.</p>
     *
     * <p>This naive implementation takes a random {@code float} value from {@link Random#nextFloat()} and
     * scales/shifts the value into the desired interval. This may give surprising results for large ranges.</p>
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    public float nextFloat(float min, float max) {
        int comparison = checkRange(Ranges.Type.FLOAT, min, max);
        return comparison == 0 ? min : min + (max - min) * nextFloat();
    }

    /**
     * Gives a random {@code int} value, uniformly distributed across the interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    public int nextInt(int min, int max) {
        return (int) nextLong(min, max);
    }

    /**
     * Gives a random {@code long} value, uniformly distributed across the interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    public long nextLong(long min, long max) {
        int comparison = checkRange(Ranges.Type.INTEGRAL, min, max);
        if (comparison == 0)
            return min;

        return choose(this, BigInteger.valueOf(min), BigInteger.valueOf(max)).longValue();
    }

    /**
     * Gives a random {@code short} value, uniformly distributed across the interval {@code [min, max]}.
     *
     * @param min lower bound of the desired interval
     * @param max upper bound of the desired interval
     * @return a random value
     */
    public short nextShort(short min, short max) {
        return (short) nextLong(min, max);
    }

    /**
     * Gives a random {@code BigInteger} representable by the given number of bits.
     *
     * @param numberOfBits the desired number of bits
     * @return a random {@code BigInteger}
     * @see BigInteger#BigInteger(int, java.util.Random)
     */
    public BigInteger nextBigInteger(int numberOfBits) {
        return new BigInteger(numberOfBits, delegate);
    }
}
