package com.yanny.ytech;

import com.yanny.ytech.configuration.ObjectType;
import com.yanny.ytech.configuration.ProductType;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.registration.Holder;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class GeneralUtils {
    private GeneralUtils() {}

    public static <T extends Holder> T get(Holder holder, Class<T> clazz) throws ClassCastException {
        return clazz.cast(holder);
    }

    public static <T extends Holder> HashMap<YTechConfigLoader.Material, T> get(HashMap<ProductType, HashMap<YTechConfigLoader.Material, Holder>> holder, ProductType productType, Class<HashMap<YTechConfigLoader.Material, T>> clazz) {
        return Objects.requireNonNull(clazz.cast(holder.get(productType)));
    }

    public static <T extends Holder> T get(HashMap<ProductType, HashMap<YTechConfigLoader.Material, Holder>> holder, ProductType productType, YTechConfigLoader.Material material, Class<T> clazz) {
        return Objects.requireNonNull(clazz.cast(holder.get(productType).get(material)));
    }

    public static <T, U, V> Stream<V> flatStream(HashMap<T, HashMap<U, V>> map) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream());
    }

    public static <V extends Holder> Stream<Map.Entry<YTechConfigLoader.Material, V>> filteredStreamMap(HashMap<ProductType, HashMap<YTechConfigLoader.Material, V>> map, ObjectType type) {
        return map.entrySet().stream().filter(e -> YTechConfigLoader.getProduct(e.getKey()).type() == type)
                .flatMap(e -> e.getValue().entrySet().stream());
    }

    public static <V extends Holder> Stream<Map.Entry<YTechConfigLoader.Material, V>> sortedStreamMap(HashMap<ProductType, HashMap<YTechConfigLoader.Material, V>> map) {
        return map.entrySet()
                .stream()
                .flatMap(e -> e.getValue().entrySet().stream())
                .sorted(Comparator.comparing(a -> a.getValue().productType.name() + a.getKey().id()));
    }

    public static <V extends Holder, X extends Holder> Stream<X> filteredSortedStream(HashMap<ProductType, HashMap<YTechConfigLoader.Material, V>> map, ObjectType type, Class<X> clazz) {
        return map.entrySet()
                .stream()
                .flatMap(e -> e.getValue().entrySet().stream())
                .filter(e -> e.getValue().objectType == type)
                .sorted(Comparator.comparing(a -> a.getValue().productType.name() + a.getKey().id()))
                .map(e -> clazz.cast(e.getValue()));
    }

    public static <T, U, V extends Holder> Stream<V> filteredStream(HashMap<T, HashMap<U, V>> map, ObjectType type) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream()).filter(e -> e.objectType == type);
    }

    public static <T, U, V extends Holder, X extends Holder> Stream<X> filteredStream(HashMap<T, HashMap<U, V>> map, ObjectType type, Class<X> clazz) {
        return map.entrySet().stream().flatMap(e -> e.getValue().values().stream()).filter(e -> e.objectType == type).map(clazz::cast);
    }
}
