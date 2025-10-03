package Utils;

import static java.lang.IO.println;
import static java.lang.IO.readln;

public class InputUtils {

    public static final String LINEBREAK = System.lineSeparator();

    public static String input() {
        return readln();
    }

    public static String input(String message) {
        println(message);
        return readln();
    }

    public static String multilineInput() {
        String line = readln();
        if (line.isEmpty()) return "";
        StringBuilder sb = new StringBuilder(line);
        while (true) {
            line = readln();
            if (line.isEmpty()) {
                break;
            }
            sb.append(LINEBREAK).append(line);
        }
        return sb.toString();
    }

    public static String multilineInput(String message) {
        println(message);
        return multilineInput();
    }

}
