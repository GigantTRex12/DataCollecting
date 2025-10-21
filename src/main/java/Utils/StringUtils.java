package Utils;

public class StringUtils {

    /**
     * Takes two String Arrays of same length and joins each pair of Strings with the same index.
     * @param sep Seperator between each pair of Strings
     * @return String like "a1(a2), b1(b2), c1(c2)"
     * @throws IllegalArgumentException If input arrays are not the same length
     */
    public static String join(String[] strings1, String[] strings2, String sep) {
        if (strings1.length != strings2.length) {
            throw new IllegalArgumentException("Can't join two arrays of different length");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < strings1.length; i++) {
            if (i != 0) {
                result.append(sep);
            }
            result.append(strings1[i]).append("(").append(strings2[i]).append(")");
        }
        return result.toString();
    }

    private StringUtils() {
    }

}
