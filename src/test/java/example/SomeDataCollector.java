package example;

import collector.Question;
import exceptions.CollectorExceptionWrapper;

import java.util.List;
import java.util.Optional;

public class SomeDataCollector extends AbstractDataCollector<SomeDataSet> {

    public SomeDataCollector(String filename) {
        super(filename, SomeDataSet.class);
    }

    @Override
    protected List<Question> getQuestions() {
        return List.of(
                Question.ask("name", "Enter some name")
                        .validate((s, _) -> s.length() >= 4 ? Optional.empty() : Optional.of("Must have at least 4 symbols"))
                        .build(),

                Question.ask("number", "Enter some number")
                        .regex("^0$|^[1-9]\\d*$")
                        .normalize((s, _) -> Integer.parseInt(s))
                        .build(),

                Question.ask("someValue", "Enter some value")
                        .when(m -> (int) m.get("number") >= 10)
                        .normalize((s, _) -> "Value is: " + s)
                        .build()
        );
    }

}
