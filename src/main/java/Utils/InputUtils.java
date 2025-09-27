package Utils;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class InputUtils {

    private static Scanner scanner = new Scanner(System.in);

    public static String input() {
        return scanner.nextLine();
    }

    public static String input(String message) {
        System.out.println(message);
        return input();
    }

    public static String multilineInput() {
        StringBuilder sb = new StringBuilder();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }
            sb.append(line).append("\n");
        }
        if (sb.length() == 0) {
            return "";
        }
        sb.deleteCharAt(sb.length() - 1); // delete the last \n character
        return sb.toString();
    }

    public static String multilineInput(String message) {
        System.out.println(message);
        return multilineInput();
    }

    public static void setInputStream(String input) { // Is needed for tests
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        scanner = new Scanner(System.in); // otherwise setting a new InputStream breaks things
    }

}
