package collector;

import Utils.StringUtils;
import collector.functions.EmptyIfEmptyBiFunction;
import collector.functions.NormalizerBiConsumer;
import collector.functions.ThrowingFunction;
import exceptions.InvalidInputFormatException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;

/**
 * Represents a single survey Question definition for collecting data.
 * Encapsulates all logic for asking and processing user input without relying on reflection.
 *
 * @param key        The name of the property that is resulting of this Question and the key in the resulting key-value map if no normalizer is given.
 * @param prompt     The prompt printed for this question.
 * @param condition  A condition for the key-value map, allowing to skip this Question depending on previous Questions.
 * @param validator  Validates user input. Return an empty Optional for valid inputs and Optional containing an error message otherwise.
 * @param normalizer Consumer parsing the user input into an Object and puts it into the key-value map.
 * @param multiline  Wether to allow multiple lines of input. If true lets user make inputs until an empty line is input.
 */
public record Question(
        String key,
        String prompt,
        Predicate<Map<String, Object>> condition,
        BiFunction<String, Map<String, Object>, Optional<String>> validator,
        // on multilines this validates each line separately
        NormalizerBiConsumer normalizer,
        boolean multiline,
        boolean allowPreset
) {
    public Question {
        requireNonNull(key);
        requireNonNull(prompt);
        condition = condition != null ? condition : _ -> true;
        validator = validator != null ? validator : (_, _) -> Optional.empty();
        normalizer = normalizer != null ? normalizer : (answer, map) -> map.put(key, answer);
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
        protected NormalizerBiConsumer normalizer;
        protected boolean multiline = false;
        protected String conditionPrompt;
        private boolean allowPreset = true;

        protected Builder(final String key, final String prompt) {
            this.key = key;
            this.prompt = prompt;
        }

        private Builder conditionPrompt(final String prompt) {
            this.conditionPrompt = prompt;
            return this;
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

        public Builder normalize(final NormalizerBiConsumer normalizer) {
            this.normalizer = normalizer;
            return this;
        }

        public Builder multiline() {
            multiline = true;
            return this;
        }

        public Builder dontAllowPreset() {
            allowPreset = false;
            return this;
        }

        // easier ways to create validator/normalizer

        /**
         * Transforms the parser into a {@link NormalizerBiConsumer}.
         * <br>
         * For normalizer with multiple keys use {@link Builder#normalizers(Map)} instead.
         */
        public Builder normalize(final ThrowingFunction<String, Object, InvalidInputFormatException> parser) {
            this.normalizer = (answer, map) -> map.put(key, parser.apply(answer.strip()));
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
         * Forces the user input to be one of the given options from either array (case-insensitive) and adjusts the
         * prompt.
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
         * <br>
         * Only works for single keys, for multiple keys use {@link Builder#normalizers(Map)} instead.
         */
        public Builder emptyToNull() {
            validator = validator == null ? null : new EmptyIfEmptyBiFunction(validator);
            normalizer = normalizer == null ?
                    ((s, m) -> m.put(key, s.isEmpty() ? null : s)) :
                    ((s, m) -> {
                        if (s.isEmpty()) m.put(key, null);
                        else normalizer.accept(s, m);
                    });
            return this;
        }

        /**
         * Merges multiple parsers into one {@link NormalizerBiConsumer}.
         *
         * @param parsers A map where each key is mapped to the function parsing the input into the value for that key.
         */
        public Builder normalizers(final Map<String, ThrowingFunction<String, Object, InvalidInputFormatException>> parsers) {
            normalizer = (answer, map) -> {
                for (Map.Entry<String, ThrowingFunction<String, Object, InvalidInputFormatException>> entry : parsers.entrySet()) {
                    map.put(entry.getKey(), entry.getValue().apply(answer));
                }
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

        // Set the validator and normalizer for specific (primitive) types

        /**
         * Forces the user input to be an integer and sets the normalizer to parse the input to an integer and adjusts
         * the prompt.
         */
        public Builder toInt() {
            return this
                    .regex("-?[1-9]\\d*|0")
                    .conditionPrompt("Input a whole number")
                    .normalize(s -> Integer.parseInt(s));
        }

        /**
         * Forces the user input to be a positive integer and sets the normalizer to parse the input to an integer and
         * adjusts the prompt.
         */
        public Builder toPositiveInt() {
            return this
                    .regex("[1-9]\\d*|0")
                    .conditionPrompt("Input a positive whole number")
                    .normalize(s -> Integer.parseInt(s));
        }

        /**
         * Forces the user input to be a float and sets the normalizer to parse the input to a float and adjusts the
         * prompt.
         */
        public Builder toFloat() {
            return this
                    .regex("-?[1-9]\\d*\\.?\\d*|0|-?0?\\.\\d+")
                    .conditionPrompt("Input a number")
                    .normalize(Float::parseFloat);
        }

        /**
         * Forces the user input to be a positive float and sets the normalizer to parse the input to a float and
         * adjusts the prompt.
         */
        public Builder toPositiveFloat() {
            return this
                    .regex("[1-9]\\d*\\.?\\d*|0|0?\\.\\d+")
                    .conditionPrompt("Input a number")
                    .normalize(Float::parseFloat);
        }

        /**
         * Forces the user input to be a single character and sets the normalizer to parse the input to a char and
         * adjusts the prompt.
         */
        public Builder toChar() {
            this.validator = (s, _) -> s.length() == 1 ? Optional.empty() : Optional.of("Input can only have one character");
            this.conditionPrompt = "Input a single character";
            return this.normalize(s -> s.charAt(0));
        }

        /**
         * Forces the user input to be "yes" or "no" (case-insensitive) and sets the normalizer to parse the input to a
         * boolean and adjusts the prompt.
         */
        public Builder toBool() {
            return this.toBool("Yes", "No");
        }

        /**
         * Sets the normalizer to return true only if the given String is entered (case-insensitive) and false
         * otherwise.
         * @param yes The String for which the parser will return true
         */
        public Builder toBool(final String yes) {
            return this.normalize(s -> s.equalsIgnoreCase(yes));
        }

        /**
         * Forces the user input to be one of the given Strings (case-insensitive) and sets the normalizer to parse the
         * input to a boolean.
         * @param yes The String for which the parser will return true
         * @param no The String for which the parser will return false
         */
        public Builder toBool(final String yes, final String no) {
            return this
                    .options(new String[]{yes, no})
                    .normalize(s -> s.equalsIgnoreCase(yes));
        }

        /**
         * Forces the user input to be one of the given Strings (case-insensitive) and sets the normalizer to parse the
         * input to a boolean and adjusts the prompt.
         * @param yes Array of Strings for which the parser will return true.
         * @param no Array of Strings for which the parser will return false.
         * @throws IllegalArgumentException If either of the input Arrays is empty.
         */
        public Builder toBool(final String[] yes, final String[] no) {
            if (yes.length < 1 || no.length < 1) throw new IllegalArgumentException("Arrays need to contain at least one element");
            return this
                    .options(yes, no)
                    .normalize(s -> Arrays.stream(yes).anyMatch(a -> a.equalsIgnoreCase(s)))
                    .conditionPrompt(String.join("|", yes) + " - " + String.join("|", no));
        }

        public Question build() {
            return new Question(
                    key,
                    conditionPrompt == null ? prompt : prompt + lineSeparator() + conditionPrompt,
                    condition,
                    validator,
                    normalizer,
                    multiline,
                    allowPreset
            );
        }
    }
}
