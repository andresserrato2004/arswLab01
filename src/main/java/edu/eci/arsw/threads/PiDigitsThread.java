package edu.eci.arsw.threads;
import edu.eci.arsw.math.PiDigits;
import java.util.Arrays;

/**
 * A class that extends Thread to calculate the digits of pi
 */
public class PiDigitsThread extends Thread {
    private final int start;
    private final int count;
    private byte[] result;

    /**
     * Constructor of the class
     * @param start the start of the range
     * @param count the count of the range
     */
    public PiDigitsThread(int start, int count) {
        this.start = start;
        this.count = count;
    }

    /**
     * Run the thread
     */
    @Override
    public void run() {
        result = PiDigits.getDigits(start, count);
    }

    /**
     * @return the result
     */
    public byte[] getResult() {
        return result;
    }

    /**
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     *
     * @return a string with the information of the thread
     */
    @Override
    public String toString() {
        return "PiDigitsThread{" +
                "start=" + start +
                ", count=" + count +
                ", result=" + Arrays.toString(result) +
                '}';
    }


}
