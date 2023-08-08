package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.Holder;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;

public enum MaterialFluidType implements MaterialEnumHolder {
    FLUID("fluid", MaterialEnumHolder.suffix("bucket"), MaterialEnumHolder.prefix("Bucket of"),
            Set.of(1),
            MaterialFluidType::bucketItemModelProvider,
            EnumSet.of(MaterialType.MERCURY)),
    ;

    @NotNull public final String id;
    @NotNull private final NameHolder key;
    @NotNull private final NameHolder name;
    @NotNull private final Set<Integer> tintIndices;
    @NotNull private final BiConsumer<Holder.FluidHolder, ItemModelProvider> model;
    @NotNull public final EnumSet<MaterialType> materials;

    MaterialFluidType(@NotNull String id, @NotNull NameHolder key, @NotNull NameHolder name, @NotNull Set<Integer> tintIndices,
                      @NotNull BiConsumer<Holder.FluidHolder, ItemModelProvider> model, @NotNull EnumSet<MaterialType> materials) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.tintIndices = tintIndices;
        this.model = model;
        this.materials = materials;
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

    public void registerModel(Holder.FluidHolder holder, ItemModelProvider provider) {
        model.accept(holder, provider);
    }

    private static void bucketItemModelProvider(@NotNull Holder.FluidHolder holder, @NotNull ItemModelProvider provider) {
        ItemModelBuilder builder = provider.getBuilder(holder.key).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", provider.mcLoc(ItemModelProvider.ITEM_FOLDER + "/bucket"));
        builder.texture("layer1", provider.modLoc(ItemModelProvider.ITEM_FOLDER + "/bucket_overlay"));
    }
}
