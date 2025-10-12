package collector;

import dataset.MapDataSet;
import exceptions.CollectorExceptionWrapper;

import java.util.Map;

public abstract class MapDataCollector extends BaseDataCollector<MapDataSet> {

    public MapDataCollector() {
        super();
    }

    @Override
    protected Class<? extends MapDataSet> getGenericClass() throws CollectorExceptionWrapper {
        return MapDataSet.class;
    }

    @Override
    protected MapDataSet mapToDataset(Map<String, Object> map) throws CollectorExceptionWrapper {
        return new MapDataSet(map);
    }

}
