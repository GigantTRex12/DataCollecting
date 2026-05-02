package collector.functions;

import exceptions.InvalidInputFormatException;

import java.util.Map;

@FunctionalInterface
/**
 * Functional Interface representing a Normalizer as in {@link collector.Question}.
 * Takes the String input and a Map and may throw an {@link InvalidInputFormatException}
 */
public interface NormalizerBiConsumer {

    void accept(String s, Map<String, Object> m) throws InvalidInputFormatException;

}
