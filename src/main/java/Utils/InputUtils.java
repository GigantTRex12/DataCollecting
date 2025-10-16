package Utils;

import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static java.lang.IO.println;
import static java.lang.IO.readln;

public class InputUtils {

    public static final String LINEBREAK = System.lineSeparator();

    private static BufferedReader br;

    public static String input() {
        try {
            return reader().readLine();
        } catch (IOException ioe) {
            throw new IOError(ioe);
        }
    }

    public static String input(String message) {
        println(message);
        return input();
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

    private static synchronized BufferedReader reader() {
        if (br == null) {
            String enc = System.getProperty("stdin.encoding", "");
            Charset cs = Charset.forName(enc, StandardCharsets.UTF_8);
            br = new BufferedReader(new InputStreamReader(System.in, cs));
        }
        return br;
    }

    public static void resetReader() {
        br = null;
    }

}
