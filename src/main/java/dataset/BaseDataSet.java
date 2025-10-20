package dataset;

public abstract class BaseDataSet {

    protected final Metadata metadata;

    public BaseDataSet(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return metadata;
    }

}
