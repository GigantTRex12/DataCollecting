package example;

import collector.BaseDataCollector;

import java.io.IOException;
import java.util.stream.Collectors;

public abstract class AbstractDataCollector<T extends AbstractDataSet> extends BaseDataCollector<T> {

    private final String filename;

    public AbstractDataCollector(String filename) {
        this.filename = filename;
    }

    @Override
    protected void setMetadata() {
        currMetadata = new MetadataExample("Test");
    }

    @Override
    protected void saveData() {
        String jsons = data.stream().map(JsonUtils::toJson).collect(Collectors.joining(System.lineSeparator()));
        try {
            FileUtils.append(filename, jsons);
        } catch (IOException e) {
            IO.println("Failed to save data: " + e.getMessage());
            return;
        }
        data.clear();
    }

}
