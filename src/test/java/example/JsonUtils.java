package example;

import dataset.Metadata;

public class JsonUtils {

    public static String toJson(AbstractDataSet data) {
        String rep = "{\"" + toJson(data.getMetadata());
        if (data instanceof SomeDataSet someDataSet) {
            rep += ",\"name\":\"" + someDataSet.getName() + ",\"number\":" + someDataSet.getNumber() + ",\"someValue:\"" + someDataSet.getSomeValue();
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
