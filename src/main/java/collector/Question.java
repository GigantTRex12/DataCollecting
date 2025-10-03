package collector;

import java.util.Map;
import java.util.Optional;
import exceptions.InvalidInputFormatException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static Utils.StringUtils.contains;
import static Utils.StringUtils.join;
import static java.lang.System.lineSeparator;
import static java.util.Objects.requireNonNull;

/**
 * Represents a single survey question definition including prompt, key, condition,
 * validator, and normalizer. Encapsulates all logic for asking and processing
 * user input without relying on reflection.
 */
public record Question(
        String key, // use "&" for multiple values in one question
        String prompt,
        Predicate<Map<String, Object>> condition,
        BiFunction<Optional<String>, Map<String, Object>, Optional<String>> validator, // on multilines this validates each line separately
        NormalizerBiFunction normalizer,
        boolean multiline,
        String fixedChoice
) {
    public Question {
        requireNonNull(key);
        requireNonNull(prompt);
        condition = condition != null ? condition : _ -> true;
        validator = validator != null ? validator : (_, _) -> Optional.empty();
        normalizer = normalizer != null ? normalizer : (answer, _) -> answer.orElse("");
    }

    public static Builder ask(final String key, final String prompt) {
        return new Builder(key, prompt);
    }

    /**
     * Builder for creating {@link Question} instances with optional condition,
     * validation and normalization logic.
     */
    public static final class Builder {
        private final String key;
        private final String prompt;
        private Predicate<Map<String, Object>> condition;
        private BiFunction<Optional<String>, Map<String, Object>, Optional<String>> validator;
        private NormalizerBiFunction normalizer;
        private boolean multiline = false;
        private String conditionPrompt;

        private Builder(final String key, final String prompt) {
            this.key = key;
            this.prompt = prompt;
        }

        public Builder when(final Predicate<Map<String, Object>> condition) {
            this.condition = condition;
            return this;
        }

        public Builder validate(final BiFunction<Optional<String>, Map<String, Object>, Optional<String>> validator) {
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
            this.normalizer = (answer, _) -> parser.apply(answer.orElse("").strip());
            return this;
        }

        public Builder regex(final String regex) {
            validator = (t, _) -> {
                if (Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(t.orElse("")).find()) return Optional.empty();
                else return Optional.of("Input needs to match pattern " + regex);
            };
            conditionPrompt = "Format: " + regex;
            return this;
        }

        public Builder options(String[] options) {
            validator = (t, _) -> {
                if (contains(options, t.orElse(""))) return Optional.empty();
                else return Optional.of("Input needs to be one of the given options");
            };
            conditionPrompt = "Options: " + join(options, ", ");
            return this;
        }

        public Builder options(String[] options1, String[] options2) {
            validator = (t, m) -> {
                if (contains(options1, t.orElse("")) || contains(options2, t.orElse(""))) return Optional.empty();
                else return Optional.of("Input needs to be one of the given options");
            };
            conditionPrompt = "Options: " + join(options1, options2, ", ");
            return this;
        }

        // only use after setting a validator/normalizer
        public Builder emptyToNull() {
            validator = validator == null ? null : new EmptyIfEmptyBiFunction(validator);
            if (normalizer == null) normalizer = (answer, _) -> answer.orElse("").strip();
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
                    list.add(parser.apply(answer.orElse("").strip()));
                return list;
            };
            return this;
        }

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
                    multiline,
                    null
            );
        }
    }
}
