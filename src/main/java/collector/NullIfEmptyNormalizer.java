package collector;

import berlin.yuna.typemap.model.LinkedTypeMap;
import berlin.yuna.typemap.model.Type;
import exceptions.InvalidInputFormatException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NullIfEmptyNormalizer implements NormalizerBiFunction {

    private final NormalizerBiFunction function;

    @Override
    public Object apply(Type<String> s, LinkedTypeMap m) throws InvalidInputFormatException {
        if ("".equals(s.orElse(""))) return null;
        return function.apply(s, m);
    }

}
