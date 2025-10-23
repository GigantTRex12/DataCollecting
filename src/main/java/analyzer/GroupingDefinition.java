package analyzer;

import dataset.BaseDataSet;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * Wrapper for a Function used to group DataSets.
 * @param name String representation of the function.
 * @param function The function wrapped by this.
 * @param forced Wether this grouper should always be applied. If true the grouping Function will be applied silently.
 * @param <T> Type of {@link BaseDataSet} that can be grouped with this.
 */
public record GroupingDefinition<T extends BaseDataSet>(
        String name,
        Function<T, ?> function,
        boolean forced
) {

    public GroupingDefinition {
        requireNonNull(function);
    }

    public GroupingDefinition(String name, Function<T, ?> function) {
        this(name, function, false);
    }

    @Override
    public String toString() {
        return name;
    }
}