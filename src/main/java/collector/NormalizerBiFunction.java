package collector;

import exceptions.InvalidInputFormatException;
import berlin.yuna.typemap.model.LinkedTypeMap;
import berlin.yuna.typemap.model.Type;

@FunctionalInterface
public interface NormalizerBiFunction {

    Object apply(Type<String> s, LinkedTypeMap m) throws InvalidInputFormatException;

}
