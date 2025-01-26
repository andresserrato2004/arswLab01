package edu.eci.arsw.math;

import edu.eci.arsw.threads.PiDigitsThread;

import java.util.ArrayList;
import java.util.List;

/**
 *  An implementation of the Bailey-Borwein-Plouffe formula for calculating hexadecimal
 *  digits of pi.
 *  https://en.wikipedia.org/wiki/Bailey%E2%80%93Borwein%E2%80%93Plouffe_formula
 *  *** Translated from C# code:
**/
public class PiDigits {

    private static int DigitsPerSum = 8;
    private static double Epsilon = 1e-17;


    /**
     * Returns a range of hexadecimal digits of pi.
     *
     * @param start The starting location of the range.
     * @param count The number of digits to return
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count) {
        if (start < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        if (count < 0) {
            throw new RuntimeException("Invalid Interval");
        }

        byte[] digits = new byte[count];
        double sum = 0;

        for (int i = 0; i < count; i++) {
            if (i % DigitsPerSum == 0) {
                sum = 4 * sum(1, start)
                        - 2 * sum(4, start)
                        - sum(5, start)
                        - sum(6, start);

                start += DigitsPerSum;
            }

            sum = 16 * (sum - Math.floor(sum));
            digits[i] = (byte) sum;
        }

        return digits;
    }

    /**
     * Returns a range of hexadecimal digits of pi using multiple threads.
     *
     * @param start The starting location of the range.
     * @param count The number of digits to return.
     * @param N The number of threads to use.
     * @return An array containing the hexadecimal digits.
     */
    public static byte[] getDigits(int start, int count, int N) {
        if (start < 0 || count < 0 || N <= 0) {
            throw new RuntimeException("Invalid Parameters");
        }

        // Determinate the number of digits to calculate per thread
        int digitsPerThread = count / N;
        int remainingDigits = count % N;

        List<PiDigitsThread> threads = new ArrayList<>();
        int currentStart = start;

        // Create the threads
        for (int i = 0; i < N; i++) {
            int currentCount = digitsPerThread + (i < remainingDigits ? 1 : 0); // Distribute the remaining digits
            PiDigitsThread thread = new PiDigitsThread(currentStart, currentCount);
            threads.add(thread);
            currentStart += currentCount;
        }

        // Initiate the threads
        for (PiDigitsThread thread : threads) {
            thread.start();
        }

        // Wait for the threads to finish
        for (PiDigitsThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException("Thread interrupted", e);
            }
        }

        // Merge the results
        byte[] digits = new byte[count];
        int index = 0;
        for (PiDigitsThread thread : threads) {
            System.arraycopy(thread.getResult(), 0, digits, index, thread.getResult().length);
            index += thread.getResult().length;
        }

        return digits;
    }

    /**
     * Calculates the sum of the series 16^(n - k) / (8 * k + m) for k starting from 0
     * and iterating until the terms become negligible (less than a predefined epsilon value).
     *
     * The series is evaluated as follows:
     * - For positive powers (power > 0): it calculates the modular exponentiation of 16^power mod d
     *   for improved efficiency and accuracy, dividing the result by d.
     * - For non-positive powers (power <= 0): it directly calculates 16^power / d using Math.pow.
     * - The iteration stops when the term becomes smaller than a predefined threshold `Epsilon`.
     *
     * @param initialValue the starting value for the denominator offset in the series formula.
     * @param initialPower the initial power of 16 in the series formula.
     * @return the sum of the series up to the point where terms become negligible.
     */
    private static double sum(int initialValue, int initialPower) {
        double sum = 0;
        int value = initialValue;
        int power = initialPower;

        while (true) {
            double term;

            if (power > 0) {
                term = (double) hexExponentModulo(power, value) / value;
            } else {
                term = Math.pow(16, power) / value;
                if (term < Epsilon) {
                    break;
                }
            }

            sum += term;
            power--;
            value += 8;
        }

        return sum;
    }

    /**
     * Calculates 16^exponent \mod modulus efficiently using the method of exponentiation by squaring.
     *
     * This method is designed to compute modular exponentiation, which is useful for
     * avoiding overflow and improving performance when dealing with large exponents.
     * The calculation is done as follows:
     *
     * - The largest power of 2 less than or equal to `exponent` is determined to break down
     *   the exponentiation into smaller steps.
     * - The result is built iteratively by squaring and reducing the intermediate results
     *   modulo `modulus` at each step.
     * - If `exponent` contains additional powers not covered by squaring, those are added
     *   to the result directly by multiplying and applying the modulo operation.
     *
     * @param exponent the exponent (non-negative integer) for the base 16.
     * @param modulus the modulus used for the modular arithmetic.
     * @return the result of \(16^exponent \mod modulus\).
     */
    private static int hexExponentModulo(int exponent, int modulus) {
        int power = 1;

        while (power * 2 <= exponent) {
            power *= 2;
        }

        int result = 1;

        while (power > 0) {
            if (exponent >= power) {
                result *= 16;
                result %= modulus;
                exponent -= power;
            }

            power /= 2;

            if (power > 0) {
                result *= result;
                result %= modulus;
            }
        }

        return result;
    }

    /**
     * @return the number of digits per sum
     */
    public static int getDigitsPerSum() {
        return DigitsPerSum;
    }

    /**
     * @param digitsPerSum the number of digits per sum
     */
    public static void setDigitsPerSum(int digitsPerSum) {
        DigitsPerSum = digitsPerSum;
    }

    /**
     * @return the epsilon value
     */
    public static double getEpsilon() {
        return Epsilon;
    }

    /**
     * @param epsilon the epsilon value
     */
    public static void setEpsilon(double epsilon) {
        Epsilon = epsilon;
    }
}
