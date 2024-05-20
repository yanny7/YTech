package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.item.BasketItem;
import com.yanny.ytech.configuration.item.SpearItem;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

class YTechItemModelsProvider extends ItemModelProvider {
    public YTechItemModelsProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, YTechMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basketItem();
        clayBucketItem(YTechItems.LAVA_CLAY_BUCKET);
        clayBucketItem(YTechItems.WATER_CLAY_BUCKET);

        simpleItem(YTechItems.ANTLER);
        simpleItem(YTechItems.BREAD_DOUGH);
        simpleItem(YTechItems.BRICK_MOLD);
        simpleItem(YTechItems.CLAY_BUCKET);
        simpleItem(YTechItems.COOKED_VENISON);
        simpleItem(YTechItems.DRIED_BEEF);
        simpleItem(YTechItems.DRIED_CHICKEN);
        simpleItem(YTechItems.DRIED_COD);
        simpleItem(YTechItems.DRIED_MUTTON);
        simpleItem(YTechItems.DRIED_PORKCHOP);
        simpleItem(YTechItems.DRIED_RABBIT);
        simpleItem(YTechItems.DRIED_SALMON);
        simpleItem(YTechItems.DRIED_VENISON);
        simpleItem(YTechItems.FLOUR);
        simpleItem(YTechItems.GRASS_FIBERS);
        simpleItem(YTechItems.GRASS_TWINE);
        simpleItem(YTechItems.IRON_BLOOM);
        simpleItem(YTechItems.LEATHER_STRIPS);
        simpleItem(YTechItems.MAMMOTH_TUSK);
        simpleItem(YTechItems.PEBBLE);
        simpleItem(YTechItems.RAW_HIDE);
        simpleItem(YTechItems.RHINO_HORN);
        simpleItem(YTechItems.SHARP_FLINT);
        simpleItem(YTechItems.UNFIRED_BRICK);
        simpleItem(YTechItems.UNFIRED_CLAY_BUCKET);
        decoratedPotItem();
        simpleItem(YTechItems.UNFIRED_FLOWER_POT);
        simpleItem(YTechItems.VENISON);

        spawnEggModel(YTechItems.AUROCHS_SPAWN_EGG);
        spawnEggModel(YTechItems.DEER_SPAWN_EGG);
        spawnEggModel(YTechItems.FOWL_SPAWN_EGG);
        spawnEggModel(YTechItems.MOUFLON_SPAWN_EGG);
        spawnEggModel(YTechItems.SABER_TOOTH_TIGER_SPAWN_EGG);
        spawnEggModel(YTechItems.TERROR_BIRD_SPAWN_EGG);
        spawnEggModel(YTechItems.WILD_BOAR_SPAWN_EGG);
        spawnEggModel(YTechItems.WOOLLY_MAMMOTH_SPAWN_EGG);
        spawnEggModel(YTechItems.WOOLLY_RHINO_SPAWN_EGG);

