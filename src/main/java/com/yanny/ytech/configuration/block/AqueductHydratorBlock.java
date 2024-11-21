package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.MaterialType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AqueductHydratorBlockEntity;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Consumer;

public class AqueductHydratorBlock extends AqueductConsumerBlock {
    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return new AqueductHydratorBlockEntity(YTechBlockEntityTypes.AQUEDUCT_HYDRATOR.get(), pos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        if (!level.isClientSide) {
            return (level1, pos, state1, blockEntity) -> AqueductConsumerBlock.createAqueductConsumerTicker(level1, blockEntity);
        } else {
            return null;
        }
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        String name = Utils.getPath(YTechBlocks.AQUEDUCT_HYDRATOR);
        ModelFile base = provider.models().getBuilder(name)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#2").cullface(direction);
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0").cullface(direction);
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("0", Utils.modBlockLoc("aqueduct/aqueduct_hydrator"))
                .texture("2", Utils.modBlockLoc("terracotta_bricks"))
                .texture("particle", Utils.modBlockLoc("aqueduct/aqueduct_hydrator"));
        ModelFile activated = provider.models().getBuilder(name + "_activated")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#2").cullface(direction);
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#4").cullface(direction);
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("2", Utils.modBlockLoc("terracotta_bricks"))
                .texture("4", Utils.modBlockLoc("aqueduct/aqueduct_hydrator_working"))
                .texture("particle", Utils.modBlockLoc("aqueduct/aqueduct_hydrator"));
        ModelFile overlay = provider.models().getBuilder(name + "_overlay")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    if (Objects.requireNonNull(direction) == Direction.NORTH) {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                    } else {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#3");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .renderType("minecraft:cutout")
                .texture("1", Utils.modBlockLoc("aqueduct/aqueduct_valve"))
                .texture("3", Utils.modBlockLoc("invisible"));
        ModelFile inventory = provider.models().getBuilder(name + "_inventory")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("0", Utils.modBlockLoc("aqueduct/aqueduct_hydrator"))
                .texture("1", Utils.modBlockLoc("aqueduct/aqueduct_valve"))
                .texture("particle", Utils.modBlockLoc("aqueduct/aqueduct_hydrator"));

        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(YTechBlocks.AQUEDUCT_HYDRATOR.get());
        builder.part().modelFile(base).addModel().condition(AqueductConsumerBlock.ACTIVATED, false).end();
        builder.part().modelFile(activated).addModel().condition(AqueductConsumerBlock.ACTIVATED, true).end();
        PROPERTY_BY_DIRECTION.forEach((dir, value) -> builder.part().modelFile(overlay).rotationY(ANGLE_BY_DIRECTION.get(dir)).addModel().condition(value, true).end());

        provider.itemModels().getBuilder(name).parent(inventory);
    }

    public static void registerRecipe(Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.AQUEDUCT_HYDRATOR.get())
                .define('#', YTechItemTags.TERRACOTTA_BRICKS)
                .define('R', YTechItemTags.RODS.get(MaterialType.COPPER))
                .define('S', YTechItemTags.PLATES.get(MaterialType.COPPER))
                .define('H', YTechItemTags.HAMMERS.tag)
                .pattern("#R#")
                .pattern("SHS")
                .pattern("#R#")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(recipeConsumer, Utils.modLoc(YTechBlocks.AQUEDUCT_HYDRATOR));
    }
}
