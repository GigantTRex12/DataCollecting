package collector;

import Utils.DoubleKeyMap;
import Utils.NoCaseString;
import dataset.BaseDataSet;
import dataset.Metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static Utils.InputUtils.input;
import static java.lang.IO.println;
import static java.util.Map.entry;

/**
 * Class for Collecting new Data. Subclasses can override any relevant methods.
 *
 * @param <T> Type of Data to Collect.
 */
public abstract class BaseDataCollector<T extends BaseDataSet> {

    private static final Map<String, String> actions = Map.ofEntries(
            entry("a", "AddData"),
            entry("c", "ClearData"),
            entry("s", "Save"),
            entry("p", "PrintData"),
            entry("m", "PickMetadata"),
            entry("fc", "FixChoices"),
            entry("cc", "ClearFixedChoices"),
            entry("e", "Exit")
    );

    private final Survey survey;
    private boolean running;

    protected List<T> data = new ArrayList<>();

    protected Metadata currMetadata;

    protected final DoubleKeyMap<NoCaseString, Runnable> actionMap;

    protected BaseDataCollector() {
        survey = new Survey(getQuestions());
        actionMap = new DoubleKeyMap<NoCaseString, Runnable>();
        addAction("AddData", "a", this::addData);
        addAction("ClearData", "c", this::clearData);
        addAction("Save", "s", this::saveData);
        addAction("PrintData", "p", this::printData);
        addAction("PickMetadata", "m", this::setMetadata);
        addAction("FixChoices", "fc", this::fixChoices);
        addAction("ClearFixedChoices", "cc", this::clearFixedChoices);
        addAction("Exit", "e", () -> {this.saveData(); this.running = false;});
    }

    /**
     * Starts up the Analyzer and presents the user with the choice of the actions available in the {@link #actionMap}.
     * Performs the chosen action and loops back to the choice.
     */
    public void collect() {
        running = true;
        setMetadata();
        while (running) {
            executeAction(inputAction());
        }
    }

    protected void executeAction(String action) {
        Runnable execute = actionMap.get(new NoCaseString(action));
        if (execute == null) println(action + " is not a valid action.");
        else execute.run();
    }

    /**
     * Runs a Survey of Questions, requesting user input to create a new DataSet and save it.
     */
    protected void addData() {
        Map<String, Object> typeMap = survey.run();
        T dataSet = mapToDataset(typeMap);
        if (validateDataSet(dataSet)) {
            this.data.add(dataSet);
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
    protected boolean validateDataSet(BaseDataSet dataSet) {
        return true;
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
        return input("What would you like to do?" + System.lineSeparator() + actionMap.keyReps());
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

    protected void addAction(String actionLong, String actionShort, Runnable action) {
        actionMap.put(new NoCaseString(actionLong), new NoCaseString(actionShort), action);
    }

}
