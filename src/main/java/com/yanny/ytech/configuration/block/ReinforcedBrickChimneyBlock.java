package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ReinforcedBrickChimneyBlock extends BrickChimneyBlock {
    private static final VoxelShape SHAPE = Shapes.box(1/16.0, 0, 1/16.0, 15/16.0, 1, 15/16.0);

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getId(YTechBlocks.REINFORCED_BRICK_CHIMNEY))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(1, 0, 15, 16).texture("#0");
                        case EAST -> faceBuilder.uvs(1, 0, 15, 16).texture("#0");
                        case SOUTH -> faceBuilder.uvs(1, 0, 15, 16).texture("#0");
                        case WEST -> faceBuilder.uvs(1, 0, 15, 16).texture("#0");
                        case UP -> faceBuilder.uvs(1, 1, 15, 15).texture("#1");
                        case DOWN -> faceBuilder.uvs(1, 1, 15, 15).texture("#1");
                    }
                })
                .from(1, 0, 1).to(15, 16, 15).end()
                .texture("particle", Utils.modBlockLoc("reinforced_bricks"))
                .texture("0", Utils.modBlockLoc("reinforced_bricks"))
                .texture("1", Utils.modBlockLoc("machine/primitive_smelter_top"));
        provider.getVariantBuilder(YTechBlocks.REINFORCED_BRICK_CHIMNEY.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(Utils.getId(YTechBlocks.REINFORCED_BRICK_CHIMNEY)).parent(model);
    }

    public static void registerRecipe(@NotNull Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.REINFORCED_BRICK_CHIMNEY.get())
                .define('#', YTechItemTags.REINFORCED_BRICKS)
                .pattern(" # ")
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.REINFORCED_BRICKS))
                .save(recipeConsumer, Utils.modLoc(Utils.getId(YTechBlocks.REINFORCED_BRICK_CHIMNEY)));
    }
}
