package exceptions;

/**
 * Exception thrown by {@link Utils.ActionMap} when an already existing key is attempted to be put into the Map.
 */
public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException(String message) {
        super(message);
    }
}
