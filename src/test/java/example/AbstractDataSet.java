package example;

import dataset.BaseDataSet;

public abstract class AbstractDataSet extends BaseDataSet {

    // forces the metadata to be the MetadataExample
    public AbstractDataSet(MetadataExample metadataexample) {
        super(metadataexample);
    }

}
