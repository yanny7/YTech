package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.BrickChimneyBlockEntity;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class BrickChimneyBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.box(3/16.0, 0, 3/16.0, 13/16.0, 1, 13/16.0);

    protected final Holder.SimpleBlockHolder holder;

    public BrickChimneyBlock(Holder.SimpleBlockHolder holder) {
        super(Properties.copy(Blocks.BRICKS).strength(2.0f, 2.0f));
        this.holder = holder;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (holder instanceof Holder.EntitySimpleBlockHolder blockHolder) {
            return new BrickChimneyBlockEntity(blockHolder.entityType.get(), pos, state);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && !state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof  BrickChimneyBlockEntity blockEntity) {
            blockEntity.onRemove();
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('B', Items.BRICKS)
                .pattern(" B ")
                .pattern("B B")
                .pattern(" B ")
                .unlockedBy(RecipeProvider.getHasName(Items.BRICKS), RecipeProvider.has(Items.BRICKS))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    public static void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ModelFile model = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(3, 0, 13, 16).texture("#0");
                        case EAST -> faceBuilder.uvs(3, 0, 13, 16).texture("#0");
                        case SOUTH -> faceBuilder.uvs(3, 0, 13, 16).texture("#0");
                        case WEST -> faceBuilder.uvs(3, 0, 13, 16).texture("#0");
                        case UP -> faceBuilder.uvs(3, 3, 13, 13).texture("#1");
                        case DOWN -> faceBuilder.uvs(3, 3, 13, 13).texture("#1");
                    }
                })
                .from(3, 0, 3).to(13, 16, 13).end()
                .texture("particle", textures[0])
                .texture("0", textures[0])
                .texture("1", textures[1]);
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    public static TextureHolder[] getTexture() {
        return List.of(
                new TextureHolder(-1, -1, Utils.modBlockLoc("bricks")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("machine/primitive_smelter_top"))
        ).toArray(TextureHolder[]::new);
    }
}
