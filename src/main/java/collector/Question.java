package collector;

import Utils.StringUtils;
import exceptions.InvalidInputFormatException;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;

/**
 * Represents a single survey Question definition for collecting data.
 * Encapsulates all logic for asking and processing user input without relying on reflection.
 *
 * @param key        The name of the property that is resulting of this Question and the key in the resulting key-value map.
 * @param prompt     The prompt printed for this question.
 * @param condition  A condition for the key-value map, allowing to skip this Question depending on previous Questions.
 * @param validator  Validates user input. Return an empty Optional for valid inputs and Optional containing an error message otherwise.
 * @param normalizer Function parsing the user input into an Object to be put in the key-value map.
 * @param multiline  Wether to allow multiple lines of input. If true lets user make inputs until an empty line is input.
 */
public record Question(
        String key, // use "&" for multiple values in one question // TODO: probably move this logic into normalizer
        String prompt,
        Predicate<Map<String, Object>> condition,
        BiFunction<String, Map<String, Object>, Optional<String>> validator,
        // on multilines this validates each line separately
        NormalizerBiFunction normalizer,
        boolean multiline
) {
    public Question {
        requireNonNull(key);
        requireNonNull(prompt);
        condition = condition != null ? condition : _ -> true;
        validator = validator != null ? validator : (_, _) -> Optional.empty();
        normalizer = normalizer != null ? normalizer : (answer, _) -> answer;
    }

    public static Builder ask(final String key, final String prompt) {
        return new Builder(key, prompt);
    }

    /**
     * Builder for creating {@link Question} instances.
     */
    public static class Builder {
        private final String key;
        private final String prompt;
        protected Predicate<Map<String, Object>> condition;
        protected BiFunction<String, Map<String, Object>, Optional<String>> validator;
        protected NormalizerBiFunction normalizer;
        protected boolean multiline = false;
        protected String conditionPrompt;

        protected Builder(final String key, final String prompt) {
            this.key = key;
            this.prompt = prompt;
        }

        public Builder when(final Predicate<Map<String, Object>> condition) {
            this.condition = condition;
            return this;
        }

        public Builder validate(final BiFunction<String, Map<String, Object>, Optional<String>> validator) {
            this.validator = validator;
            this.conditionPrompt = null;
            return this;
        }

        public Builder normalize(final NormalizerBiFunction normalizer) {
            this.normalizer = normalizer;
            return this;
        }

        public Builder multiline() {
            multiline = true;
            return this;
        }

        // easier ways to create validator/normalizer
        public Builder normalize(final ThrowingFunction<String, Object, InvalidInputFormatException> parser) {
            this.normalizer = (answer, _) -> parser.apply(answer.strip());
            return this;
        }

        /**
         * Makes the resulting Question require the user input to match the given regex and adjusts the prompt.
         */
        public Builder regex(final String regex) {
            validator = (s, _) -> {
                if (Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(s).find()) return Optional.empty();
                else return Optional.of("Input needs to match pattern " + regex);
            };
            conditionPrompt = "Format: " + regex;
            return this;
        }

        /**
         * Forces the user input to be one of the given options (case-insensitive) and adjusts the prompt.
         */
        public Builder options(String[] options) {
            validator = (s, _) -> {
                if (Arrays.stream(options).anyMatch(a -> a.equalsIgnoreCase(s))) return Optional.empty();
                else return Optional.of("Input needs to be one of the given options");
            };
            conditionPrompt = "Options: " + String.join(", ", options);
            return this;
        }

        /**
         * Forces the user input to be one of the given options from either array (case-insensitive) and adjusts the prompt.
         */
        public Builder options(String[] options1, String[] options2) {
            validator = (s, _) -> {
                if (Arrays.stream(options1).anyMatch(a -> a.equalsIgnoreCase(s))
                        || Arrays.stream(options2).anyMatch(a -> a.equalsIgnoreCase(s))) return Optional.empty();
                else return Optional.of("Input needs to be one of the given options");
            };
            conditionPrompt = "Options: " + StringUtils.join(options1, options2, ", ");
            return this;
        }

        /**
         * Always turn an empty String to null and allow empty user input.
         * Set validator and normalizer before this.
         */
        public Builder emptyToNull() {
            validator = validator == null ? null : new EmptyIfEmptyBiFunction(validator);
            if (normalizer == null) normalizer = (answer, _) -> answer.strip();
            normalizer = new NullIfEmptyNormalizer(normalizer);
            return this;
        }

        // multiple values in one question need multiple normalizers -> instead return list of results
        public Builder normalizers(final NormalizerBiFunction... normalizers) {
            this.normalizer = (answer, answers) -> {
                List<Object> list = new ArrayList<>();
                for (NormalizerBiFunction n : normalizers) list.add(n.apply(answer, answers));
                return list;
            };
            return this;
        }

        public Builder normalizers(final ThrowingFunction<String, Object, InvalidInputFormatException>... parsers) {
            this.normalizer = (answer, _) -> {
                List<Object> list = new ArrayList<>();
                for (ThrowingFunction<String, Object, InvalidInputFormatException> parser : parsers)
                    list.add(parser.apply(answer.strip()));
                return list;
            };
            return this;
        }

        /**
         * Don't validate user input. Useful to generate prompt for regex or options without enforcing them.
         */
        public Builder dontValidate() {
            this.validator = null;
            return this;
        }

        public Question build() {
            return new Question(
                    key,
                    conditionPrompt == null ? prompt : prompt + lineSeparator() + conditionPrompt,
                    condition,
                    validator,
                    normalizer,
                    multiline
            );
        }
    }
}
