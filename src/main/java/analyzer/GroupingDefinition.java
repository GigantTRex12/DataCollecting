package analyzer;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public record GroupingDefinition<T>(
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