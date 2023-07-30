package com.yanny.ytech;

import com.yanny.ytech.registration.Holder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class GeneralUtils {
    private GeneralUtils() {}

    public static <T, U, V, X extends V> X getFromMap(HashMap<T, HashMap<U, Holder>> holder, T type1, U type2, Class<X> clazz) {
        return Objects.requireNonNull(clazz.cast(holder.get(type1).get(type2)));
    }

    public static <T, U, V> Stream<V> mapToStream(HashMap<T, HashMap<U, V>> map) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream());
    }

    public static <T, U, V> Stream<Map.Entry<U, V>> sortedStreamMap(HashMap<T, HashMap<U, V>> map, Comparator<Map.Entry<U, V>> comparator) {
        return map.entrySet()
                .stream()
                .flatMap(e -> e.getValue().entrySet().stream())
                .sorted(comparator);
    }

    public static <T, U, V, X extends V> Stream<X> filteredSortedStream(HashMap<T, HashMap<U, V>> map, Predicate<Map.Entry<U, V>> predicate,
                                                                        Comparator<Map.Entry<U, V>> comparator, Class<X> clazz) {
        return map.entrySet()
                .stream()
                .flatMap(e -> e.getValue().entrySet().stream())
                .filter(predicate)
                .sorted(comparator)
                .map(e -> clazz.cast(e.getValue()));
    }

    public static <T, U, V> Stream<V> filteredStream(HashMap<T, HashMap<U, V>> map, Predicate<V> predicate) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream()).filter(predicate);
    }

    public static <T, U, V, X extends V> Stream<X> filteredStream(HashMap<T, HashMap<U, V>> map, Predicate<V> predicate, Class<X> clazz) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream()).filter(predicate).map(clazz::cast);
    }
}
