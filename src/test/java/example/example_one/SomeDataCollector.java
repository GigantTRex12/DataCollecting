package example.example_one;

import collector.Question;
import example.AbstractDataCollector;
import example.MetadataExample;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SomeDataCollector extends AbstractDataCollector<SomeDataSet> {

    public SomeDataCollector(String filename) {
        super(filename);
    }

    @Override
    protected List<Question> getQuestions() {
        return List.of(
                Question.ask("name", "Enter some name")
                        .validate((s, _) -> s.length() >= 4 ? Optional.empty() : Optional.of("Must have at least 4 symbols"))
                        .build(),

                Question.ask("number", "Enter some number")
                        .regex("^0$|^[1-9]\\d*$")
                        .normalize(s -> Integer.parseInt(s))
                        .build(),

                Question.ask("someValue", "Enter some value")
                        .when(m -> (int) m.get("number") >= 10)
                        .normalize(s -> "Value is: " + s)
                        .build()
        );
    }

    @Override
    protected SomeDataSet mapToDataset(Map<String, Object> map) {
        return new SomeDataSet(
                (MetadataExample) currMetadata,
                (String) map.get("name"),
                (Integer) map.get("number"),
                (String) map.get("someValue")
        );
    }

}
