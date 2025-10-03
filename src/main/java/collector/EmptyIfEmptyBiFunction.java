package collector;

import berlin.yuna.typemap.model.LinkedTypeMap;
import lombok.AllArgsConstructor;

import java.util.Optional;
import java.util.function.BiFunction;

@AllArgsConstructor
public class EmptyIfEmptyBiFunction implements BiFunction<Optional<String>, LinkedTypeMap, Optional<String>> {

    private final BiFunction<Optional<String>, LinkedTypeMap, Optional<String>> function;

    @Override
    public Optional<String> apply(Optional<String> s, LinkedTypeMap m) {
        if (s.orElse("").isEmpty()) return Optional.empty();
        return function.apply(s, m);
    }

}
