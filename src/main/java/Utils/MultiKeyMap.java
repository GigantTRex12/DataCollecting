package Utils;

import exceptions.KeyAlreadyExistsException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * A Map that allows to have multiple keys for each entry. The secondary keys are of the same type as the regular
 * key. The Values may be accessed from either key. To add a new entry with
 * one key use the normal {@link #put(Object, Object)}, to put a second key for an entry use
 * {@link #put(Object, Object, Object)} or for 2 or more keys {@link #put(List, Object)}. Neither keys
 * nor values for either method may be null or a {@link NullPointerException} will be thrown.
 * Removing or replacing either key of a pair will remove or replace the other as well.
 * Attempting to add a new entry with an already existing key will throw a {@link KeyAlreadyExistsException}.
 * The {@link #forEach(BiConsumer)}, {@link #size()} and {@link #values()} methods behave as if the second key
 * didn't exist and therefor avoid duplicates. This does not apply to {@link #keySet()}.
 *
 * @param <K> The type of keys maintained by this map
 * @param <V> The type of mapped values
 */
public class MultiKeyMap<K, V> implements Map<K, V> {

    private final Map<K, Integer> keys = new HashMap<>();
    private final Map<Integer, V> values = new HashMap<>();
    private final Map<Integer, String> stringReps = new LinkedHashMap<>();
    private final String sep;
    private Integer counter = 0;

    public MultiKeyMap() {
        sep = "; ";
    }

    public MultiKeyMap(String sep) {
        this.sep = sep;
    }

    @Override
    public int size() {
        return values.size();
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return values.containsValue(value);
    }

    @Override
    public V get(Object key) {
        Integer index = keys.get(key);
        if (index == null) {
            keys.remove(key);
            return null;
        }
        V value = values.get(index);
        if (value == null) keys.remove(key);
        return value;
    }

    @Override
    public V put(K key, V value) {
        requireNonNull(key);
        requireNonNull(value);
        if (containsKey(key)) {
            throw new KeyAlreadyExistsException("Cannot add an already existing key");
        }

        keys.put(key, counter);
        values.put(counter, value);
        stringReps.put(counter, key.toString());
        counter++;
        return value;
    }

    public V put(K key_long, K key_short, V value) {
        requireNonNull(key_long);
        requireNonNull(key_short);
        requireNonNull(value);
        if (containsKey(key_long) || containsKey(key_short)) {
            throw new KeyAlreadyExistsException("Cannot add an already existing key");
        } else if (key_long == key_short) {
            throw new KeyAlreadyExistsException("Cannot use the same long and short key");
        }

        keys.put(key_long, counter);
        keys.put(key_short, counter);
        values.put(counter, value);
        stringReps.put(counter, key_long + " (" + key_short + ")");
        counter++;
        return value;
    }

    public V put(List<K> keyList, V value) {
        requireNonNull(keyList);
        requireNonNull(value);
        if (keyList.isEmpty()) throw new KeyAlreadyExistsException("Need to add at least 1 key");
        for (K key : keyList) {
            requireNonNull(key);
            if (containsKey(key)) throw new KeyAlreadyExistsException("Cannot add an already existing key");
        }

        for (K key : keyList) {
            keys.put(key, counter);
        }
        values.put(counter, value);
        stringReps.put(counter, keyList.getFirst() + " ("
                + keyList.stream()
                .map(Object::toString)
                .distinct().filter(s -> s != keyList.getFirst().toString())
                .collect(Collectors.joining(sep))
                + ")");
        counter++;
        return value;
    }

    @Override
    public V remove(Object key) {
        Integer index = keys.get(key);
        if (index != null) {
            stringReps.remove(index);
            return values.remove(index);
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: putAll()");
    }

    @Override
    public void clear() {
        keys.clear();
        values.clear();
        stringReps.clear();
    }

    @Override
    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();
        for (Entry<K, Integer> entry : keys.entrySet()) {
            if (values.get(entry.getValue()) == null) keys.remove(entry.getKey());
            else keySet.add(entry.getKey());
        }
        return keySet;
    }

    @Override
    public Collection<V> values() {
        return values.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: entrySet()");
    }

    public String keyReps() {
        return String.join(sep, stringReps.values());
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: forEach(BiConsumer). Use forEach(Consumer) instead");
    }

    public void forEach(Consumer<? super V> action) {
        values().forEach(action);
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

    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: merge()");
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        Integer index = keys.get(key);
        if (index == null) return false;
        return values.replace(index, oldValue, newValue);
    }

    @Override
    public V replace(K key, V value) {
        Integer index = keys.get(key);
        if (index == null) return null;
        return values.replace(index, value);
    }

    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        throw new UnsupportedOperationException("Unsupported Operation for DoubleKeyMaps: replaceAll()");
    }
}
