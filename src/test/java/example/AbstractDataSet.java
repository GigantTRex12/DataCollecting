package example;

import dataset.BaseDataSet;

public abstract class AbstractDataSet extends BaseDataSet {

    private final MetadataExample metadataexample;

    public AbstractDataSet() {
        metadataexample = null;
    }

    public AbstractDataSet(MetadataExample metadataexample) {
        this.metadataexample = metadataexample;
    }

    public MetadataExample getMetadataexample() {
        return metadataexample;
    }
}
