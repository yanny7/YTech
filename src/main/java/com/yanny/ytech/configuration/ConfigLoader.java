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

public class ConfigLoader {
    private static final YTechRec MODEL;
    private static final Map<String, Material> MATERIAL_MAP = new HashMap<>();
    private static final Map<TierType, Tier> TIER_MAP = new HashMap<>();

    static {
        try (InputStream inputStream = Registration.class.getResourceAsStream("/configuration.json")) {

            if (inputStream != null) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(GameObjects.class, new GameObjectDeserializer())
                        .registerTypeAdapter(ItemObject.class, new ItemObjectDeserializer())
                        .registerTypeAdapter(BlockObject.class, new BlockObjectDeserializer())
                        .registerTypeAdapter(FluidObject.class, new FluidObjectDeserializer())
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

    private ConfigLoader() {}

    public static Material getMaterial(String id) {
        return MATERIAL_MAP.get(id);
    }

    public static Material[] getElements() {
        return MODEL.materials.elements;
    }

    public static ItemObject[] getItemObjects() {
        return MODEL.objects.items;
    }

    public static BlockObject[] getBlockObjects() {
        return MODEL.objects.blocks;
    }

    public static FluidObject[] getFluidObjects() {
        return MODEL.objects.fluids;
    }

    public static Machine[] getMachines() {
        return MODEL.machines;
    }

    public static Tier[] getTiers() {
        return MODEL.tiers;
    }

    public static Tier getTier(TierType id) {
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
            @NotNull GameObjects objects,
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

    public record GameObjects(
            @NotNull ItemObject[] items,
            @NotNull BlockObject[] blocks,
            @NotNull FluidObject[] fluids
    ) {}

    public static class BaseObject<T> {
        @NotNull public final T id;
        @NotNull public final NameHolder name;
        @NotNull public final MaterialHolder[] materials;

        BaseObject(@NotNull T id, @NotNull NameHolder name, @NotNull MaterialHolder[] materials) {
            this.id = id;
            this.name = name;
            this.materials = materials;
        }

        BaseObject(@NotNull ConfigLoader.BaseObject<T> self) {
            id = self.id;
            name = self.name;
            materials = self.materials;
        }
    }

    public static class ItemObject extends BaseObject<ItemObjectType> {
        ItemObject(@NotNull BaseObject<ItemObjectType> obj) {
            super(obj);
        }
    }

    public static class BlockObject extends BaseObject<BlockObjectType> {
        BlockObject(@NotNull BaseObject<BlockObjectType> obj) {
            super(obj);
        }
    }

    public static class FluidObject extends BaseObject<FluidObjectType> {
        FluidObject(@NotNull BaseObject<FluidObjectType> obj) {
            super(obj);
        }
    }

    public record Material(
            @NotNull String id,
            @NotNull String name,
            @Nullable String color,
            @Nullable String symbol,
            @Nullable Float density,
            @Nullable Float melting,
            @Nullable Float boiling,
            @Nullable Float hardness,
            @Nullable String block,
            @Nullable String item
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
            @NotNull MachineType id,
            @NotNull String name,
            @NotNull TierType fromTier
    ) {}

    public record Tier(
            @NotNull TierType id,
            @NotNull String name
    ) {}

    public record Generator(
            @NotNull Kinetic[] kinetic
    ) {}

    public record Kinetic (
            @NotNull KineticBlockType id,
            @NotNull String name,
            @NotNull KineticMaterial[] materials
    ) {}

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

    public record MaterialHolder (
            @NotNull Material material,
            @Nullable Model model
    ) {}

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
                String id = Objects.requireNonNull(object.get("id").getAsString(), "Invalid or missing material ID");
                String name = Objects.requireNonNull(object.get("name").getAsString(), "Missing material name for " + id);
                String color = context.deserialize(object.get("color"), String.class);
                String symbol = context.deserialize(object.get("symbol"), String.class);
                Float density = context.deserialize(object.get("density"), Float.class);
                Float melting = context.deserialize(object.get("melting"), Float.class);
                Float boiling = context.deserialize(object.get("boiling"), Float.class);
                Float hardness = context.deserialize(object.get("hardness"), Float.class);
                String block = context.deserialize(object.get("block"), String.class);
                String item = context.deserialize(object.get("item"), String.class);
                Material material = new Material(id, name, color, symbol, density, melting, boiling, hardness, block, item);

                MATERIAL_MAP.put(id, material);
                return material;
            } else {
                throw new JsonParseException("Expecting object");
            }
        }
    }

