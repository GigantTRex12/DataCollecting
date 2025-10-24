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

/**
 * Class for Analyzing existing Data. Subclasses can override any relevant methods.
 * Provides static evaluators for subclasses.
 * @param <T> Type of Data to Analyze
 */
public abstract class BaseDataAnalyzer<T extends BaseDataSet> {

    private static final Map<String, String> actions = Map.ofEntries(
            entry("a", "Analyze"),
            entry("p", "PrintData"),
            entry("e", "Exit")
    );

    /**
     * Takes a List of Objects and prints the String representation together with its percentage in the List.
     */
    protected static final Consumer<List<Object>> SIMPLE_PERCENTAGES = BaseDataAnalyzer::simplePercentages;
    /**
     * Takes a List of Objects and prints the String representation together with the Wilson Score confidence interval with a confidence of 0.95.
     * @see <a href="https://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval">https://en.wikipedia.org/wiki/Binomial_proportion_confidence_interval</a>
     */
    protected static final Consumer<List<Object>> WILSON_CONFIDENCE = BaseDataAnalyzer::percentageBasedConfidence;

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

    /**
     * Starts up the Analyzer and presents the user with the choice of these actions:
     * analyze; printData; exit.
     * Performs the chosen action and loops back to the choice.
     */
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

    /**
     * Provides the Questions used by this Analyzer.
     * Each Question represents one possible way to analyze the given Data.
     */
    protected abstract List<Question<T>> getQuestions();

    /**
     * The actual analysis.
     */
    protected void analyzation() {
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

    /**
     * Method of inputting the action to choose in {@link BaseDataAnalyzer#analyze()}
     */
    protected String inputAction() {
        String action = input("What would you like to do?").toLowerCase();
        return actions.getOrDefault(action, action).toLowerCase();
    }

    /**
     * Prints all DataSets with 1 per line.
     */
    protected void printData() {
        for (T data : data) {
            println(data);
        }
    }

    private static <R> void simplePercentages(List<R> values) {
        Counter<R> counter = new Counter<>(values);
        int total = counter.sum();
        counter.forEach((value, amount) -> println(
                value + ": "
                        + amount + "/" + total + " "
                        + Utils.numbertoStringWithComma(Math.round((float) (10_000 * amount) / total), 2) + "%"
        ));
    }

    private static <R> void percentageBasedConfidence(List<R> values) {
        Counter<R> counter = new Counter<>(values);
        int total = counter.sum();
        counter.forEach((value, amount) -> println(
                value + ": "
                        + Utils.toBinomialConfidenceRange(amount, total, 0.95, 2)
                        + " (" + amount + "/" + total + ")"
        ));
    }

}
