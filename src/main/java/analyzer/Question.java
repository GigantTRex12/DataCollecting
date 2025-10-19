package analyzer;

import dataset.BaseDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public record Question<T extends BaseDataSet>(
        String name,
        List<GroupingDefinition<T>> groupings,
        Consumer<List<T>> evaluator,
        Predicate<T> conditionAll
) {
    public Question {
        requireNonNull(name);
        groupings = groupings != null ? groupings : new ArrayList<>();
        requireNonNull(evaluator);
        conditionAll = conditionAll != null ? conditionAll : _ -> true;
    }

    @Override
    public String toString() {
        return name;
    }

    public static <T extends BaseDataSet> Builder<T> ask(String name, Class<T> c) {
        return new Builder<>(name);
    }

    public static final class Builder<T extends BaseDataSet> {
        private final String name;
        private Consumer<List<T>> evaluator;
        private List<GroupingDefinition<T>> groupings;
        private Predicate<T> conditionAll;

        public Builder(String name) {
            this.name = name;
            groupings = new ArrayList<>();
        }

        public Builder<T> evaluator(Consumer<List<T>> evaluator) {
            this.evaluator = evaluator;
            return this;
        }

        public Builder<T> evaluator(Function<T, ?> mapper, Consumer<List<?>> evaluator) {
            this.evaluator = l -> evaluator.accept(l.stream().map(mapper).toList());
            return this;
        }

        public Builder<T> groupings(List<GroupingDefinition<T>> groupings) {
            this.groupings.addAll(groupings);
            return this;
        }

        public Builder<T> groupings(GroupingDefinition<T>... groupings) {
            this.groupings.addAll(Arrays.asList(groupings));
            return this;
        }

        // TODO: generate name instead of "Unknown" since names are not allowed to repeat
        public Builder<T> groupingFunctions(List<Function<T, ?>> functions) {
            this.groupings.addAll(functions.stream().map(f -> new GroupingDefinition<>("Unknown", f)).toList());
            return this;
        }

        public Builder<T> groupingFunctions(Function<T, ?>... functions) {
            this.groupings.addAll(Arrays.stream(functions).map(f -> new GroupingDefinition<>("Unknown", f)).toList());
            return this;
        }

        public Builder<T> forcedGrouping(List<Function<T, ?>> functions) {
            this.groupings.addAll(functions.stream().map(f -> new GroupingDefinition<>("Unknown", f, true)).toList());
            return this;
        }

        public Builder<T> forcedGrouping(Function<T, ?>... functions) {
            this.groupings.addAll(Arrays.stream(functions).map(f -> new GroupingDefinition<>("Unknown", f, true)).toList());
            return this;
        }

        public Builder<T> conditionAll(Predicate<T> conditionAll) {
            this.conditionAll = conditionAll;
            return this;
        }

        public Question<T> build() {
            return new Question<>(
                    name,
                    groupings,
                    evaluator,
                    conditionAll
            );
        }
    }

}
