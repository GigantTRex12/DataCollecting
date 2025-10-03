package collector;

import exceptions.InvalidInputFormatException;
import berlin.yuna.typemap.model.LinkedTypeMap;
import java.util.Optional;

@FunctionalInterface
public interface NormalizerBiFunction {

    Object apply(Optional<String> s, LinkedTypeMap m) throws InvalidInputFormatException;

}
