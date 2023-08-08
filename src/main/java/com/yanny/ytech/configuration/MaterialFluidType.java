package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public enum MaterialFluidType implements INameable, IMaterialModel<Holder.FluidHolder, ItemModelProvider> {
    FLUID("fluid", INameable.suffix("bucket"), INameable.prefix("Bucket of"),
            (material) -> bucketTexture(IModel.modItemLoc("bucket_overlay")),
            MaterialFluidType::bucketItemModelProvider,
            EnumSet.of(MaterialType.MERCURY)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull private final Set<Integer> tintIndices;
    @NotNull private final HashMap<MaterialType, ResourceLocation[]> textures;
    @NotNull private final BiConsumer<Holder.FluidHolder, ItemModelProvider> model;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialFluidType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Function<MaterialType, TextureHolder[]> textureGetter,
                      @NotNull BiConsumer<Holder.FluidHolder, ItemModelProvider> model, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.tintIndices = new HashSet<>();
        this.textures = new HashMap<>();
        this.model = model;
        this.materials = materials;

        for (MaterialType material : materials) {
            TextureHolder[] holders = textureGetter.apply(material);
            ArrayList<ResourceLocation> resources = new ArrayList<>();

            for (TextureHolder holder : holders) {
                if (holder.tintIndex() >= 0) {
                    this.tintIndices.add(holder.tintIndex());
                }
                resources.add(holder.texture());
            }

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
    public Set<Integer> getTintIndices() {
        return tintIndices;
    }

    @NotNull
    @Override
    public ResourceLocation[] getTextures(MaterialType material) {
        return textures.get(material);
    }

    @Override
    public void registerModel(@NotNull Holder.FluidHolder holder, @NotNull ItemModelProvider provider) {
        model.accept(holder, provider);
    }

    private static void bucketItemModelProvider(@NotNull Holder.FluidHolder holder, @NotNull ItemModelProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", textures[0]);
        builder.texture("layer1", textures[1]);
    }

    private static TextureHolder[] bucketTexture(ResourceLocation overlay) {
        return List.of(new TextureHolder(-1, IModel.mcItemLoc("bucket")), new TextureHolder(1, overlay)).toArray(TextureHolder[]::new);
    }
}
