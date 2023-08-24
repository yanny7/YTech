package com.yanny.ytech.configuration.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.*;
import com.yanny.ytech.configuration.block_entity.DryingRackBlockEntity;
import com.yanny.ytech.registration.Holder;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class DryingRack extends Block implements EntityBlock, IProbeInfoProvider {
    private static final VoxelShape SHAPE_EAST_WEST = Shapes.box(0, 0, 7/16.0, 1, 1, 9/16.0);
    private static final VoxelShape SHAPE_NORTH_SOUTH = Shapes.box(7/16.0, 0, 0, 9/16.0, 1, 1);

    private final Holder.BlockHolder holder;

    public DryingRack(Holder.BlockHolder holder) {
        super(Properties.of());
        this.holder = holder;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @NotNull
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        return (direction == Direction.EAST || direction == Direction.WEST) ? SHAPE_NORTH_SOUTH : SHAPE_EAST_WEST;
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
        if (holder instanceof Holder.EntityBlockHolder blockHolder) {
            BlockEntityType<? extends BlockEntity> blockEntityType = blockHolder.entityType.get();
            return new DryingRackBlockEntity(blockEntityType, pos, state);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        if (!level.isClientSide) {
            return DryingRack::createDryingRackTicker;
        } else {
            return null;
        }
    }

    public static void createDryingRackTicker(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof DryingRackBlockEntity block) {
            block.tick(level, pos, state, block);
        }
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof  DryingRackBlockEntity dryingRack) {
            return dryingRack.onUse(state, level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof DryingRackBlockEntity dryingRack) {
                Containers.dropContents(level, pos, NonNullList.withSize(1, dryingRack.getItem()));
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(YTechMod.MOD_ID, getClass().getName());
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
        if (!level.isClientSide && level.getBlockEntity(probeHitData.getPos()) instanceof DryingRackBlockEntity blockEntity && blockEntity.getDryingLeft() >= 0) {
            probeInfo.horizontal().text("Remaining: ").text(Integer.toString(Math.round(blockEntity.getDryingLeft() / 20f))).text("s");
        }
    }

    public static void registerModel(@NotNull Holder.BlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures(holder.material);
        ModelFile model = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(Utils.mcLoc("block/block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 4, 16).texture("#all");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 4, 4).cullface(direction).texture("#all");
                    }
                }).from(0, 0, 7).to(2, 16, 9).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch (direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 4, 16).texture("#all");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 4, 4).cullface(direction).texture("#all");
                    }
                }).from(14, 0, 7).to(16, 16, 9).end()
                .element()
                .face(Direction.NORTH).uvs(0, 1, 16, 3).texture("#stick").end()
                .face(Direction.SOUTH).uvs(0, 1, 16, 3).texture("#stick").end()
                .face(Direction.UP).uvs(0, 1, 16, 3).texture("#stick").end()
                .face(Direction.DOWN).uvs(0, 1, 16, 3).texture("#stick").end()
                .from(2, 14, 7.5f).to(14, 15, 8.5f).end()
                .texture("particle", textures[0])
                .texture("all", textures[0])
                .texture("stick", textures[1]);
        provider.horizontalBlock(holder.block.get(), model);
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    public static void registerRecipe(@NotNull Holder.BlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.block.get())
                    .define('W', Utils.getLogFromMaterial(holder.material))
                    .define('S', Items.STICK)
                    .define('T', SimpleItemType.GRASS_TWINE.itemTag)
                    .define('F', SimpleItemType.SHARP_FLINT.itemTag)
                    .define('B', SimpleItemType.WOODEN_BOLT.itemTag)
                    .pattern("TST")
                    .pattern("BFB")
                    .pattern("W W")
                    .group(MaterialBlockType.DRYING_RACK.id + "_" + holder.material.group)
                    .unlockedBy("has_logs", RecipeProvider.has(ItemTags.LOGS))
                    .save(recipeConsumer, new ResourceLocation(YTechMod.MOD_ID, holder.key));
    }

    public static TextureHolder[] getTexture(MaterialType material) {
        return List.of(new TextureHolder(-1, -1, Utils.mcBlockLoc(material.key + "_log")),
                new TextureHolder(-1, -1, Utils.mcBlockLoc(material.key + "_planks"))).toArray(TextureHolder[]::new);
    }
}
