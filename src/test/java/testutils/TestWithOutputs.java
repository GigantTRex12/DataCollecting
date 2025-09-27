package testutils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestWithOutputs {

    private Path outputFile;

    private PrintStream oldStream;

    @BeforeEach
    void setup_Output() throws IOException {
        oldStream = System.out;
        outputFile = Files.createTempFile("output", ".txt");
        outputFile.toFile().deleteOnExit();
        System.setOut(new PrintStream(outputFile.toFile()));
    }

    @AfterEach
    void cleanup_Output() {
        System.setOut(oldStream);
    }

    protected void validateOutputs(String[] expectedOutputs) throws IOException {
        List<String> output = Files.readAllLines(outputFile);
        int pos = 0;
        for (String expected : expectedOutputs) {
            if (expected != null) {
                assertEquals(expected, output.get(pos));
            }
            pos++;
        }
    }

}
