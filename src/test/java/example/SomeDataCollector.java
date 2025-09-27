package example;

import berlin.yuna.typemap.model.Type;
import collector.Question;
import exceptions.CollectorExceptionWrapper;

import java.util.List;

public class SomeDataCollector extends AbstractDataCollector<SomeDataSet> {

    public SomeDataCollector(String filename) {
        super(filename, SomeDataSet.class);
    }

    @Override
    protected List<Question> getQuestions() throws CollectorExceptionWrapper {
        return List.of(
                Question.ask("name", "Enter some name")
                        .validate((s, m) -> s.value().length() >= 4 ? Type.empty() : Type.typeOf("Must have at least 4 symbols"))
                        .build(),

                Question.ask("number", "Enter some number")
                        .regex("^0$|^[1-9]\\d*$")
                        .normalize((s, m) -> Integer.parseInt(s.value()))
                        .build(),

                Question.ask("someValue", "Enter some value")
                        .when(m -> (int) m.get("number") >= 10)
                        .normalize((s, m) -> "Value is: " + s.value())
                        .build()
        );
    }

}
