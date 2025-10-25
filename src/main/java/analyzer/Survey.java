package analyzer;

import dataset.BaseDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Utils.InputUtils.input;
import static java.lang.IO.println;

class Survey {

    static <T extends BaseDataSet> void run(Question<T> question, List<T> data) {
        List<String> groupReps = new ArrayList<>();
        List<Function<T, ?>> groupings = new ArrayList<>();
        for (GroupingDefinition<T> gd : question.groupings()) {
            if (!gd.forced()) {
                String f = input("Do you want to group by " + gd.name() + "? (y|yes)").toLowerCase();
                if (!(f.equals("yes") || f.equals("y"))) continue;
            }
            groupReps.add(gd.toString());
            groupings.add(gd.function());
        }

        if (groupings.isEmpty()) {
            question.evaluator().accept(data.stream().filter(question.conditionAll()).toList());
            return;
        }
        Map<List<?>, List<T>> groupedData = data.stream()
                .filter(question.conditionAll())
                .collect(Collectors.groupingBy(t -> {
                    List<Object> list = new ArrayList<>();
                    groupings.forEach(g -> list.add(g.apply(t)));
                    return list;
                }));

        groupedData.forEach((keys, values) -> {
            println(values.size() + " grouped Datasets with:");
            println(String.join(", ", groupReps));
            println(keys.stream().map(String::valueOf).collect(Collectors.joining(", ")));
            question.evaluator().accept(values);
        });
    }

    private Survey() {
    }

}
