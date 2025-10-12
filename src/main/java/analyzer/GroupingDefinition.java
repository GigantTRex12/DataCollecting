package analyzer;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public record GroupingDefinition<T>(
        Function<T, ?> function,
        boolean forced,
        boolean filter
) {
    public GroupingDefinition {
        requireNonNull(function);
    }

    public GroupingDefinition(Function<T, ?> function) {
        this(function, false, false);
    }
}