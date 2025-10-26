package analyzer;

import example.MetadataExample;
import example.example_one.SomeDataAnalyzer;
import example.example_one.SomeDataSet;
import org.junit.jupiter.api.Test;
import testutils.InputBuilder;
import testutils.TestWithOutputs;

import java.io.IOException;
import java.util.List;

public class SomeDataAnalyzerTest extends TestWithOutputs {

    private static final MetadataExample M = new MetadataExample();

    private static final String ANALYZE = "a";
    private static final String WHAT_DO = "What would you like to do?";
    private static final String ANALYZE_OPTIONS1 = "Choose what to analyze. Options:";
    private static final String ANALYZE_OPTIONS2 = "Name, Some_Value";
    private static final String YES = "y";
    private static final String NO = "n";

    private SomeDataAnalyzer analyzer;

    @Test
    void analyze() throws IOException {
        // given
        analyzer = new SomeDataAnalyzer(List.of(
                new SomeDataSet(M, "Name 1", 50, "Value 1"),
                new SomeDataSet(M, "Name 2", 20, "Value 2"),
                new SomeDataSet(M, "Name 3", 42, "Value 2"),
                new SomeDataSet(M, "Name 4", -17, "Value 3"),
                new SomeDataSet(M, null, 22, "Value 2"),
                new SomeDataSet(M, null, -33, "Value 3"),
                new SomeDataSet(M, "Name 1", 24, "Value 1"),
                new SomeDataSet(M, "Name 1", -1, "Value 1"),
                new SomeDataSet(M, "Name 2", 0, "Value 1"),
                new SomeDataSet(M, "Name 2", 46, "Value 2"),
                new SomeDataSet(M, "Name 3", 90, "Value 1"),
                new SomeDataSet(M, "Name 2", -12, "Value 1"),
                new SomeDataSet(M, "Name 1", 0, "Value 1")
        ));
        InputBuilder.start()
                .line(ANALYZE)
                .line("name")
                .line(YES)
                .line(ANALYZE)
                .line("name")
                .line(NO)
                .line(ANALYZE)
                .line("some_value")
                .line("e")
                .set();

        final String GROUP_QUESTION = "Do you want to group by number_positive? (y|yes)";
        final String GROUPED_VALUES1 = " grouped Datasets with:";
        final String GROUPED_VALUES2 = "number_positive";
        final String GROUPED_VALUES3 = "Grouping 1";

        // when
        analyzer.analyze();

        // then
        // TODO: order of evaluation outputs might not be deterministic
        validateOutputs(new String[]{
                WHAT_DO,
                ANALYZE_OPTIONS1,
                ANALYZE_OPTIONS2,
                GROUP_QUESTION,
                5 + GROUPED_VALUES1,
                GROUPED_VALUES2,
                "false",
                "Name 4: [3.62% - 62.45%] (1/5)",
                "Name 2: [11.76% - 76.93%] (2/5)",
                "Name 1: [11.76% - 76.93%] (2/5)",
                6 + GROUPED_VALUES1,
                GROUPED_VALUES2,
                "true",
                "Name 3: [9.68% - 70%] (2/6)",
                "Name 2: [9.68% - 70%] (2/6)",
                "Name 1: [9.68% - 70%] (2/6)",
                WHAT_DO,
                ANALYZE_OPTIONS1,
                ANALYZE_OPTIONS2,
                GROUP_QUESTION,
                "Name 4: [1.62% - 37.74%] (1/11)",
                "Name 3: [5.14% - 47.7%] (2/11)",
                "Name 2: [15.17% - 64.62%] (4/11)",
                "Name 1: [15.17% - 64.62%] (4/11)",
                WHAT_DO,
                ANALYZE_OPTIONS1,
                ANALYZE_OPTIONS2,
                4 + GROUPED_VALUES1,
                GROUPED_VALUES3,
                "Name 1",
                "Value 1: [51.01% - 100%] (4/4)",
                1 + GROUPED_VALUES1,
                GROUPED_VALUES3,
                "Name 4",
                "Value 3: [20.65% - 100%] (1/1)",
                2 + GROUPED_VALUES1,
                GROUPED_VALUES3,
                "Name 3",
                "Value 1: [9.45% - 90.55%] (1/2)",
                "Value 2: [9.45% - 90.55%] (1/2)",
                2 + GROUPED_VALUES1,
                GROUPED_VALUES3,
                "null",
                "Value 3: [9.45% - 90.55%] (1/2)",
                "Value 2: [9.45% - 90.55%] (1/2)",
                4 + GROUPED_VALUES1,
                GROUPED_VALUES3,
                "Name 2",
                "Value 1: [15% - 85%] (2/4)",
                "Value 2: [15% - 85%] (2/4)",
                WHAT_DO
        });
    }

}
