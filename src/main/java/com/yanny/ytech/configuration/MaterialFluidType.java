package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum MaterialFluidType implements INameable, IMaterialModel<Holder.FluidHolder, ItemModelProvider>, IFluidTag<Holder.FluidHolder> {
    FLUID("fluid", INameable.suffix("bucket"), INameable.prefix("Bucket of"),
            (material) -> FluidTags.create(Utils.forgeLoc(material.key)),
            (material) -> bucketTexture(Utils.modItemLoc("bucket_overlay"), material),
            MaterialFluidType::bucketItemModelProvider,
            (holder, provider) -> provider.tag(holder.object.fluidTag.get(holder.material)).add(holder.source.get()).add(holder.flowing.get()),
            EnumSet.of(MaterialType.MERCURY)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull public final Map<MaterialType, TagKey<Fluid>> fluidTag;
    @NotNull private final Map<MaterialType, Map<Integer, Integer>> tintColors;
    @NotNull private final Map<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.FluidHolder, ItemModelProvider> model;
    @NotNull private final BiConsumer<Holder.FluidHolder, FluidTagsProvider> fluidTagsGetter;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialFluidType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Function<MaterialType, TagKey<Fluid>> fluidTag,
                      @NotNull Function<MaterialType, TextureHolder[]> textureGetter,
                      @NotNull BiConsumer<Holder.FluidHolder, ItemModelProvider> model,
                      @NotNull BiConsumer<Holder.FluidHolder, FluidTagsProvider> fluidTagsGetter, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.fluidTag = materials.stream().map((material) -> Pair.of(material, fluidTag.apply(material))).collect(Collectors.toMap(Pair::getKey, Pair::getValue));
        this.tintColors = new HashMap<>();
        this.textures = new HashMap<>();
        this.model = model;
        this.fluidTagsGetter = fluidTagsGetter;
        this.materials = materials;

        for (MaterialType material : materials) {
            TextureHolder[] holders = textureGetter.apply(material);
            ArrayList<ResourceLocation> resources = new ArrayList<>();
            Map<Integer, Integer> tintMap = new HashMap<>();

            for (TextureHolder holder : holders) {
                if (holder.tintIndex() >= 0) {
                    tintMap.put(holder.tintIndex(), holder.color());
                }

                resources.add(holder.texture());
            }

            this.tintColors.put(material, tintMap);
            this.textures.computeIfAbsent(material, (m) -> resources.toArray(ResourceLocation[]::new));
        }
    }

    @NotNull
    @Override
    public NameHolder getKeyHolder() {
        return key;
    }

    @NotNull
    @Override
    public NameHolder getNameHolder() {
        return name;
    }

    @NotNull
    @Override
    public Map<Integer, Integer> getTintColors(@NotNull MaterialType material) {
        return tintColors.get(material);
    }

    @NotNull
    @Override
    public ResourceLocation[] getTextures(@NotNull MaterialType material) {
        return textures.get(material);
    }

    @Override
    public void registerModel(@NotNull Holder.FluidHolder holder, @NotNull ItemModelProvider provider) {
        model.accept(holder, provider);
    }

    @Override
    public void registerTag(@NotNull Holder.FluidHolder holder, @NotNull FluidTagsProvider provider) {
        fluidTagsGetter.accept(holder, provider);
    }

    private static void bucketItemModelProvider(@NotNull Holder.FluidHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
        builder.texture("layer1", textures[1]);
    }

    @NotNull
    private static TextureHolder[] bucketTexture(@NotNull ResourceLocation overlay, MaterialType material) {
        return List.of(new TextureHolder(-1, -1, Utils.mcItemLoc("bucket")),
                new TextureHolder(1, material.color, overlay)).toArray(TextureHolder[]::new);
    }
}