    private static class GameObjectDeserializer implements JsonDeserializer<GameObjects> {
        @Override
        public GameObjects deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                ItemObject[] items = context.deserialize(object.get("items"), ItemObject[].class);
                BlockObject[] blocks = context.deserialize(object.get("blocks"), BlockObject[].class);
                FluidObject[] fluids = context.deserialize(object.get("fluids"), FluidObject[].class);
                return new GameObjects(items, blocks, fluids);
            } else {
                throw new JsonParseException("Expecting object");
            }
        }
    }

    private static class BaseObjectDeserializer<T, V extends BaseObject<T>> implements JsonDeserializer<V> {
        private final ParentObjectFunction<T, V> consumer;
        private final Class<T> objectTypeClass;

        BaseObjectDeserializer(ParentObjectFunction<T, V> consumer, Class<T> objectTypeClass) {
            this.consumer = consumer;
            this.objectTypeClass = objectTypeClass;
        }

        @Override
        public V deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject object = json.getAsJsonObject();
                T id = Objects.requireNonNull(context.deserialize(object.get("id"), objectTypeClass), "Invalid or missing product ID");
                NameHolder name = Objects.requireNonNull(context.deserialize(object.get("name"), NameHolder.class), "Missing product name for " + id);
                Model model = context.deserialize(object.get("model"), Model.class);

                if (!object.has("material") || !object.get("material").isJsonArray()) {
                    throw new JsonParseException("Missing or invalid material array");
                }

                MaterialHolder[] materials = object.get("material").getAsJsonArray().asList().stream().map(m -> {
                    if (m.isJsonPrimitive() && m.getAsJsonPrimitive().isString()) {
                        return new MaterialHolder(Objects.requireNonNull(getMaterial(context.deserialize(m, String.class)),
                                "Invalid material id " + m.getAsString()), model);
                    } else if (m.isJsonObject()) {
                        MaterialHolder holder = context.deserialize(m, MaterialHolder.class);

                        if (holder.model == null && model != null) {
                            return new MaterialHolder(holder.material, model);
                        } else if (holder.model != null) {
                            return holder;
                        } else {
                            throw new JsonParseException("Missing model definition");
                        }
                    } else {
                        throw new JsonParseException("Invalid array entry, expecting String or Object");
                    }
                }).toArray(MaterialHolder[]::new);

                return consumer.factory(new BaseObject<>(id, name, materials), object, context);
            } else {
                throw new JsonParseException("Expecting object");
            }
        }
    }

    private static class ItemObjectDeserializer extends BaseObjectDeserializer<ItemObjectType, ItemObject> {
        ItemObjectDeserializer() {
            super((base, object, context) -> new ItemObject(base), ItemObjectType.class);
        }
    }

    private static class BlockObjectDeserializer extends BaseObjectDeserializer<BlockObjectType, BlockObject> {
        BlockObjectDeserializer() {
            super((base, object, context) -> new BlockObject(base), BlockObjectType.class);
        }
    }

    private static class FluidObjectDeserializer extends BaseObjectDeserializer<FluidObjectType, FluidObject> {
        FluidObjectDeserializer() {
            super((base, object, context) -> new FluidObject(base), FluidObjectType.class);
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

    private interface ParentObjectFunction<U, T extends BaseObject<U>> {
        T factory(BaseObject<U> baseObject, JsonObject object, JsonDeserializationContext context);
    }
}
