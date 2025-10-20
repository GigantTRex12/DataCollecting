package Utils;

import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 * HashMap with Objects (elements) as key and their frequency as value.
 * Negative values are allowed.
 * Values of 0 act as non-existent for all relevant purposes.
 * @param <T> Type of Objects to count
 */
public class Counter<T> extends HashMap<T, Integer> {

    /**
     * Creates an empty Counter
     */
    public Counter() {
        super();
    }

    /**
     * Creates a Counter with all given elements added
     */
    public Counter(T[] array) {
        super(array.length);
        for (T t : array) {
            this.add(t);
        }
    }

    /**
     * Creates a Counter with all given elements added
     */
    public Counter(Iterable<T> iterable) {
        super();
        iterable.forEach(this::add);
    }

    @Override
    public Integer get(Object key) {
        return getOrDefault(key, 0);
    }

    /**
     * Adds the elements to this Counter by incrementing its value
     */
    public void add(T t) {
        this.put(t, this.getOrDefault(t, 0) + 1);
    }

    /**
     * Adds all elements to this Counter
     */
    public void add(T[] array) {
        for (T t : array) {
            add(t);
        }
    }

    /**
     * Adds all elements to this Counter
     */
    public void add(Iterable<T> iterable) {
        iterable.forEach(this::add);
    }

    /**
     * Removes the element from this Counter once by decrementing its value
     */
    public void substract(T t) {
        this.put(t, this.getOrDefault(t, 0) - 1);
    }

    /**
     * Removes all elements from this Counter once each
     */
    public void substract(T[] array) {
        for (T t : array) {
            substract(t);
        }
    }

    /**
     * Removes all elements from this Counter once each
     */
    public void substract(Iterable<T> iterable) {
        iterable.forEach(this::substract);
    }

    @Override
    public boolean isEmpty() {
        for (T t : this.keySet()) {
            if (this.get(t) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sums up all values in this Counter
     */
    public int sum() {
        int sum = 0;
        for (T t : this.keySet()) {
            sum += this.get(t);
        }
        return sum;
    }

    @Override
    public void forEach(BiConsumer<? super T, ? super Integer> action) {
        for (T t : this.keySet()) {
            if (get(t) != 0) {action.accept(t, this.get(t));}
        }
    }

}
