package collector;

import exceptions.InvalidInputFormatException;

import java.util.Map;
import java.util.Optional;

@FunctionalInterface
public interface NormalizerBiFunction {

    Object apply(Optional<String> s, Map<String, Object> m) throws InvalidInputFormatException;

}
