package collector;

import java.util.Map;
import java.util.Optional;
import exceptions.InvalidInputFormatException;

public class NullIfEmptyNormalizer implements NormalizerBiFunction {

    private final NormalizerBiFunction function;

    public NullIfEmptyNormalizer(NormalizerBiFunction function) {
        this.function = function;
    }

    @Override
    public Object apply(Optional<String> s, Map<String, Object> m) throws InvalidInputFormatException {
        if ("".equals(s.orElse(""))) return null;
        return function.apply(s, m);
    }

}
