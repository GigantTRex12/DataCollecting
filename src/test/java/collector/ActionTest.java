package collector;

import example.other_examples.ActionTestDataCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutils.InputBuilder;
import testutils.TestWithOutputs;

import java.io.IOException;

public class ActionTest extends TestWithOutputs {

    private static final String OPTIONS = "Options: PrintData (p); FixChoices (fc); Exit (e); test1; test2 (t2|tt2); TEst3 (t3); mmm (m); sss (ssss)";
    private static final String WHAT_DO = "What would you like to do?";

    private ActionTestDataCollector collector;

    @BeforeEach
    void setup() {
        collector = new ActionTestDataCollector();
    }

    @Test
    void actions() throws IOException {
        // given
        InputBuilder.start()
                .line("save")
                .line("invalid")
                .line("ssss")
                .line("sss")
                .line("printdATA")
                .line("p")
                .line("mmm")
                .line("m")
                .line("fixchoices")
                .line("fc")
                .line("test1")
                .line("test2")
                .line("t2")
                .line("tt2")
                .line("test3")
                .line("t3")
                .line("e")
                .set();

        // when
        collector.collect();

        // then
        validateOutputs(new String[]{
                WHAT_DO,
                OPTIONS,
                "save is not a valid option.",
                WHAT_DO,
                OPTIONS,
                "invalid is not a valid option.",
                WHAT_DO,
                OPTIONS,
                "s",
                WHAT_DO,
                OPTIONS,
                "s",
                WHAT_DO,
                OPTIONS,
                "print",
                WHAT_DO,
                OPTIONS,
                "print",
                WHAT_DO,
                OPTIONS,
                "m",
                WHAT_DO,
                OPTIONS,
                "m",
                WHAT_DO,
                OPTIONS,
                "f",
                WHAT_DO,
                OPTIONS,
                "f",
                WHAT_DO,
                OPTIONS,
                "TEST1",
                WHAT_DO,
                OPTIONS,
                "TEST2",
                WHAT_DO,
                OPTIONS,
                "TEST2",
                WHAT_DO,
                OPTIONS,
                "TEST2",
                WHAT_DO,
                OPTIONS,
                "TEST3",
                WHAT_DO,
                OPTIONS,
                "TEST3",
                WHAT_DO,
                OPTIONS
        });
    }

}
