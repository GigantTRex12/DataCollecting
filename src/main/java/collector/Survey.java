package collector;

import exceptions.InvalidInputFormatException;

import java.util.*;

import static Utils.InputUtils.input;
import static Utils.InputUtils.multilineInput;
import static java.lang.IO.println;
import static java.lang.System.lineSeparator;

/**
 * Utility class to execute a sequence of {@link Question}s in order, collecting
 * validated and normalized answers into a {@link Map}.
 */
class Survey {

    private final List<Question> questions;
    private final Map<Question, String> presetAnswers;

    Survey(List<Question> questions) {
        this.questions = Collections.unmodifiableList(questions);
        presetAnswers = new HashMap<>();
    }

    /**
     * Runs the survey for a given list of {@link Question}s and returns all
     * collected answers as a {@link Map}.
     *
     * @return a map of keys to validated, normalized user answers
     */
    Map<String, Object> run() {
        final Map<String, Object> answers = new HashMap<>();
        for (final Question question : questions) {
            if (!question.condition().test(answers)) continue;

            String preset = presetAnswers.get(question);
            if (preset != null) {
                if (!validateAndPrintError(preset, question, answers)) presetAnswers.remove(question);
                else {
                    try {
                        question.normalizer().accept(preset, answers);
                        continue;
                    } catch (InvalidInputFormatException e) {
                        println("Invalid input: " + e.getMessage());
                        presetAnswers.remove(question);
                    }
                }
            }

            while (true) {
                final String raw = question.multiline() ? multilineInput(question.prompt()) : input(question.prompt());

                if (!validateAndPrintError(raw, question, answers)) continue;

                try {
                    question.normalizer().accept(raw, answers);
                } catch (InvalidInputFormatException e) {
                    println("Invalid input: " + e.getMessage());
                    continue;
                }
                break;
            }
        }
        return answers;
    }

    private boolean validateAndPrintError(String input, Question question, Map<String, Object> answers) {
        Optional<String> error = Optional.empty();
        if (question.multiline() && !input.isEmpty()) {
            for (String s : input.split(lineSeparator())) {
                error = question.validator().apply(s, answers);
                if (error.isPresent()) break;
            }
        } else error = question.validator().apply(input, answers);
        if (error.isPresent()) {
            println("Invalid input: " + error.get());
            return false;
        }
        return true;
    }

    void presetAnswers() {
        clearPresetAnswers();
        println("Set the fixed answers. Leave empty to skip. Enter \\ for empty String.");
        for (final Question question : questions) {
            final String raw = question.multiline() ? multilineInput(question.prompt()) : input(question.prompt());
            if (raw.equals("\\")) presetAnswers.put(question, "");
            else if (!raw.isEmpty()) presetAnswers.put(question, raw);
        }
    }

    void clearPresetAnswers() {
        presetAnswers.clear();
    }

}