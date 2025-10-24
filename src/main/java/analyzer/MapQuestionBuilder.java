package analyzer;

import dataset.MapDataSet;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Builder for creating {@link Question} instances specifically for {@link MapDataSet}.
 */
public class MapQuestionBuilder extends Question.Builder<MapDataSet> {

    public MapQuestionBuilder(String name) {
        super(name);
    }

    /**
     * Allows easy creation of an evaluator out of two parts.
     *
     * @param key       String representing the key of which the values are mapped to before passing it to the evaluator.
     * @param evaluator Function that takes the List of Objects each DataSet has been mapped to.
     */
    public MapQuestionBuilder evaluator(String key, Consumer<List<?>> evaluator) {
        evaluator(l -> evaluator.accept(l.stream().map(d -> d.getMap().get(key)).toList()));
        return this;
    }

    /**
     * Creates a List of new {@link GroupingDefinition} where the name is the key and the function is getting the value from the given key.
     */
    public MapQuestionBuilder groupBy(List<String> keys) {
        this.groupings.addAll(keys.stream().map(s -> new GroupingDefinition<MapDataSet>(s, d -> d.getMap().get(s))).toList());
        return this;
    }

    /**
     * Creates a List of new {@link GroupingDefinition} where the name is the key and the function is getting the value from the given key.
     */
    public MapQuestionBuilder groupBy(String... keys) {
        this.groupings.addAll(Arrays.stream(keys).map(s -> new GroupingDefinition<MapDataSet>(s, d -> d.getMap().get(s))).toList());
        return this;
    }

}
