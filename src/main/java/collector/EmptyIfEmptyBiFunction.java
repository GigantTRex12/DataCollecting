package collector;

import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

public class EmptyIfEmptyBiFunction implements BiFunction<Optional<String>, Map<String, Object>, Optional<String>> {

    private final BiFunction<Optional<String>, Map<String, Object>, Optional<String>> function;

    public EmptyIfEmptyBiFunction(BiFunction<Optional<String>, Map<String, Object>, Optional<String>> function) {
        this.function = function;
    }

    @Override
    public Optional<String> apply(Optional<String> s, Map<String, Object> m) {
        if (s.orElse("").isEmpty()) return Optional.empty();
        return function.apply(s, m);
    }

}
