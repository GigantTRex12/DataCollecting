package example;

import dataset.Metadata;

public class MetadataExample extends Metadata {

    private final String description;

    public MetadataExample() {
        description = null;
    }

    public MetadataExample(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
