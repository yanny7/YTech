package com.yanny.ytech.configuration;

import com.google.gson.Gson;
import com.yanny.ytech.registration.Registration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class YTechConfigLoader {
    private final YTechRec model;
    private final Map<String, Material> elementMap = new HashMap<>();
    private final Map<String, Machine> machineMap = new HashMap<>();
    private final Map<String, Tier> tierMap = new HashMap<>();
    private final Set<String> oreSet = new HashSet<>();
    private final Set<String> metalSet = new HashSet<>();
    private final Set<String> mineralSet = new HashSet<>();
    private final Set<String> dustSet = new HashSet<>();
    private final Set<String> fluidSet = new HashSet<>();
    private final Set<String> gasSet = new HashSet<>();

    public YTechConfigLoader() {
        try (InputStream inputStream = Registration.class.getResourceAsStream("/configuration.json")) {

            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new Gson();

                model = gson.fromJson(bufferedReader, YTechRec.class);
            } else {
                throw new IllegalArgumentException("Missing resource configuration.json!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Material element : model.materials.elements) {
            elementMap.put(element.id, element);
        }

        for (Tier tier : model.tiers) {
            tierMap.put(tier.id, tier);
        }

        for (Machine machine : model.machines) {
            machineMap.put(machine.id, machine);
        }

        oreSet.addAll(Arrays.asList(model.properties.ore));
        metalSet.addAll(Arrays.asList(model.properties.metal));
        mineralSet.addAll(Arrays.asList(model.properties.mineral));
        dustSet.addAll(Arrays.asList(model.properties.dust));
        fluidSet.addAll(Arrays.asList(model.properties.fluid));
        gasSet.addAll(Arrays.asList(model.properties.gas));
    }
    public Material[] getElements() {
        return model.materials.elements;
    }

    public Material getElement(String id) {
        return elementMap.get(id);
    }

    public Machine[] getMachines() {
        return model.machines;
    }

    public Machine getMachine(String id) {
        return machineMap.get(id);
    }

    public Tier[] getTiers() {
        return model.tiers;
    }

    public Tier getTier(String id) {
        return tierMap.get(id);
    }

    public boolean isOre(Material material) {
        return oreSet.contains(material.id);
    }

    public boolean isMetal(Material material) {
        return metalSet.contains(material.id);
    }

    public boolean isMineral(Material material) {
        return mineralSet.contains(material.id);
    }

    public boolean isDust(Material material) {
        return dustSet.contains(material.id);
    }

    public boolean isFluid(Material material) {
        return fluidSet.contains(material.id);
    }

    public boolean isGas(Material material) {
        return gasSet.contains(material.id);
    }

    private record YTechRec(
            @NotNull Materials materials,
            @NotNull Properties properties,
            @NotNull Machine[] machines,
            @NotNull Tier[] tiers
    ) {}

    public record Materials(
            @NotNull Material[] elements,
            @NotNull Material[] alloys,
            @NotNull Material[] compounds,
            @NotNull Material[] isotopes
    ) {}

    public record Properties(
            @NotNull String[] ore,
            @NotNull String[] metal,
            @NotNull String[] mineral,
            @NotNull String[] dust,
            @NotNull String[] fluid,
            @NotNull String[] gas
    ) {}

    public record Material(
            @NotNull String id,
            @NotNull String name,
            @Nullable String color,
            @NotNull String symbol,
            @Nullable Float density,
            @Nullable Float melting,
            @Nullable Float boiling,
            @Nullable Float hardness
    ) {
        public int getColor() {
            if (color != null) {
                return Color.decode(color).getRGB();
            } else {
                return -1;
            }
        }
    }

    public record Machine(
        @NotNull String id,
        @NotNull String name,
        @NotNull String fromTier
    ) {}

    public record Tier(
            @NotNull String id,
            @NotNull String name
    ) {}
}
