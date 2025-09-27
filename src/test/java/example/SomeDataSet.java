package example;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class SomeDataSet extends AbstractDataSet {

    private final String name;
    private final int number;
    private final String someValue;

    public SomeDataSet(MetadataExample metadataexample, String name, int number, String someValue) {
        super(metadataexample);
        this.name = name;
        this.number = number;
        this.someValue = someValue;
    }

}
