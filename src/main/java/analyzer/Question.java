package analyzer;

import dataset.BaseDataSet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

/**
 * Represents a single survey Question definition for analyzing data.
 * Encapsulates all logic for asking and processing user input without relying on reflection.
 *
 * @param name         String representation for this Question.
 * @param groupings    Functions allowing to group the DataSets before evaluating, see {@link GroupingDefinition}.
 * @param evaluator    Consumer that takes a List of DataSets, does logical analyzation and outputs it in some way.
 * @param conditionAll Filter condition, any DataSet that doesn't fill the condition is ignored
 * @param <T>          Type of {@link BaseDataSet} that can be analyzed with this Question.
 */
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

    /**
     * Builder for creating {@link Question} instances.
     *
     * @param <T> Type parametetr of the Question to create.
     */
    public static class Builder<T extends BaseDataSet> {
        private final String name;
        protected Consumer<List<T>> evaluator;
        protected List<GroupingDefinition<T>> groupings;
        protected Predicate<T> conditionAll;
        private int count;

        protected Builder(String name) {
            this.name = name;
            groupings = new ArrayList<>();
            count = 0;
        }

        public Builder<T> evaluator(Consumer<List<T>> evaluator) {
            this.evaluator = evaluator;
            return this;
        }

        /**
         * Allows easy creation of an evaluator out of two parts.
         *
         * @param mapper    Function that maps each DataSet to some Object.
         * @param evaluator Function that takes the List of Objects each DataSet has been mapped to.
         */
        public <R> Builder<T> evaluator(Function<T, R> mapper, Consumer<List<R>> evaluator) {
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

        /**
         * Creates a {@link GroupingDefinition} from just the functions and generates the name (forced = false)
         */
        public Builder<T> groupingFunctions(List<Function<T, ?>> functions) {
            this.groupings.addAll(functions.stream().map(f -> {
                count++;
                return new GroupingDefinition<>("Grouping " + count, f);
            }).toList());
            return this;
        }

        /**
         * Creates a {@link GroupingDefinition} from just the functions and generates the name (forced = false)
         */
        public Builder<T> groupingFunctions(Function<T, ?>... functions) {
            this.groupings.addAll(Arrays.stream(functions).map(f -> {
                count++;
                return new GroupingDefinition<>("Grouping " + count, f);
            }).toList());
            return this;
        }

        /**
         * Creates a {@link GroupingDefinition} from just the functions and generates the name (forced = true)
         */
        public Builder<T> forcedGrouping(List<Function<T, ?>> functions) {
            this.groupings.addAll(functions.stream().map(f -> {
                count++;
                return new GroupingDefinition<>("Grouping " + count, f, true);
            }).toList());
            return this;
        }

        /**
         * Creates a {@link GroupingDefinition} from just the functions and generates the name (forced = true)
         */
        public Builder<T> forcedGrouping(Function<T, ?>... functions) {
            this.groupings.addAll(Arrays.stream(functions).map(f -> {
                count++;
                return new GroupingDefinition<>("Grouping " + count, f, true);
            }).toList());
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
