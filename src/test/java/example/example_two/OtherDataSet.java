package example.example_two;

import example.AbstractDataSet;
import example.MetadataExample;

public class OtherDataSet extends AbstractDataSet {

    private final String name;
    private final String value1;
    private final String value2;

    public OtherDataSet(MetadataExample metadataexample, String name, String value1, String value2) {
        super(metadataexample);
        this.name = name;
        this.value1 = value1;
        this.value2 = value2;
    }

    public String getName() {
        return name;
    }

    public String getValue1() {
        return value1;
    }

    public String getValue2() {
        return value2;
    }
}
