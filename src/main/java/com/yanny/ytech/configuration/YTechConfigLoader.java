package com.yanny.ytech.configuration;

import com.google.gson.*;
import com.yanny.ytech.machine.MachineType;
import com.yanny.ytech.machine.TierType;
import com.yanny.ytech.network.kinetic.common.KineticBlockType;
import com.yanny.ytech.registration.Registration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class YTechConfigLoader {
    private static final YTechRec MODEL;
    private static final Map<String, Material> MATERIAL_MAP = new HashMap<>();
    private static final Map<String, Tier> TIER_MAP = new HashMap<>();
    private static final Map<ProductType, Product> PRODUCT_MAP = new HashMap<>();

    static {
        try (InputStream inputStream = Registration.class.getResourceAsStream("/configuration.json")) {

            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Product.class, new ProductDeserializer())
                        .registerTypeAdapter(Material.class, new MaterialDeserializer())
                        .registerTypeAdapter(Model.class, new ModelDeserializer())
                        .registerTypeAdapter(Element.class, new ElementDeserializer())
                        .create();

                MODEL = gson.fromJson(bufferedReader, YTechRec.class);
            } else {
                throw new IllegalArgumentException("Missing resource configuration.json!");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (Tier tier : MODEL.tiers) {
            TIER_MAP.put(tier.id, tier);
        }
    }

    private YTechConfigLoader() {}

    public static Material getMaterial(String id) {
        return MATERIAL_MAP.get(id);
    }

    public static Material[] getElements() {
        return MODEL.materials.elements;
    }

    public static Product[] getProducts() {
        return MODEL.products();
    }

    public static Product getProduct(ProductType id) {
        return PRODUCT_MAP.get(id);
    }

    public static Machine[] getMachines() {
        return MODEL.machines;
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

    private record YTechRec(
            @NotNull Materials materials,
            @NotNull Product[] products,
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

    public record Product(
            @NotNull ProductType id,
            @NotNull NameHolder name,
            @NotNull ObjectType type,
            @NotNull Model model,
            @NotNull Material[] material
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

    public static class NameHolder {
        public final String prefix;
        public final String localizedPrefix;
        public final String suffix;
        public final String localizedSuffix;

        public String getKey(Material material) {
            return (prefix == null ? "" : (prefix + "_")) + material.id + (suffix == null ? "" : ("_" + suffix));
        }

        public String getLocalized(Material material) {
            return (localizedPrefix == null ? "" : (localizedPrefix + " ")) + material.name + (localizedSuffix == null ? "" : (" " + localizedSuffix));
        }

        private NameHolder() {
            prefix = null;
            localizedPrefix = null;
            suffix = null;
            localizedSuffix = null;
        }
    }

    public record Model(
            @NotNull Element base,
            @Nullable Element overlay
    ) {}

    public record Element(
            @NotNull ResourceLocation texture,
            @Nullable Integer tint
    ) {}

    private static class MaterialDeserializer implements JsonDeserializer<Material> {
        @Override
        public Material deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                String id = object.get("id").getAsString();
                String name = object.get("name").getAsString();
                String color = context.deserialize(object.get("color"), String.class);
                String symbol = context.deserialize(object.get("symbol"), String.class);
                Float density = context.deserialize(object.get("density"), Float.class);
                Float melting = context.deserialize(object.get("melting"), Float.class);
                Float boiling = context.deserialize(object.get("boiling"), Float.class);
                Float hardness = context.deserialize(object.get("hardness"), Float.class);
                String block = context.deserialize(object.get("block"), String.class);
                Material material = new Material(id, name, color, symbol, density, melting, boiling, hardness, block);

                MATERIAL_MAP.put(id, material);
                return material;
            } else {
                throw new JsonParseException("Expecting object");
            }
        }
    }

    private static class ProductDeserializer implements JsonDeserializer<Product> {
        @Override
        public Product deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                ProductType id = Objects.requireNonNull(context.deserialize(object.get("id"), ProductType.class), "Invalid or missing product ID");
                ObjectType type = Objects.requireNonNull(context.deserialize(object.get("type"), ObjectType.class), "Missing object type for " + id);
                NameHolder name = Objects.requireNonNull(context.deserialize(object.get("name"), NameHolder.class), "Missing product name for " + id);
                Model model = Objects.requireNonNull(context.deserialize(object.get("model"), Model.class), "Missing texture model for " + id);
                Material[] materials = Objects.requireNonNull(Arrays.stream((String[])context.deserialize(object.get("material"), String[].class))
                        .map((materialId) -> Objects.requireNonNull(getMaterial(materialId), "No material with id " + materialId))
                        .toArray(Material[]::new), "Missing product materials for " + id);
                Product product = new Product(id, name, type, model, materials);

                PRODUCT_MAP.put(id, product);
                return product;
            } else {
                throw new JsonParseException("Expecting object");
            }
        }
    }

    private static class ModelDeserializer implements JsonDeserializer<Model> {
        @Override
        public Model deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                Element base = Objects.requireNonNull(context.deserialize(object.get("base"), Element.class), "Base element must be provided");
                Element overlay = context.deserialize(object.get("overlay"), Element.class);

                return new Model(base, overlay);
            } else {
                throw new JsonParseException("Expecting object");
            }
        }
    }

    private static class ElementDeserializer implements JsonDeserializer<Element> {
        @Override
        public Element deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                String texture = Objects.requireNonNull(context.deserialize(object.get("texture"), String.class), "texture must be provided");
                Integer tint = context.deserialize(object.get("tint"), Integer.class);

                return new Element(new ResourceLocation(texture), tint);
            } else {
                throw new JsonParseException("Expecting object");
            }
        }
    }
}
