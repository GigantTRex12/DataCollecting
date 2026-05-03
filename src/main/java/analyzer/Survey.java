package analyzer;

import dataset.BaseDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Utils.InputUtils.input;
import static java.lang.IO.println;

public class Survey {

    /**
     * Runs the given questions against the given data
     */
    public static <T extends BaseDataSet> void run(Question<T> questions, List<T> data) {
        List<String> groupReps = new ArrayList<>();
        List<Function<T, ?>> groupings = new ArrayList<>();
        for (GroupingDefinition<T> gd : questions.groupings()) {
            if (!gd.forced()) {
                String f = input("Do you want to group by " + gd.name() + "? (y|yes)").toLowerCase();
                if (!(f.equals("yes") || f.equals("y"))) continue;
            }
            groupReps.add(gd.toString());
            groupings.add(gd.function());
        }

        if (groupings.isEmpty()) {
            questions.evaluator().accept(data.stream().filter(questions.conditionAll()).toList());
            return;
        }
        Map<List<?>, List<T>> groupedData = data.stream()
                .filter(questions.conditionAll())
                .collect(Collectors.groupingBy(t -> {
                    List<Object> list = new ArrayList<>();
                    groupings.forEach(g -> list.add(g.apply(t)));
                    return list;
                }));

        groupedData.forEach((keys, values) -> {
            println(values.size() + " grouped Datasets with:");
            println(String.join(", ", groupReps));
            println(keys.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            questions.evaluator().accept(values);
        });
    }

    private Survey() {
    }

}
