package example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileUtils {

    private static final String lineSeperator = System.lineSeparator();

    private static void create(File file) throws IOException {
        if (!file.createNewFile()) {
            throw new IllegalStateException("File already exists!");
        }
    }

    private static void create(File file, String content) throws IOException {
        create(file);
        overwrite(file, content);
    }

    private static String read(File file) throws FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("File " + file.getPath() + " doesn't exist");
        }
        Scanner scanner = new Scanner(file);
        StringBuilder result = new StringBuilder();
        while (scanner.hasNextLine()) {
            result.append(scanner.nextLine()).append(lineSeperator);
        }
        scanner.close();
        return result.toString().strip();
    }

    private static void overwrite(File file, String content) throws IOException {
        if (file.exists()) {
            if (!file.canWrite()) {
                throw new IllegalStateException("File " + file.getPath() + " cannot be written");
            }
            FileWriter writer = new FileWriter(file);
            writer.write(content);
            writer.close();
        }
        else {
            create(file, content);
        }
    }

    public static void append(String filename, String content) throws IOException {
        File file = new File(filename);
        if (file.exists()) {
            String fullContent = read(file) + lineSeperator + content;
            fullContent = fullContent.strip();
            overwrite(file, fullContent);
        }
        else {
            create(file, content);
        }
    }

}
