package collector;

import Utils.ActionMap;
import Utils.InputUtils;
import dataset.BaseDataSet;
import dataset.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static Utils.InputUtils.input;
import static java.lang.IO.println;

/**
 * Class for Collecting new Data. Subclasses can override any relevant methods.
 *
 * @param <T> Type of Data to Collect.
 */
public abstract class BaseDataCollector<T extends BaseDataSet> {

    private final Survey survey;

    protected List<T> data = new ArrayList<>();

    protected Metadata currMetadata;

    protected final ActionMap actions;

    protected boolean running;

    protected BaseDataCollector() {
        survey = new Survey(getQuestions());
        actions = new ActionMap();
        actions.put("AddData", this::addData, List.of("a"));
        actions.put("AddDataMultiple", this::addMultipleDatasets, List.of("am"));
        actions.put("ClearData", this::clearData, List.of("c"));
        actions.put("Save", this::saveData, List.of("s"));
        actions.put("PrintData", this::printData, List.of("p"));
        actions.put("PickMetadata", this::setMetadata, List.of("m"));
        actions.put("FixChoices", this::fixChoices, List.of("f", "fc"));
        actions.put("ClearFixedChoices", this::clearFixedChoices, List.of("cc"));
        actions.put("Exit", this::exit, List.of("e"));
        running = false;
    }

    /**
     * Starts up the Analyzer and presents the user with the choice of these actions:
     * addData; clearData; save; printData; pickMetadata; fixChoices; clearFixedChoices; exit.
     * Performs the chosen action and loops back to the choice.
     */
    public void collect() {
        setMetadata();
        running = true;
        while (running) {
            String action = inputAction();
            executeAction(action);
        }
    }

    protected void executeAction(String action) {
        actions.acceptOrFallback(action, () -> println(action + " is not a valid option."));
    }

    /**
     * Runs a Survey of Questions, requesting user input to create a new DataSet and save it.
     */
    protected void addData() {
        Map<String, Object> typeMap = survey.run();
        T dataSet = mapToDataset(typeMap);
        if (validateDataSet(dataSet)) this.data.add(dataSet);
        else this.onInvalidDataSet(dataSet);
    }

    /**
     * Requests the user to input a number, then runs {@link BaseDataCollector#addData()} that many times.
     * Executes {@link BaseDataCollector#printMultipleSep(int, int)} for each Dataset to be added.
     */
    protected void addMultipleDatasets() {
        int amount = InputUtils.inputInt("How many datasets do you want to add?");
        for (int i = 1; i <= amount; i++) {
            printMultipleSep(i, amount);
            this.addData();
        }
    }

    /**
     * Used to get an ordered List of all {@link Question} needed to create corresponding DataSets.
     */
    protected abstract List<Question> getQuestions();

    /**
     * Called before adding a new DataSet to validate it.
     * If this returns false for a DataSet the DataSet will be discarded.
     */
    protected boolean validateDataSet(T dataSet) {
        return true;
    }

    /**
     * Called when a DataSet fails {@link BaseDataCollector#validateDataSet(BaseDataSet)}.
     * @param dataSet The invalid DataSet
     */
    protected void onInvalidDataSet(T dataSet) {
        println("Invalid Dataset: " + dataSet);
    }

    /**
     * Called when adding multiple Datasets with {@link BaseDataCollector#addMultipleDatasets()} to print a separator
     * message.
     * @param pos The number, which Dataset is being added.
     * @param max The total number of Datasets being added.
     */
    protected void printMultipleSep(int pos, int max) {
        println("----- Adding dataset number " + pos + " out of " + max + " -----");
    }

    /**
     * Called to transform a key-value Map with Strings as keys into the corresponding DataSet.
     * The keys are the Strings given as key in the {@link Question}.
     */
    protected abstract T mapToDataset(Map<String, Object> map);

    /**
     * Used to set the Metadata this Collector remembers and may add to created DataSets.
     * Can be left unused for DataSets not needing {@link Metadata}.
     * Is always called once on starting up the Collector with {@link BaseDataCollector#collect()}.
     * May use user input to create the Metadata.
     */
    protected void setMetadata() {
        currMetadata = null;
    }

    /**
     * Method of inputting the action to choose in {@link BaseDataCollector#collect()}.
     */
    protected String inputAction() {
        return input("What would you like to do?" + System.lineSeparator() + "Options: " + actions.keyReps("; ", "|"));
    }

    /**
     * Prints all collected and unsaved DataSets with 1 per line.
     */
    protected void printData() {
        for (T data : data) {
            println(data);
        }
    }

    /**
     * Called when the "save" action is executed.
     * <br>
     * When implementing this should end with a call of {@link BaseDataCollector#clearData()} on success
     */
    protected abstract void saveData();

    /**
     * Simply removes all unsaved collected data.
     * May be overridden to for example add extra validation.
     */
    protected void clearData() {
        this.data.clear();
    }

    /**
     * Goes through all Questions and lets the user optionally input responses to those Questions that will automatically be used when the Question would otherwise be asked.
     * Does not check or validate the responses immediately, but invalid responses are removed as they come up.
     * Also resets any previously fixed choices.
     */
    protected void fixChoices() {
        survey.presetAnswers();
    }

    /**
     * Resets any fixed choices made via {@link BaseDataCollector#fixChoices()}
     */
    protected void clearFixedChoices() {
        survey.clearPresetAnswers();
    }

    /**
     * Exits the loop in the {@link #collect()} method, exiting the analyzer.
     */
    protected void exit() {
        this.saveData();
        this.running = false;
    }

}
