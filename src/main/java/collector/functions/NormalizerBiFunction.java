package collector.functions;

import exceptions.InvalidInputFormatException;

import java.util.Map;

@FunctionalInterface
public interface NormalizerBiFunction {

    Object apply(String s, Map<String, Object> m) throws InvalidInputFormatException;

}
