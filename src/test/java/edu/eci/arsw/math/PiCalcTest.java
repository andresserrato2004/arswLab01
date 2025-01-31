/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.math;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class PiCalcTest {

    byte[] expected = new byte[]{
            0x2, 0x4, 0x3, 0xF, 0x6, 0xA, 0x8, 0x8,
            0x8, 0x5, 0xA, 0x3, 0x0, 0x8, 0xD, 0x3,
            0x1, 0x3, 0x1, 0x9, 0x8, 0xA, 0x2, 0xE,
            0x0, 0x3, 0x7, 0x0, 0x7, 0x3, 0x4, 0x4,
            0xA, 0x4, 0x0, 0x9, 0x3, 0x8, 0x2, 0x2,
            0x2, 0x9, 0x9, 0xF, 0x3, 0x1, 0xD, 0x0,
            0x0, 0x8, 0x2, 0xE, 0xF, 0xA, 0x9, 0x8,
            0xE, 0xC, 0x4, 0xE, 0x6, 0xC, 0x8, 0x9,
            0x4, 0x5, 0x2, 0x8, 0x2, 0x1, 0xE, 0x6,
            0x3, 0x8, 0xD, 0x0, 0x1, 0x3, 0x7, 0x7,
    };
    public PiCalcTest() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void piGenTest() throws Exception {

        for (int start = 0; start < expected.length; start++) {
            for (int count = 0; count < expected.length - start; count++) {
                byte[] digits = PiDigits.getDigits(start, count);
                assertEquals(count, digits.length);

                for (int i = 0; i < digits.length; i++) {
                    assertEquals(expected[start + i], digits[i]);
                }
            }
        }
    }

    @Test
    public void piGenWithOneThreadTest() throws Exception {

        for (int start = 0; start < expected.length; start++) {
            for (int count = 0; count < expected.length - start; count++) {
                byte[] digits = PiDigits.getDigits(start, count, 1);
                assertEquals(count, digits.length);

                for (int i = 0; i < digits.length; i++) {
                    assertEquals(expected[start + i], digits[i]);
                }
            }
        }
    }

    @Test
    public void piGenWithTwoThreadsTest() throws Exception {

        for (int start = 0; start < expected.length; start++) {
            for (int count = 0; count < expected.length - start; count++) {
                byte[] digits = PiDigits.getDigits(start, count, 2);
                assertEquals(count, digits.length);

                for (int i = 0; i < digits.length; i++) {
                    assertEquals(expected[start + i], digits[i]);
                }
            }
        }


    }

    @Test
    public void piGenWithThreeThreadsTest() throws Exception {

        for (int start = 0; start < expected.length; start++) {
            for (int count = 0; count < expected.length - start; count++) {
                byte[] digits = PiDigits.getDigits(start, count, 3);
                assertEquals(count, digits.length);

                for (int i = 0; i < digits.length; i++) {
                    assertEquals(expected[start + i], digits[i]);
                }
            }
        }
    }

    @Test
    public void shoulBeEquals() {
        byte[] digits = PiDigits.getDigits(0, 100, 1);
        byte[] digits2 = PiDigits.getDigits(0, 100, 2);
        byte[] digits3 = PiDigits.getDigits(0, 100, 3);
        for (int i = 0; i < digits.length; i++) {
            assertEquals(digits[i], digits2[i]);
            assertEquals(digits[i], digits3[i]);
        }
    }

    @Test
    public void shoulBeFailBecauseOfInvalidInterval() {
        try {
            byte[] digits = PiDigits.getDigits(-1, 100, 1);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }

    @Test
    public void shouldBeFailBecauseHasEmptyThreads() {
        try {
            byte[] digits = PiDigits.getDigits(0, 100, 0);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }

    @Test
    public void shouldBeFailBecauseHasEmptyThreadsAndInterval() {
        try {
            byte[] digits = PiDigits.getDigits(0, 0, 0);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }

    @Test
    public void shouldBeFailBecauseHasEmptyThreadsAndIntervalAndStart() {
        try {
            byte[] digits = PiDigits.getDigits(-1, 0, 0);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }

    @Test
    public void shouldBeFailBecauseHasEmptyThreadsAndIntervalAndStartAndCount() {
        try {
            byte[] digits = PiDigits.getDigits(-1, -1, 0);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }

    @Test
    public void shouldBeFailBecauseHasEmptyThreadsAndIntervalAndCount() {
        try {
            byte[] digits = PiDigits.getDigits(0, -1, 0);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }

    @Test
    public void shouldBeFailBecauseHasEmptyThreadsAndStart() {
        try {
            byte[] digits = PiDigits.getDigits(-1, 100, 0);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }

    @Test
    public void shouldBeHasException() {
        try {
            byte[] digits = PiDigits.getDigits(0, 100, -1);
            fail();
        } catch (Exception e) {
            assertEquals("Invalid Parameters", e.getMessage());
        }
    }
}
