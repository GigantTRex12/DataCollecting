package collector;

import dataset.MapDataSet;

import java.util.Map;

public abstract class MapDataCollector extends BaseDataCollector<MapDataSet> {

    public MapDataCollector() {
        super();
    }

    @Override
    protected MapDataSet mapToDataset(Map<String, Object> map) {
        return new MapDataSet(currMetadata, map);
    }

}
