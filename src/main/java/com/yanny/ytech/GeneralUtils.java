package com.yanny.ytech;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class GeneralUtils {
    private GeneralUtils() {}

    @NotNull
    public static <T, U, V> V getFromMap(@NotNull HashMap<T, HashMap<U, V>> holder, @NotNull T type1, @NotNull U type2) {
        return Objects.requireNonNull(Objects.requireNonNull(holder.get(type1)).get(type2));
    }

    @NotNull
    public static <T, U, V> Stream<V> mapToStream(@NotNull HashMap<T, HashMap<U, V>> map) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream());
    }

    @NotNull
    public static <T, U, V> Stream<Map.Entry<U, V>> sortedStreamMap(@NotNull HashMap<T, HashMap<U, V>> map, @NotNull Comparator<Map.Entry<U, V>> comparator) {
        return map.entrySet()
                .stream()
                .flatMap(e -> e.getValue().entrySet().stream())
                .sorted(comparator);
    }
}
