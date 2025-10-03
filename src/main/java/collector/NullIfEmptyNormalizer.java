package collector;

import berlin.yuna.typemap.model.LinkedTypeMap;
import java.util.Optional;
import exceptions.InvalidInputFormatException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NullIfEmptyNormalizer implements NormalizerBiFunction {

    private final NormalizerBiFunction function;

    @Override
    public Object apply(Optional<String> s, LinkedTypeMap m) throws InvalidInputFormatException {
        if ("".equals(s.orElse(""))) return null;
        return function.apply(s, m);
    }

}
