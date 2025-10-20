package Utils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.lang.IO.println;
import static java.lang.IO.readln;

public class InputUtils {

    public static final String LINEBREAK = System.lineSeparator();

    private static BufferedReader br;

    /**
     * Reads one line of console input
     */
    public static String input() {
        try {
            return reader().readLine();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    /**
     * Prints the message then reads the next line of console input
     */
    public static String input(String message) {
        println(message);
        return input();
    }

    /**
     * Reads lines of console input until an empty line is entered
     */
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

    /**
     * Prints the message then reads following lines of console input until an empty line is entered
     */
    public static String multilineInput(String message) {
        println(message);
        return multilineInput();
    }

    private static synchronized BufferedReader reader() {
        if (br == null) {
            String enc = System.getProperty("stdin.encoding", "");
            Charset cs = Charset.forName(enc, StandardCharsets.UTF_8);
            br = new BufferedReader(new InputStreamReader(System.in, cs));
        }
        return br;
    }

    private InputUtils() {
    }

    /**
     * Resets the Reader used by input()
     * Used when manually setting console inputs with {@link System#setIn(InputStream)} for example in tests
     */
    public static void resetReader() {
        br = null;
    }

}
