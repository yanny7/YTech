package com.yanny.ytech;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Stream;

public class GeneralUtils {
    private GeneralUtils() {}

    public static <T> Stream<T> sortedStreamSet(@NotNull Set<T> set, Comparator<T> comparator) {
        return set.stream().sorted(comparator);
    }
}
