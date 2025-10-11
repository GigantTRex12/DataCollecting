package example;

import collector.BaseDataCollector;
import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.CollectorExceptionWrapper;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractDataCollector<T extends AbstractDataSet> extends BaseDataCollector<T> {

    private final String filename;
    private final Class<T> t;

    public AbstractDataCollector(String filename, Class<T> t) {
        this.filename = filename;
        this.t = t;
    }

    @Override
    protected void setMetadata() {
        currMetadata = new MetadataExample("Test");
    }

    @Override
    protected Class<? extends T> getGenericClass() {
        return t;
    }

    @Override
    protected void saveData() throws CollectorExceptionWrapper {
        String jsons = data.stream().map(JsonUtils::toJson).collect(Collectors.joining(System.lineSeparator()));
        try {
            FileUtils.append(filename, jsons);
        } catch (IOException e) {
            throw new CollectorExceptionWrapper(e);
        }
    }

    @Override
    protected T mapToDataset(Map<String, Object> map) throws CollectorExceptionWrapper {
        try {
            return JsonUtils.parseJson(JsonUtils.toJson(map), getGenericClass());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

}
