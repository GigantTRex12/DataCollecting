package Utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class StringUtils {

    public static String join(String[] strings, String sep) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                result.append(sep);
            }
            result.append(strings[i]);
        }
        return result.toString();
    }

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

    public static String join(List<String> strings) {
        return join(strings, "");
    }

    public static String join(List<String> strings, String sep) {
        return join(strings.toArray(new String[0]), sep);
    }

    public static boolean contains(String[] strings, String option) {
        for (String string : strings) {
            if (string.equalsIgnoreCase(option)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(Collection<String> strings, String option) {
        for (String string : strings) {
            if (string.equalsIgnoreCase(option)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(Map<String, String> strings, String option) {
        return contains(strings.keySet(), option) || contains(strings.values(), option);
    }

}
