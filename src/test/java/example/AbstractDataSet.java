package example;

import dataset.BaseDataSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor(force = true)
public abstract class AbstractDataSet extends BaseDataSet {

    private final MetadataExample metadataexample;

}