        materialItem(YTechItems.ARROWS, "item/generated");
        vanillaMaterialItem(YTechItems.AXES, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/handheld");
        materialItem(YTechItems.BOLTS, "item/generated");
        vanillaMaterialItem(YTechItems.BOOTS, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/generated");
        vanillaMaterialItem(YTechItems.CHESTPLATES, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/generated");
        materialItem(YTechItems.CRUSHED_MATERIALS, "item/generated");
        materialItem(YTechItems.FILES, "item/handheld");
        materialItem(YTechItems.HAMMERS, "item/handheld");
        vanillaMaterialItem(YTechItems.HELMETS, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/generated");
        vanillaMaterialItem(YTechItems.HOES, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/handheld");
        vanillaMaterialItem(YTechItems.INGOTS, MaterialType.VANILLA_METALS, "item/generated");
        materialItem(YTechItems.KNIVES, "item/generated");
        vanillaMaterialItem(YTechItems.LEGGINGS, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/generated");
        materialItem(YTechItems.MORTAR_AND_PESTLES, "item/generated");
        vanillaMaterialItem(YTechItems.PICKAXES, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/handheld");
        materialItem(YTechItems.PLATES, "item/generated");
        vanillaMaterialItem(YTechItems.RAW_MATERIALS, MaterialType.VANILLA_METALS, "item/generated");
        materialItem(YTechItems.RODS, "item/generated");
        materialItem(YTechItems.SAWS, "item/handheld");
        materialItem(YTechItems.SAW_BLADES, "item/generated");
        vanillaMaterialItem(YTechItems.SHOVELS, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/handheld");
        spearItem();
        vanillaMaterialItem(YTechItems.SWORDS, EnumSet.of(MaterialType.IRON, MaterialType.GOLD), "item/handheld");
    }

    private void simpleItem(RegistryObject<Item> item) {
        withExistingParent(item.getId().getPath(), "item/generated")
                .texture("layer0", Utils.modItemLoc(item.getId().getPath()));
    }

    private void materialItem(YTechItems.MaterialItem materialItem, String parent) {
        materialItem.materials().forEach((material) -> {
            withExistingParent(materialItem.of(material).getId().getPath(), parent)
                    .texture("layer0", Utils.modItemLoc(materialItem.getGroup() + "/" + material.key));
        });
    }

    private void vanillaMaterialItem(YTechItems.MaterialItem materialItem, EnumSet<MaterialType> excludeMaterial, String parent) {
        materialItem.materials().forEach((material) -> {
            if (!excludeMaterial.contains(material)) {
                withExistingParent(materialItem.of(material).getId().getPath(), parent)
                        .texture("layer0", Utils.modItemLoc(materialItem.getGroup() + "/" + material.key));
            }
        });
    }

    private void basketItem() {
        ItemModelBuilder builder = getBuilder(Utils.getPath(YTechItems.BASKET)).parent(new ModelFile.UncheckedModelFile("item/generated"));
        ModelFile model = getBuilder(Utils.getPath(YTechItems.BASKET) + "_filled")
                .parent(builder)
                .texture("layer0", Utils.modItemLoc(Utils.getPath(YTechItems.BASKET) + "_filled"));

        builder.override().predicate(BasketItem.FILLED_PREDICATE, 0.0001f).model(model).end();
        builder.texture("layer0", Utils.modItemLoc(Utils.getPath(YTechItems.BASKET)));
    }

    private void clayBucketItem(@NotNull RegistryObject<Item> item) {
        ItemModelBuilder builder = getBuilder(Utils.getPath(item)).parent(new ModelFile.UncheckedModelFile("item/generated"));
        builder.texture("layer0", Utils.modItemLoc("clay_bucket"));
        builder.texture("layer1", Utils.modItemLoc("bucket_overlay"));
    }

    private void spearItem() {
        YTechItems.SPEARS.entries().forEach((entry) -> {
            RegistryObject<Item> item = entry.getValue();
            MaterialType material = entry.getKey();
            String texture = Utils.modItemLoc(YTechItems.SPEARS.getGroup() + "/" + material.key).getPath();

            getBuilder(Utils.getPath(item))
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", texture);

            ItemModelBuilder throwing = getBuilder(Utils.getPath(item) + "_throwing")
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .texture("particle", texture)
                    .transforms()
                    .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 90, 180).translation(8, -17, 9).end()
                    .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 90, 180).translation(8, -17, -7).end()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(-3, 17, 1).end()
                    .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(13, 17, 1).end()
                    .transform(ItemDisplayContext.GUI).rotation(15, -25, -5).translation(2, 3, 0).scale(0.65F).end()
                    .transform(ItemDisplayContext.FIXED).rotation(0, 180, 0).translation(-2, 4, -5).scale(0.5F).end()
                    .transform(ItemDisplayContext.GROUND).translation(4, 4, 2).scale(0.25F).end()
                    .end();

            getBuilder(Utils.getPath(item) + "_in_hand")
                    .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                    .guiLight(BlockModel.GuiLight.FRONT)
                    .texture("particle", texture)
                    .transforms()
                    .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND).rotation(0, 60, 0).translation(11, 17, -2).end()
                    .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND).rotation(0, 60, 0).translation(3, 17, 12).end()
                    .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND).rotation(0, -90, 25).translation(-3, 17, 1).end()
                    .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND).rotation(0, 90, -25).translation(13, 17, 1).end()
                    .transform(ItemDisplayContext.GUI).rotation(15, -25, -5).translation(2, 3, 0).scale(0.65F).end()
                    .transform(ItemDisplayContext.FIXED).rotation(0, 180, 0).translation(-2, 4, -5).scale(0.5F).end()
                    .transform(ItemDisplayContext.GROUND).translation(4, 4, 2).scale(0.25F).end()
                    .end()
                    .override().predicate(SpearItem.THROWING_PREDICATE, 1).model(throwing).end();
        });
    }

    private void decoratedPotItem() {
        getBuilder(Utils.getPath(YTechItems.UNFIRED_DECORATED_POT))
                .parent(getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(1, 0, 15, 16).texture("#1");
                        case EAST -> faceBuilder.uvs(1, 0, 15, 16).texture("#1");
                        case SOUTH -> faceBuilder.uvs(1, 0, 15, 16).texture("#1");
                        case WEST -> faceBuilder.uvs(1, 0, 15, 16).texture("#1");
                        case UP -> faceBuilder.uvs(0, 6.5f, 7, 13.5f).texture("#0");
                        case DOWN -> faceBuilder.uvs(7, 6.5f, 14, 13.5f).texture("#0");
                    }
                })
                .from(1, 0, 1).to(15, 16, 15).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(9, 5.5f, 12, 6).texture("#0");
                        case EAST -> faceBuilder.uvs(6, 5.5f, 9, 6).texture("#0");
                        case SOUTH -> faceBuilder.uvs(3, 5.5f, 6, 6).texture("#0");
                        case WEST -> faceBuilder.uvs(0, 5.5f, 3, 6).texture("#0");
                    }
                })
                .from(5, 16, 5).to(11, 17, 11).rotation().angle(0).axis(Direction.Axis.Y).origin(7, 16, 7).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(4, 4, 8, 5.5f).texture("#0");
                        case EAST -> faceBuilder.uvs(12, 4, 16, 5.5f).texture("#0");
                        case SOUTH -> faceBuilder.uvs(8, 4, 12, 5.5f).texture("#0");
                        case WEST -> faceBuilder.uvs(0, 4, 4, 5.5f).texture("#0");
                        case UP -> faceBuilder.uvs(4, 0, 8, 4).texture("#0");
                        case DOWN -> faceBuilder.uvs(8, 0, 12, 4).texture("#0");
                    }
                })
                .from(4, 17, 4).to(12, 20, 12).rotation().angle(0).axis(Direction.Axis.Y).origin(4, 17, 4).end()
                .end()
                .texture("particle", Utils.modBlockLoc("unfired_decorated_pot_side"))
                .texture("0", Utils.modBlockLoc("unfired_decorated_pot_base"))
                .texture("1", Utils.modBlockLoc("unfired_decorated_pot_side"));
    }

    private void spawnEggModel(RegistryObject<Item> item) {
        getBuilder(Utils.getPath(item)).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }
}
