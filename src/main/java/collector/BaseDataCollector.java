package collector;

import dataset.BaseDataSet;
import dataset.Metadata;
import exceptions.CollectorExceptionWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    protected List<T> data = new ArrayList<>();

    protected Metadata currMetadata;

    protected BaseDataCollector() {
        survey = new Survey(getQuestions());
    }

    /**
     * Starts up the Analyzer and presents the user with the choice of these actions:
     * addData; clearData; save; printData; pickMetadata; fixChoices; clearFixedChoices; exit.
     * Performs the chosen action and loops back to the choice.
     */
    public void collect() throws CollectorExceptionWrapper {
        setMetadata();
        while (true) {
            String action = inputAction();
            if (action.equals("exit")) {
                saveData();
                break;
            }
            executeAction(action);
        }
    }

    protected void executeAction(String action) throws CollectorExceptionWrapper {
        switch (action) {
            case ("adddata") -> addData();
            case ("cleardata") -> this.data.clear();
            case ("save") -> saveData();
            case ("printdata") -> printData();
            case ("pickmetadata") -> setMetadata();
            case ("fixchoices") -> fixChoices();
            case ("clearfixedchoices") -> clearFixedChoices();
            default -> println("Unknown action: " + action);
        }
    }

    /**
     * Runs a Survey of Questions, requesting user input to create a new DataSet and save it.
     */
    protected void addData() throws CollectorExceptionWrapper {
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
    protected boolean validateDataSet(BaseDataSet dataSet) throws CollectorExceptionWrapper {
        return true;
    }

    // TODO: should have default implementation

    /**
     * Called to transform a key-value Map with Strings as keys into the corresponding DataSet.
     * The keys are the Strings given as key in the {@link Question}.
     */
    protected abstract T mapToDataset(Map<String, Object> map) throws CollectorExceptionWrapper;

    /**
     * Used to set the Metadata this Collector remembers and may add to created DataSets.
     * Can be left unused for DataSets not needing {@link Metadata}.
     * Is always called once on starting up the Collector with {@link BaseDataCollector#collect()}.
     * May use user input to create the Metadata.
     */
    protected void setMetadata() throws CollectorExceptionWrapper {
        currMetadata = null;
    }

    /**
     * Method of inputting the action to choose in {@link BaseDataCollector#collect()}.
     */
    protected String inputAction() throws CollectorExceptionWrapper {
        String action = input("What would you like to do?").toLowerCase();
        return actions.getOrDefault(action, action).toLowerCase();
    }

    /**
     * Prints all collected and unsaved DataSets with 1 per line.
     */
    protected void printData() throws CollectorExceptionWrapper {
        for (T data : data) {
            println(data);
        }
    }

    /**
     * Called when the "save" action is executed.
     */
    protected abstract void saveData() throws CollectorExceptionWrapper;

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

}
