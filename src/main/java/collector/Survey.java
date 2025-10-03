package collector;

import berlin.yuna.typemap.model.LinkedTypeMap;
import dataset.Metadata;
import exceptions.InvalidInputFormatException;

import java.util.List;
import java.util.Optional;

import static Utils.InputUtils.input;
import static Utils.InputUtils.multilineInput;
import static java.lang.IO.println;
import static java.lang.System.lineSeparator;

/**
 * Utility class to execute a sequence of {@link Question}s in order, collecting
 * validated and normalized answers into a {@link LinkedTypeMap}.
 */
public class Survey {

    /**
     * Runs the survey for a given array of {@link Question}s and returns all
     * collected answers as a {@link LinkedTypeMap}.
     *
     * @param questions the survey questions to ask
     * @return a map of keys to validated, normalized user answers
     */
    public static LinkedTypeMap run(Metadata strategy, final Question... questions) {
        return run(List.of(questions), strategy);
    }

    /**
     * Runs the survey for a given list of {@link Question}s and returns all
     * collected answers as a {@link LinkedTypeMap}.
     *
     * @param questions the survey questions to ask
     * @return a map of keys to validated, normalized user answers
     */
    public static LinkedTypeMap run(final List<Question> questions, Metadata metadata) {
        final LinkedTypeMap answers = new LinkedTypeMap();
        if (metadata != null) {
            answers.put(metadata.getClass().getSimpleName().toLowerCase(), metadata);
        }
        for (final Question question : questions) {
            if (!question.condition().test(answers)) continue;

            while (true) {
                final String raw = question.multiline() ? multilineInput(question.prompt()) : input(question.prompt());
                final Optional<String> input = Optional.of(raw);

                if (!validateAndPrintError(input, question, answers)) continue;

                try {
                    final Object normalized = question.normalizer().apply(input, answers);
                    putInMap(answers, question.key(), (normalized instanceof Optional<?> t) ? t.orElse(null) : normalized);
                } catch (InvalidInputFormatException e) {
                    println("Invalid input: " + e.getMessage());
                    continue;
                }
                break;
            }
        }
        return answers;
    }

    private static boolean validateAndPrintError(Optional<String> input, Question question, LinkedTypeMap answers) {
        Optional<String> error = Optional.empty();
        if (question.multiline() && !input.orElse("").isEmpty()) {
            for (String s : input.orElse("").split(lineSeparator())) {
                error = question.validator().apply(Optional.of(s), answers);
                if (error.isPresent()) break;
            }
        } else error = question.validator().apply(input, answers);
        if (error.isPresent()) {
            println("Invalid input: " + error.get());
            return false;
        }
        return true;
    }

    private static void putInMap(LinkedTypeMap answers, String key, Object value) {
        if (key.contains("&")) {
            if (!(value instanceof List<?> values))
                throw new IllegalStateException("For multiple fields value has to be a List");
            String[] keys = key.split("&");
            if (values.size() != keys.length)
                throw new IllegalStateException("Different amount of keys and values.");
            for (int i = 0; i < keys.length; i++) answers.put(keys[i], values.get(i));
        } else answers.put(key, value);
    }

    private Survey() {
        // Utility class, no instances allowed
    }

}