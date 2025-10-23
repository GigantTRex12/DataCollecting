package collector;

/**
 * Functional Interface that allows Throwing a specified checked Exception
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Throwable> {

    R apply(T t) throws E;

}
