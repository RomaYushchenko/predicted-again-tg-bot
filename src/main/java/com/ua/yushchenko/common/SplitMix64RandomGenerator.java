package com.ua.yushchenko.common;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

/**
 * {@code ThreadSafeSplitMix64RandomGenerator} is a thread-safe pseudo-random number generator
 * based on the {@code SplitMix64} algorithm, designed to provide high-quality random values
 * with strong bit mixing and minimal collision probability.
 * <p>
 * The generator initializes its internal state using a combination of multiple entropy sources:
 * <ul>
 *    <li>Current system time in nanoseconds ({@code System.nanoTime()});</li>
 *    <li>Current thread ID;</li>
 *    <li>Available free memory in the JVM;</li>
 *    <li>Identity hash code of a newly created object;</li>
 *    <li>Hash code of a randomly generated {@code UUID}.</li>
 *  </ul>
 *  This ensures a unique initial state for each generator instance, enhancing unpredictability.
 * <p>
 *  Thread safety is achieved using an {@link AtomicLong} to manage the internal state,
 *  ensuring that each thread gets a unique, non-overlapping sequence of numbers without synchronization overhead.
 *
 * @author roman.yushchenko
 * @version 0.2
 */
@Component
public class SplitMix64RandomGenerator {

    private final AtomicLong state;

    public SplitMix64RandomGenerator() {
        final long nanoTime = System.nanoTime();
        final long threadId = Thread.currentThread().getId();
        final long freeMemory = Runtime.getRuntime().freeMemory();
        final int objectHash = System.identityHashCode(new Object());
        final int uuidHash = UUID.randomUUID().hashCode();

        final long initialState = nanoTime ^ threadId ^ freeMemory ^ objectHash ^ uuidHash;

        this.state = new AtomicLong(initialState);
    }

    /**
     * Generates the next pseudo-random integer in the range from {@code 0} (inclusive)
     * to {@code bound} (exclusive).
     *
     * @param bound the upper bound (exclusive) of the generated random number.
     * @return a pseudo-random number between {@code 0} (inclusive) and {@code bound - 1} (inclusive).
     */
    public int nextInt(final int bound) {
        final long result = nextSplitMix64();
        return (int) (Math.abs(result) % bound);
    }

    /**
     * Generates the next pseudo-random 64-bit value using the {@code SplitMix64} algorithm.
     * This algorithm ensures high-quality bit mixing for better randomness.
     *
     * @return a pseudo-random 64-bit value.
     */
    private long nextSplitMix64() {
        long z = state.getAndAdd(0x9E3779B97F4A7C15L);
        z = (z ^ (z >>> 30)) * 0xBF58476D1CE4E5B9L;
        z = (z ^ (z >>> 27)) * 0x94D049BB133111EBL;
        return z ^ (z >>> 31);
    }
}
