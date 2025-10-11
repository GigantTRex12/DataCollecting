package example;

public class SomeDataSet extends AbstractDataSet {

    private final String name;
    private final int number;
    private final String someValue;

    public SomeDataSet() {
        name = null;
        number = 0;
        someValue = null;
    }

    public SomeDataSet(MetadataExample metadataexample, String name, int number, String someValue) {
        super(metadataexample);
        this.name = name;
        this.number = number;
        this.someValue = someValue;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getSomeValue() {
        return someValue;
    }
}
