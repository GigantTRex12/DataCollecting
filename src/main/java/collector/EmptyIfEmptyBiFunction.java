package collector;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

class EmptyIfEmptyBiFunction implements BiFunction<String, Map<String, Object>, Optional<String>> {

    private final BiFunction<String, Map<String, Object>, Optional<String>> function;

    EmptyIfEmptyBiFunction(BiFunction<String, Map<String, Object>, Optional<String>> function) {
        this.function = function;
    }

    @Override
    public Optional<String> apply(String s, Map<String, Object> m) {
        if (s.isEmpty()) return Optional.empty();
        return function.apply(s, m);
    }

}
