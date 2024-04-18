package com.yanny.ytech;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class GeneralUtils {
    private GeneralUtils() {}

    @NotNull
    public static <T, U, V> Stream<V> mapToStream(@NotNull HashMap<T, HashMap<U, V>> map) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream());
    }

    @NotNull
    public static <T, U, V> Stream<Map.Entry<U, V>> sortedStreamMapOfMap(@NotNull HashMap<T, HashMap<U, V>> map, @NotNull Comparator<Map.Entry<U, V>> comparator) {
        return map.entrySet()
                .stream()
                .flatMap(e -> e.getValue().entrySet().stream())
                .sorted(comparator);
    }

    @NotNull
    public static <T, U> Stream<Map.Entry<T, U>> sortedStreamMap(@NotNull HashMap<T, U> map, @NotNull Comparator<Map.Entry<T, U>> comparator) {
        return map.entrySet().stream().sorted(comparator);
    }

    public static <T> Stream<T> sortedStreamSet(@NotNull Set<T> set, Comparator<T> comparator) {
        return set.stream().sorted(comparator);
    }
}
