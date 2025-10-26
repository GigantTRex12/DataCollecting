package example.example_two;

import collector.Question;
import example.AbstractDataCollector;
import example.MetadataExample;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OtherDataCollector extends AbstractDataCollector<OtherDataSet> {

    public OtherDataCollector(String filename) {
        super(filename);
    }

    @Override
    protected List<Question> getQuestions() {
        return List.of(
                Question.ask("name", "Enter the name")
                        .validate((s, _) -> s.length() > 2 ? Optional.empty() : Optional.of("Must have at least 3 symbols"))
                        .emptyToNull()
                        .build(),
                Question.ask("values", "Enter values in format value1|value2")
                        .regex("^\\w*\\|\\w*$")
                        .normalizers(Map.ofEntries(
                                Map.entry("value1", answer -> answer.split("\\|")[0]),
                                Map.entry("value2", answer -> answer.split("\\|")[1])
                        ))
                        .build()
        );
    }

    @Override
    protected OtherDataSet mapToDataset(Map<String, Object> map) {
        return new OtherDataSet(
                (MetadataExample) currMetadata,
                (String) map.get("name"),
                (String) map.get("value1"),
                (String) map.get("value2")
        );
    }

}
