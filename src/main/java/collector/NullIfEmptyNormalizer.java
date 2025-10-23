package collector;

import exceptions.InvalidInputFormatException;

import java.util.Map;

class NullIfEmptyNormalizer implements NormalizerBiFunction {

    private final NormalizerBiFunction function;

    NullIfEmptyNormalizer(NormalizerBiFunction function) {
        this.function = function;
    }

    @Override
    public Object apply(String s, Map<String, Object> m) throws InvalidInputFormatException {
        if ("".equals(s)) return null;
        return function.apply(s, m);
    }

}
