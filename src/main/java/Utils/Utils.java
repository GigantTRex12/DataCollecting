package Utils;

import org.apache.commons.math3.stat.interval.ConfidenceInterval;
import org.apache.commons.math3.stat.interval.WilsonScoreInterval;

public class Utils {

    public static String toBinomialConfidenceRange(int successes, int sampleSize, double confidence, int digits) {
        ConfidenceInterval interval = new WilsonScoreInterval().createInterval(sampleSize, successes, confidence);
        long tens = (long) Math.pow(10, digits);
        long lower = Math.round(100 * tens * interval.getLowerBound());
        long higher = Math.round(100 * tens * interval.getUpperBound());
        return "[" + numbertoStringWithComma(lower, digits) + "% - " + numbertoStringWithComma(higher, digits) + "%]";
    }

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

}
