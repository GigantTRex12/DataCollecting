package Utils;

import exceptions.DuplicateKeyException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A Map implementation that allows mapping String representations to executable actions in the Form of
 * {@link Runnable}. The String representations are case-insensitive and null values are not permitted. Attempting
 * to put null as key or value will throw a {@link NullPointerException}. Keys may not repeat, attempting to
 * put an already existing key will throw a {@link DuplicateKeyException}.
 * Removing or replacing a key will remove/replace all other keys that point to the same value. Keys point to the
 * same value when they were either added together in the same call of {@link #put(String, Runnable, List)} or
 * added later with {@link #addKeys(String, String...)}. For every value there is a main key, which will be the first
 * key given in a put call. To not repeat values {@link #size()}, {@link #keySet()}, {@link #values()} and
 * {@link #forEach(BiConsumer)} will only look at the main keys. The {@link #containsKey(Object)} method looks at
 * all keys.
 * This class is an implementation of {@link Consumer} and by calling {@link #accept(String)} the runnable that
 * String is mapped to can be executed. The {@link #keyReps(String)} or {@link #keyReps(String, String)} method
 * can be used to generate a String representing the options which keys can be accepted.
 */
public class ActionMap implements Map<String, Runnable>, Consumer<String> {

    private final Map<String, Box> map = new HashMap<>();
    private final Set<String> keySet = new LinkedHashSet<>();

    @Override
    public int size() {
        return keySet.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        if (key instanceof String s) return map.containsKey(s.toLowerCase());
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable get(Object key) {
        if (key instanceof String s) return map.get(s.toLowerCase()).value;
        return null;
    }

    /**
     * Links the given key to the given value.
     *
     * @param key String representation of the key to point to the value.
     * @param value Runnable the given key should get linked to.
     * @return The value the given key is now associated on success. Always returns the parameter value.
     * @throws NullPointerException If the specified key or value is null.
     * @throws DuplicateKeyException If the key is already linked to some value.
     */
    @Override
    public Runnable put(String key, Runnable value) {
        if (key == null || value == null) throw new NullPointerException("Keys or values in ActionMaps may never be null!");
        if (map.containsKey(key.toLowerCase())) throw new DuplicateKeyException("Key " + key + " already exists!");

        Box box = new Box(value, key);
        map.put(key.toLowerCase(), box);
        keySet.add(key.toLowerCase());
        return value;
    }

    /**
     * Links all the given keys to the given value.
     *
     * @param key String representation of the main key to point to the value.
     * @param value Runnable the given key should get linked to.
     * @param extraKeys A List of keys that will be pointing to the same given value. These keys will not be main keys
     *                  and will be associative with the main key.
     * @return The value the given keys are now associated on success. Always returns the parameter value.
     * @throws NullPointerException If the specified key or value is null or if the extraKeys List is null or empty.
     * @throws DuplicateKeyException If any of the given keys is already linked to some value.
     */
    public Runnable put(String key, Runnable value, List<String> extraKeys) {
        if (key == null || value == null) throw new NullPointerException("Keys or values in ActionMaps may never be null!");
        if (extraKeys == null || extraKeys.isEmpty()) throw new NullPointerException("A List of extra keys needs to be provided and not be empty!");
        if (map.containsKey(key.toLowerCase())) throw new DuplicateKeyException("Key " + key + " already exists!");
        for (String extraKey : extraKeys) {
            if (extraKey == null) throw new NullPointerException("Keys in ActionMaps may never be null!");
            if (map.containsKey(extraKey.toLowerCase())) throw new DuplicateKeyException("Key " + extraKey + " already exists!");
        }

        Box box = new Box(value, key, extraKeys);
        map.put(key.toLowerCase(), box);
        keySet.add(key.toLowerCase());
        extraKeys.forEach(extraKey -> map.put(extraKey.toLowerCase(), box));
        return value;
    }

    /**
     * Links the newKeys to the value the given key is linked to.
     *
     * @param key The key from which the value will be linked to.
     * @param newKeys The new keys which will be linked to the value.
     * @return The value the given key is associated on success.
     * @throws NullPointerException If the given key or any of the newKeys is null or the given key is not currently
     *                  in this map.
     * @throws DuplicateKeyException If any of the given newKeys are linked to some value.
     */
    public Runnable addKeys(String key, String... newKeys) {
        if (key == null) throw new NullPointerException("Keys in ActionMaps can never be null!");
        if (!containsKey(key)) throw new NullPointerException("The given key is not in this map");
        for (String newKey : newKeys) {
            if (newKey == null) throw new NullPointerException("Keys in ActionMaps may never be null!");
            if (containsKey(newKey)) throw new DuplicateKeyException("Key " + newKey + " already exists!");
        }

        Box box = map.get(key.toLowerCase());
        box.keys.addAll(new LinkedList<>(Arrays.asList(newKeys)));
        Arrays.stream(newKeys).forEach(newKey -> map.put(newKey.toLowerCase(), box));
        return box.value;
    }

    /**
     * Removes only the keys without affecting other keys linked to the same value. Will skip any keys that are
     * null, a main key or not currently linked to a value.
     *
     * @param keys The keys to remove.
     * @return The amount of successfully removed keys.
     */
    public int removeKeys(String... keys) {
        int removed = 0;
        for (String key : keys) {
            if (key == null) continue;
            Box box = map.get(key.toLowerCase());
            if (box == null || box.mainKey.equalsIgnoreCase(key)) continue;
            box.keys.removeIf(key::equalsIgnoreCase);
            map.remove(key.toLowerCase());
            removed++;
        }
        return removed;
    }

    /**
     * Removes the value the given key is linked to. Will also cause all other keys linked to the same value to
     * be removed.
     *
     * @param key The key from which the value is to be removed from the map.
     * @return The removed value or null if the key is not currently linked to a value.
     */
    @Override
    public Runnable remove(Object key) {
        if (key instanceof String s) {
            Box box = map.get(s.toLowerCase());
            if (box == null) return null;
            box.forEachKey(ks -> map.remove(ks.toLowerCase()));
            keySet.remove(box.mainKey.toLowerCase());
            return box.value;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends Runnable> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        map.clear();
        keySet.clear();
    }

    @Override
    public Set<String> keySet() {
        return keySet;
    }

    @Override
    public Collection<Runnable> values() {
        List<Runnable> values = new ArrayList<>();
        keySet.forEach(k -> values.add(map.get(k).value));
        return values;
    }

    @Override
    public Set<Entry<String, Runnable>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable getOrDefault(Object key, Runnable defaultValue) {
        Runnable value = get(key);
        return value == null ? defaultValue : value;
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Runnable> action) {
        keySet.forEach(key -> action.accept(key, get(key)));
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Runnable, ? extends Runnable> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable putIfAbsent(String key, Runnable value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object key, Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean replace(String key, Runnable oldValue, Runnable newValue) {
        throw new UnsupportedOperationException();
    }

    /**
     * Replaces the value the given key is linked to. All other keys linked to the same value will also have their
     * value replaced. Has no effect when the key is not linked to any value.
     *
     * @param key The key from which the value is to be changed.
     * @param value The new value.
     * @return The old value or null if there was no value linked before.
     * @throws NullPointerException If the specified key is null.
     */
    @Override
    public Runnable replace(String key, Runnable value) {
        if (key == null) throw new NullPointerException("Keys in ActionMaps can never be null!");
        Box box = map.get(key.toLowerCase());
        if (box == null) return null;
        Runnable oldValue = box.value;
        box.value = value;
        return oldValue;
    }

    @Override
    public Runnable computeIfAbsent(String key, Function<? super String, ? extends Runnable> mappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable computeIfPresent(String key, BiFunction<? super String, ? super Runnable, ? extends Runnable> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable compute(String key, BiFunction<? super String, ? super Runnable, ? extends Runnable> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable merge(String key, Runnable value, BiFunction<? super Runnable, ? super Runnable, ? extends Runnable> remappingFunction) {
        throw new UnsupportedOperationException();
    }

    /**
     * Executes the {@link Runnable} the given key is currently linked to. Does nothing when the key is not
     * linked to a value.
     *
     * @param s The key.
     * @throws NullPointerException If the specified key is null.
     */
    @Override
    public void accept(String s) {
        if (s == null) throw new NullPointerException("Keys in ActionMaps can never be null!");
        Box box = map.get(s.toLowerCase());
        if (box.value != null) box.value.run();
    }

    /**
     * Executes the {@link Runnable} the given key is currently linked to. If the given key is not linked
     * to a value will instead run the fallback function.
     *
     * @param s The key.
     * @param fallback The fallback function called when the key is not linked to a value.
     * @throws NullPointerException If the specified key is null.
     */
    public void acceptOrFallback(String s, Runnable fallback) {
        if (s == null) throw new NullPointerException("Keys in ActionMaps can never be null!");
        Box box = map.get(s.toLowerCase());
        if (box.value != null) box.value.run();
        else fallback.run();
    }

    /**
     * Works like calling {@link #keyReps(String, String)} with both separators being '; '
     */
    public String keyReps() {
        return keyReps("; ");
    }

    /**
     * Works like calling {@link #keyReps(String, String)} with the same separator.
     */
    public String keyReps(String sep) {
        return keyReps(sep, sep);
    }

    /**
     * Creates and returns a String representation of all keys in this map, where keys linked to the same value are
     * linked together.
     * If for example sep_main is ',' and sep_small is ';' then the result could for example look something like this:
     * 'keyA1 (keyA2;keyA3),keyB1 (keyB2),keyC1'
     *
     * @param sep_main The separator for keys linked to different values.
     * @param sep_small The separator for keys linked to the same value.
     * @return A string representation of the keys in this map
     */
    public String keyReps(String sep_main, String sep_small) {
        return keySet.stream()
                .map(key -> map.get(key).keyReps(sep_small))
                .collect(Collectors.joining(sep_main));
    }

    private static class Box {
        Runnable value;
        final String mainKey;
        final List<String> keys;

        Box(Runnable value, String mainKey, List<String> keys) {
            this.value = value;
            this.mainKey = mainKey;
            this.keys = keys;
        }

        Box(Runnable value, String mainKey) {
            this.value = value;
            this.mainKey = mainKey;
            keys = new LinkedList<>();
        }

        void forEachKey(Consumer<String> function) {
            function.accept(mainKey);
            keys.forEach(function);
        }

        String keyReps(String sep) {
            if (keys.isEmpty()) return mainKey;
            return mainKey + " (" + String.join(sep, keys) + ")";
        }
    }

}
