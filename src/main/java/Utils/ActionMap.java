package Utils;

import exceptions.DuplicateKeyException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ActionMap implements Map<String, Runnable>, Consumer<String> {

    private final Map<String, Box> map = new LinkedHashMap<>();
    private final Set<String> keySet = new HashSet<>();

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

    @Override
    public Runnable put(String key, Runnable value) {
        if (key == null || value == null) throw new NullPointerException("Keys or values in ActionMaps may never be null!");
        if (map.containsKey(key.toLowerCase())) throw new DuplicateKeyException("Key " + key + " already exists!");

        Box box = new Box(value, key);
        map.put(key.toLowerCase(), box);
        keySet.add(key.toLowerCase());
        return value;
    }

    public Runnable put(String key, Runnable value, List<String> extraKeys) {
        if (key == null || value == null) throw new NullPointerException("Keys or values in ActionMaps may never be null!");
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

    @Override
    public Runnable remove(Object key) {
        if (key instanceof String s) {
            Box box = map.get(s.toLowerCase());
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
        throw new UnsupportedOperationException();
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

    @Override
    public Runnable replace(String key, Runnable value) {
        Box box = map.get(key.toLowerCase());
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

    @Override
    public void accept(String s) {
        Box box = map.get(s.toLowerCase());
        if (box.value != null) box.value.run();
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

        void addKey(String newKey) {
            keys.add(newKey);
        }

        void forEachKey(Consumer<String> function) {
            function.accept(mainKey);
            keys.forEach(function);
        }
    }

}
