package example.other_examples;

import collector.Question;
import example.AbstractDataCollector;
import example.example_one.SomeDataSet;

import java.util.List;
import java.util.Map;

import static java.lang.IO.println;

public class ActionTestDataCollector extends AbstractDataCollector<SomeDataSet> {

    public ActionTestDataCollector() {
        super(null);
        actions.put("test1", () -> println("TEST1"));
        actions.put("test2", () -> println("TEST2"), List.of("t2", "tt2"));
        actions.put("TEst3", () -> println("TEST3"), List.of("t3"));
        actions.remove("A");
        actions.remove("cc");
        actions.remove("c");
        actions.replace("p", () -> println("print"));
        actions.removeKeys("f", "s", "save");
        actions.renameKey("pickmetadata", "mmm");
        actions.addKeys("save", "ssss", "sss");
        actions.replace("ssss", () -> println("s"));
        actions.replace("m", () -> println("m"));
        actions.replace("fc", () -> println("f"));
        actions.renameKey("save", "sss");
    }

    @Override
    protected List<Question> getQuestions() {
        return List.of();
    }

    @Override
    protected SomeDataSet mapToDataset(Map<String, Object> map) {
        return null;
    }

    @Override
    protected void exit() {
        this.running = false;
    }
}
