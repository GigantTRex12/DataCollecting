package collector;

import example.MetadataExample;
import example.example_one.SomeDataCollector;
import example.example_one.SomeDataSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutils.InputBuilder;
import testutils.TestWithOutputs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static example.JsonUtils.toJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SomeDataCollectorTest extends TestWithOutputs {

    private static final String ADD = "a";
    private static final String FIX = "fc";
    private static final String WHAT_DO = "What would you like to do?";
    private static final String FIX_MSG = "Set the fixed answers. Leave empty to skip. Enter \\ for empty String.";
    private static final MetadataExample METADATA_EXAMPLE = new MetadataExample("Test");

    private SomeDataCollector collector;

    private Path tempfile;

    @BeforeEach
    void setup() throws IOException {
        tempfile = Files.createTempFile("test", ".txt");
        tempfile.toFile().deleteOnExit();
        collector = new SomeDataCollector(tempfile.toString());
    }

    private List<String> getContent() throws IOException {
        return Files.readAllLines(tempfile).stream()
                .filter(s -> !s.trim().isEmpty()).collect(Collectors.toList());
    }

    @Test
    void add() throws IOException {
        // given
        InputBuilder.start()
                .line(ADD)
                .line("bla")
                .line("test")
                .line("NaN")
                .line(7)
                .line(ADD)
                .line("test2")
                .line(-15)
                .line(15)
                .line("value")
                .line(ADD)
                .line("test3")
                .line(0)
                .line("e")
                .set();

        SomeDataSet expected1 = new SomeDataSet(METADATA_EXAMPLE, "test", 7, null);
        SomeDataSet expected2 = new SomeDataSet(METADATA_EXAMPLE, "test2", 15, "Value is: value");
        SomeDataSet expected3 = new SomeDataSet(METADATA_EXAMPLE, "test3", 0, null);

        // when
        collector.collect();

        // then
        List<String> content = getContent();
        assertEquals(3, content.size());
        assertEquals(toJson(expected1), content.get(0));
        assertEquals(toJson(expected2), content.get(1));
        assertEquals(toJson(expected3), content.get(2));

        validateOutputs(new String[]{
                WHAT_DO,
                "Enter some name",
                "Invalid input: Must have at least 4 symbols",
                "Enter some name",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                "Invalid input: Input needs to match pattern ^0$|^[1-9]\\d*$",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                WHAT_DO,
                "Enter some name",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                "Invalid input: Input needs to match pattern ^0$|^[1-9]\\d*$",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                "Enter some value",
                WHAT_DO,
                "Enter some name",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                WHAT_DO
        });
    }

    @Test
    void fixChoices() throws IOException {
        // given
        InputBuilder.start()
                .line(FIX)
                .line("name")
                .emptyLine()
                .line("bla")
                .line(ADD)
                .line(50)
                .line(ADD)
                .line(3)
                .line(FIX)
                .emptyLine()
                .line("NaN")
                .line("blabla")
                .line(ADD)
                .line("name")
                .line(15)
                .line(FIX)
                .emptyLine()
                .line(20)
                .line("\\")
                .line(ADD)
                .line("longname")
                .line("e")
                .set();

        SomeDataSet expected1 = new SomeDataSet(METADATA_EXAMPLE, "name", 50, "Value is: bla");
        SomeDataSet expected2 = new SomeDataSet(METADATA_EXAMPLE, "name", 3, null);
        SomeDataSet expected3 = new SomeDataSet(METADATA_EXAMPLE, "name", 15, "Value is: blabla");
        SomeDataSet expected4 = new SomeDataSet(METADATA_EXAMPLE, "longname", 20, "Value is: ");

        // when
        collector.collect();

        // then
        List<String> content = getContent();
        assertEquals(4, content.size());
        assertEquals(toJson(expected1), content.get(0));
        assertEquals(toJson(expected2), content.get(1));
        assertEquals(toJson(expected3), content.get(2));
        assertEquals(toJson(expected4), content.get(3));

        validateOutputs(new String[]{
                WHAT_DO,
                FIX_MSG,
                "Enter some name",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                "Enter some value",
                WHAT_DO,
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                WHAT_DO,
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                WHAT_DO,
                FIX_MSG,
                "Enter some name",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                "Enter some value",
                WHAT_DO,
                "Enter some name",
                "Invalid input: Input needs to match pattern ^0$|^[1-9]\\d*$",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                WHAT_DO,
                FIX_MSG,
                "Enter some name",
                "Enter some number",
                "Format: ^0$|^[1-9]\\d*$",
                "Enter some value",
                WHAT_DO,
                "Enter some name",
                WHAT_DO
        });
    }

}
