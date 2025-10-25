package collector.functions;

import exceptions.InvalidInputFormatException;

import java.util.Map;

@FunctionalInterface
public interface NormalizerBiConsumer {

    void accept(String s, Map<String, Object> m) throws InvalidInputFormatException;

}
