package example.example_one;

import analyzer.BaseDataAnalyzer;
import analyzer.GroupingDefinition;
import analyzer.Question;

import java.util.List;

public class SomeDataAnalyzer extends BaseDataAnalyzer<SomeDataSet> {

    public SomeDataAnalyzer(List<SomeDataSet> data) {
        super(data);
    }

    @Override
    protected List<Question<SomeDataSet>> getQuestions() {
        return List.of(
                Question.ask("Name", SomeDataSet.class)
                        .evaluator(SomeDataSet::getName, WILSON_CONFIDENCE)
                        .conditionAll(t -> t.getName() != null)
                        .groupings(new GroupingDefinition<>("number_positive", t -> t.getNumber() > 0))
                        .build(),
                Question.ask("Some_Value", SomeDataSet.class)
                        .evaluator(SomeDataSet::getSomeValue, WILSON_CONFIDENCE)
                        .forcedGrouping(SomeDataSet::getName)
                        .build()
        );
    }
}
