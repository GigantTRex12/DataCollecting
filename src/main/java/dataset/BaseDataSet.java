package dataset;

/**
 * Any Base class for structure of collected Data.
 * Has optional field for {@link Metadata}.
 */
public abstract class BaseDataSet {

    protected final Metadata metadata;

    public BaseDataSet() {
        this.metadata = null;
    }

    public BaseDataSet(Metadata metadata) {
        this.metadata = metadata;
    }

    public Metadata getMetadata() {
        return metadata;
    }

}
