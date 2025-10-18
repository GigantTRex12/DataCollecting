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

    private void addData() throws CollectorExceptionWrapper {
        Map<String, Object> typeMap = survey.run(currMetadata);
        T dataSet = mapToDataset(typeMap);
        if (validateDataSet(dataSet)) {
            this.data.add(dataSet);
        }
    }

    protected abstract List<Question> getQuestions();

    protected abstract Class<? extends T> getGenericClass() throws CollectorExceptionWrapper;

    protected boolean validateDataSet(BaseDataSet dataSet) throws CollectorExceptionWrapper {
        return true;
    }

    // TODO: should have default implementation
    protected abstract T mapToDataset(Map<String, Object> map) throws CollectorExceptionWrapper;

    protected void setMetadata() throws CollectorExceptionWrapper {
        currMetadata = null;
    }

    protected String inputAction() throws CollectorExceptionWrapper {
        String action = input("What would you like to do?").toLowerCase();
        return actions.getOrDefault(action, action).toLowerCase();
    }

    protected void printData() throws CollectorExceptionWrapper {
        for (T data : data) {
            println(data);
        }
    }

    protected abstract void saveData() throws CollectorExceptionWrapper;

    private void fixChoices() {
        survey.presetAnswers();
    }

    private void clearFixedChoices() {
        survey.clearPresetAnswers();
    }

}
