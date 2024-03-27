package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.SimpleBlockType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

public class ReinforcedBrickChimneyBlock extends BrickChimneyBlock {
    private static final VoxelShape SHAPE = Shapes.box(1/16.0, 0, 1/16.0, 15/16.0, 1, 15/16.0);

    public ReinforcedBrickChimneyBlock(Holder.SimpleBlockHolder holder) {
        super(holder);
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    public static void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('#', SimpleBlockType.REINFORCED_BRICKS.itemTag)
                .pattern(" # ")
                .pattern("# #")
                .pattern(" # ")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(SimpleBlockType.REINFORCED_BRICKS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ModelFile model = provider.models().getBuilder(holder.key)
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
                .texture("particle", textures[0])
                .texture("0", textures[0])
                .texture("1", textures[1]);
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    public static TextureHolder[] getTexture() {
        return List.of(
                new TextureHolder(-1, -1, Utils.modBlockLoc("reinforced_bricks")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("machine/primitive_smelter_top"))
        ).toArray(TextureHolder[]::new);
    }
}
