package example;

import dataset.Metadata;
import example.example_one.SomeDataSet;
import example.example_two.OtherDataSet;

public class JsonUtils {

    public static String toJson(AbstractDataSet data) {
        String rep = "{\"" + toJson(data.getMetadata());
        if (data instanceof SomeDataSet someDataSet) {
            rep += ",\"name\":\"" + someDataSet.getName() + ",\"number\":" + someDataSet.getNumber() + ",\"someValue:\"" + someDataSet.getSomeValue();
        } else if (data instanceof OtherDataSet otherDataSet) {
            rep += ",\"name\":\"" + otherDataSet.getName() + ",\"value1\":\"" + otherDataSet.getValue1() + ",\"value2\":\"" + otherDataSet.getValue2();
        }
        return rep + "}";
    }

    public static String toJson(Metadata metadata) {
        String rep = "{";
        if (metadata instanceof MetadataExample metadataExample) {
            rep += "\"description\":" + metadataExample.getDescription();
        }
        return rep + "}";
    }

}
