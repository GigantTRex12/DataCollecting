package exceptions;

import java.io.IOException;

/**
 * Exception allowed to be thrown by normalizers in {@link collector.Question}.
 */
public class InvalidInputFormatException extends IOException {
    public InvalidInputFormatException(String errorMessage) {
        super(errorMessage);
    }
}
