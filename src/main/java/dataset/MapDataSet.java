package dataset;

import java.util.Map;

public class MapDataSet extends BaseDataSet {

    protected final Map<String, Object> map;

    public MapDataSet(Map<String, Object> map) {
        this.map = map;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
