package com.yanny.ytech.configuration;

import com.google.gson.Gson;
import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import com.yanny.ytech.registration.Registration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class YTechConfigLoader {
    private static final YTechRec MODEL;
    private static final Map<String, Material> MATERIAL_MAP = new HashMap<>();
    private static final Map<String, Machine> MACHINE_MAP = new HashMap<>();
    private static final Map<String, Tier> TIER_MAP = new HashMap<>();
    private static final Map<String, Kinetic> KINETIC_MAP = new HashMap<>();
    private static final Set<String> ORE_SET = new HashSet<>();
    private static final Set<String> METAL_SET = new HashSet<>();
    private static final Set<String> MINERAL_SET = new HashSet<>();
    private static final Set<String> DUST_SET = new HashSet<>();
    private static final Set<String> FLUID_SET = new HashSet<>();
    private static final Set<String> GAS_SET = new HashSet<>();

    static {
        try (InputStream inputStream = Registration.class.getResourceAsStream("/configuration.json")) {

            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new Gson();

                MODEL = gson.fromJson(bufferedReader, YTechRec.class);
            } else {
                throw new IllegalArgumentException("Missing resource configuration.json!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        MATERIAL_MAP.putAll(Stream.of(MODEL.materials.elements, MODEL.materials.alloys, MODEL.materials.compounds, MODEL.materials.isotopes, MODEL.materials.minecraft)
                .flatMap(Stream::of).collect(Collectors.toMap(m -> m.id, m -> m)));

        for (Tier tier : MODEL.tiers) {
            TIER_MAP.put(tier.id, tier);
        }

        for (Machine machine : MODEL.machines) {
            MACHINE_MAP.put(machine.id, machine);
        }

        for (Kinetic kinetic : MODEL.generators.kinetic) {
            KINETIC_MAP.put(kinetic.id, kinetic);
        }

        ORE_SET.addAll(Arrays.asList(MODEL.properties.ore));
        METAL_SET.addAll(Arrays.asList(MODEL.properties.metal));
        MINERAL_SET.addAll(Arrays.asList(MODEL.properties.mineral));
        DUST_SET.addAll(Arrays.asList(MODEL.properties.dust));
        FLUID_SET.addAll(Arrays.asList(MODEL.properties.fluid));
        GAS_SET.addAll(Arrays.asList(MODEL.properties.gas));
    }

    private YTechConfigLoader() {}

    public static Material getMaterial(String id) {
        return MATERIAL_MAP.get(id);
    }

    public static Material[] getElements() {
        return MODEL.materials.elements;
    }

    public static Machine[] getMachines() {
        return MODEL.machines;
    }

    public static Machine getMachine(String id) {
        return MACHINE_MAP.get(id);
    }

    public static Tier[] getTiers() {
        return MODEL.tiers;
    }

    public static Tier getTier(String id) {
        return TIER_MAP.get(id);
    }

    public static int getTierIndex(Tier tier) {
        return Arrays.asList(MODEL.tiers).indexOf(tier);
    }

    public static Kinetic[] getKinetic() {
        return MODEL.generators.kinetic;
    }

    public static boolean isOre(Material material) {
        return ORE_SET.contains(material.id);
    }

    public static boolean isMetal(Material material) {
        return METAL_SET.contains(material.id);
    }

    public static boolean isMineral(Material material) {
        return MINERAL_SET.contains(material.id);
    }

    public static boolean isDust(Material material) {
        return DUST_SET.contains(material.id);
    }

    public static boolean isFluid(Material material) {
        return FLUID_SET.contains(material.id);
    }

    public static boolean isGas(Material material) {
        return GAS_SET.contains(material.id);
    }

    private record YTechRec(
            @NotNull Materials materials,
            @NotNull Properties properties,
            @NotNull Machine[] machines,
            @NotNull Tier[] tiers,
            @NotNull Generator generators
    ) {}

    public record Materials(
            @NotNull Material[] elements,
            @NotNull Material[] alloys,
            @NotNull Material[] compounds,
            @NotNull Material[] isotopes,
            @NotNull Material[] minecraft
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
            @Nullable String symbol,
            @Nullable Float density,
            @Nullable Float melting,
            @Nullable Float boiling,
            @Nullable Float hardness,
            @Nullable String block
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
            @NotNull String fromTier,
            @NotNull MachineType machineType
    ) {
        public Machine(String id, String name, String fromTier, MachineType machineType) {
            this.id = id;
            this.name = name;
            this.fromTier = fromTier;
            this.machineType = Objects.requireNonNull(MachineType.fromConfiguration(id));
        }
    }

    public record Tier(
            @NotNull String id,
            @NotNull String name,
            @NotNull TierType tierType
    ) {
        public Tier(String id, String name, TierType tierType) {
            this.id = id;
            this.name = name;
            this.tierType = Objects.requireNonNull(TierType.fromConfiguration(id));
        }
    }

    public record Generator(
            @NotNull Kinetic[] kinetic
    ) {}

    public record Kinetic (
            @NotNull String id,
            @NotNull String name,
            @NotNull KineticMaterial[] materials,
            @NotNull KineticBlockType kineticBlockType
    ) {
        public Kinetic(String id, String name, KineticMaterial[] materials, KineticBlockType kineticBlockType) {
            this.id = id;
            this.name = name;
            this.materials = materials;
            this.kineticBlockType = Objects.requireNonNull(KineticBlockType.fromConfiguration(id));
        }
    }

    public record KineticMaterial(
            @NotNull String id,
            float stress_multiplier
    ) {}
}
