package collector;

import example.MetadataExample;
import example.example_two.OtherDataCollector;
import example.example_two.OtherDataSet;
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

public class OtherDataCollectorTest extends TestWithOutputs {

    private static final String ADD = "a";
    private static final String ADD_OPTIONS = "Options: AddData (a); ClearData (c); Save (s); PrintData (p); PickMetadata (m); FixChoices (f|fc); ClearFixedChoices (cc); Exit (e)";
    private static final String WHAT_DO = "What would you like to do?";
    private static final MetadataExample METADATA_EXAMPLE = new MetadataExample("Test");

    private OtherDataCollector collector;

    private Path tempfile;

    @BeforeEach
    void setup() throws IOException {
        tempfile = Files.createTempFile("test", ".txt");
        tempfile.toFile().deleteOnExit();
        collector = new OtherDataCollector(tempfile.toString());
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
                .line("name")
                .line("test")
                .line("test1|test2")
                .line(ADD)
                .line("")
                .line("value1|value2")
                .line("e")
                .set();

        OtherDataSet expected1 = new OtherDataSet(METADATA_EXAMPLE, "name", "test1", "test2");
        OtherDataSet expected2 = new OtherDataSet(METADATA_EXAMPLE, null, "value1", "value2");

        // when
        collector.collect();

        // then
        List<String> content = getContent();
        assertEquals(2, content.size());
        assertEquals(toJson(expected1), content.get(0));
        assertEquals(toJson(expected2), content.get(1));

        validateOutputs(new String[]{
                WHAT_DO,
                ADD_OPTIONS,
                "Enter the name",
                "Enter values in format value1|value2",
                "Format: ^\\w*\\|\\w*$",
                "Invalid input: Input needs to match pattern ^\\w*\\|\\w*$",
                "Enter values in format value1|value2",
                "Format: ^\\w*\\|\\w*$",
                WHAT_DO,
                ADD_OPTIONS,
                "Enter the name",
                "Enter values in format value1|value2",
                "Format: ^\\w*\\|\\w*$",
                WHAT_DO,
                ADD_OPTIONS
        });
    }

}
