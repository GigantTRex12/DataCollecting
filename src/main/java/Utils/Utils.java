package Utils;

import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.commons.math3.stat.interval.WilsonScoreInterval;

public class Utils {

    /**
     * Calculates the Willson Score Interval
     * @see <a href="https://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval">https://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval</a>
     * @param digits Number of digits to round to (after converting to percentage)
     *               Defaults to rounding to whole number if digits is non-positive
     * @return String representing the Wilson Score Interval with Format ['lowerBoundaryInPercent'% - 'upperBoundaryInPercent'%]
     */
    public static String toBinomialConfidenceRange(int successes, int sampleSize, double confidence, int digits) {
        ConfidenceInterval interval = new WilsonScoreInterval().createInterval(sampleSize, successes, confidence);
        long tens = (long) Math.pow(10, Math.max(0, digits));
        long lower = Math.round(100 * tens * interval.getLowerBound());
        long higher = Math.round(100 * tens * interval.getUpperBound());
        return "[" + numbertoStringWithComma(lower, digits) + "% - " + numbertoStringWithComma(higher, digits) + "%]";
    }

    /**
     * Formats a number to an Integer or Floating-Point Number String representation
     * @param digits Amount of digits to move the decimal point by
     *               Defaults to 0 if negative
     * @return The formatted number as String
     */
    public static String numbertoStringWithComma(long number, int digits) {
        if (digits <= 0) return String.valueOf(number);
        String rep = String.valueOf(number);
        int pos = rep.length() - digits;

        if (pos < 0) return "0." + new String(new char[-pos]).replace('\0', '0') + rep;
        if (pos == 0) {
            while (rep.endsWith("0")) rep = rep.substring(0, rep.length() - 1);
            return "0." + rep;
        }

        String before = rep.substring(0, pos);
        String after = rep.substring(pos);
        while (after.endsWith("0")) after = after.substring(0, after.length() - 1);

        if (after.isEmpty()) return before;
        return before + "." + after;
    }

    private Utils() {
    }

}
