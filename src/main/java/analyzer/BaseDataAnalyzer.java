package analyzer;

import Utils.Counter;
import Utils.Utils;
import dataset.BaseDataSet;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static Utils.InputUtils.input;
import static java.lang.IO.println;
import static java.util.Map.entry;

public abstract class BaseDataAnalyzer<T extends BaseDataSet> {

    private static final Map<String, String> actions = Map.ofEntries(
            entry("a", "Analyze"),
            entry("p", "PrintData"),
            entry("e", "Exit")
    );

    protected static final Consumer<List<?>> WILSON_CONFIDENCE = BaseDataAnalyzer::percentageBasedConfidence;

    protected final List<T> data;

    protected final List<Question<T>> questions;
    protected final List<String> questionNames;

    public BaseDataAnalyzer(List<T> data) {
        this.data = data;
        questions = getQuestions();
        questionNames = questions.stream().map(Question::toString).toList();
        if (questionNames.size() != new HashSet<>(questionNames).size()) {
            throw new IllegalStateException("Multiple Questions can't have the same name.");
        }
    }

    public void analyze() {
        while (true) {
            String action = inputAction();
            if (action.equals("exit")) break;
            switch (action) {
                case ("analyze") -> analyzation();
                case ("printdata") -> printData();
                default -> println("Unknown action: " + action);
            }
        }
    }

    protected abstract List<Question<T>> getQuestions();

    private void analyzation() {
        String o = "Choose what to analyze. Options:" + System.lineSeparator();
        o += questions.stream().map(Question::toString).collect(Collectors.joining(", "));
        while (true) {
            final String picked = input(o);
            if (questionNames.stream().anyMatch(n -> n.equalsIgnoreCase(picked))) {
                Survey.run(
                        questions.stream()
                                .filter(q -> q.toString().equalsIgnoreCase(picked))
                                .findAny().orElseThrow(() -> new IllegalStateException("Should not be able to pick a non-existent question")),
                        data
                );
                break;
            }
            println("Invalid question.");
        }

    }

    protected String inputAction() {
        String action = input("What would you like to do?").toLowerCase();
        return actions.getOrDefault(action, action).toLowerCase();
    }

    protected void printData() {
        for (T data : data) {
            println(data);
        }
    }

    private static <R> void percentageBasedConfidence(List<R> values) {
        Counter<R> counter = new Counter<>(values);
        int total = counter.sum();
        counter.forEachNonZero((value, amount) -> println(
                (value != null ? value.toString() : "null") + ": "
                        + Utils.toBinomialConfidenceRange(amount, total, 0.95, 2)
        ));
    }

}
