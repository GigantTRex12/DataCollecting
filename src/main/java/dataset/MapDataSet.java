package dataset;

import java.util.Map;

/**
 * A simple implementation of {@link BaseDataSet} where all values are stored in a key-value Map where the keys are Strings.
 */
public class MapDataSet extends BaseDataSet {

    protected final Map<String, Object> map;

    public MapDataSet(Map<String, Object> map) {
        this.map = map;
    }

    public MapDataSet(Metadata metadata, Map<String, Object> map) {
        super(metadata);
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
