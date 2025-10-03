package collector;

import java.util.Map;
import java.util.Optional;
import exceptions.InvalidInputFormatException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NullIfEmptyNormalizer implements NormalizerBiFunction {

    private final NormalizerBiFunction function;

    @Override
    public Object apply(Optional<String> s, Map<String, Object> m) throws InvalidInputFormatException {
        if ("".equals(s.orElse(""))) return null;
        return function.apply(s, m);
    }

}
