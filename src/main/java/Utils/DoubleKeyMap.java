package Utils;

import exceptions.KeyAlreadyExistsException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A Map that allows to have a second key for each entry. The second key is of the same type as the regular key
 * and the key pairs are linked together. The Values may be accessed from either key. To add a new entry with
 * one key use the normal {@link #put(Object, Object)}, to put a second key for an entry use
 * {@link #put(Object, Object, Object)}. Neither keys nor values for either method may be null or
 * a {@link NullPointerException} will be thrown.
 * Removing or replacing either key of a pair will remove or replace the other as well.
 * Attempting to add a new entry with an already existing key will throw a {@link KeyAlreadyExistsException}.
 * The {@link #forEach(BiConsumer)}, {@link #size()} and {@link #values()} methods behave as if the second key
 * didn't exist and therefor avoid duplicates. This does not apply to {@link #keySet()}.
 * @param <K> The type of keys maintained by this map
 * @param <V> The type of mapped values
 */
public class DoubleKeyMap<K, V> extends HashMap<K, V> {

    private final Map<K, K> keys_short_to_long;
    private final Map<K, K> keys_long_to_short; // short keys are optional, so values here can be null

    public DoubleKeyMap() {
        keys_short_to_long = new HashMap<>();
        keys_long_to_short = new HashMap<>();
    }

    @Override
    public void clear() {
        super.clear();
        keys_long_to_short.clear();
        keys_short_to_long.clear();
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        keys_long_to_short.forEach((key, _) -> action.accept(key, this.get(key)));
    }

    @Override
    public V put(K key, V value) {
        requireNonNull(key);
        requireNonNull(value);
        if (keySet().contains(key)) {
            throw new KeyAlreadyExistsException("Cannot add an already existing key");
        }

        super.put(key, value);
        keys_long_to_short.put(key, null);
        return value;
    }

    public V put(K key_long, K key_short, V value) {
        requireNonNull(key_long);
        requireNonNull(key_short);
        requireNonNull(value);
        if (keySet().contains(key_long) || keySet().contains(key_short)) {
            throw new KeyAlreadyExistsException("Cannot add an already existing key");
        }
        else if (key_long == key_short) {
            throw new KeyAlreadyExistsException("Cannot use the same long and short key");
        }

        super.put(key_long, value);
        super.put(key_short, value);
        keys_long_to_short.put(key_long, key_short);
        keys_short_to_long.put(key_short, key_long);
        return value;
    }

    @Override
    public V remove(Object key) {
        V v = super.remove(key);
        K k;
        k = keys_long_to_short.remove(key);
        if (k != null) super.remove(k);
        k = keys_short_to_long.remove(key);
        if (k != null) super.remove(k);
        return v;
    }

    @Override
    public boolean remove(Object key, Object value) {
        boolean removed = super.remove(key, value);
        if (removed) {
            K k;
            k = keys_long_to_short.remove(key);
            if (k != null) super.remove(k);
            k = keys_short_to_long.remove(key);
            if (k != null) super.remove(k);
        }
        return removed;
    }

    @Override
    public V replace(K key, V value) {
        V v = super.replace(key, value);
        if (v != null) {
            K k;
            k = keys_long_to_short.get(key);
            if (k != null) super.replace(k, value);
            k = keys_short_to_long.get(key);
            if (k != null) super.replace(k, value);
        }
        return v;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        boolean replaced = super.replace(key, oldValue, newValue);
        if (replaced) {
            K k;
            k = keys_long_to_short.get(key);
            if (k != null) super.replace(k, newValue);
            k = keys_short_to_long.get(key);
            if (k != null) super.replace(k, newValue);
        }
        return replaced;
    }

    @Override
    public int size() {
        return keys_long_to_short.size();
    }

    @Override
    public Collection<V> values() {
        List<V> values = new ArrayList<>();
        keys_long_to_short.keySet().forEach(k -> values.add(get(k)));
        return values;
    }

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: merge()");
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: putAll()");
    }

    @Override
    public V putIfAbsent(K key, V value) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: putIfAbsent()");
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: replaceAll()");
    }

    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: compute()");
    }


    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: computeIfAbsent()");
    }

    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: computeIfPresent()");
    }

    public String keyReps() {
        return keys_long_to_short.keySet().stream()
                .map(k -> keys_long_to_short.get(k) == null ? k.toString() : k.toString() + " (" + keys_long_to_short.get(k).toString() + ")")
                .collect(Collectors.joining("; "));
    }

}