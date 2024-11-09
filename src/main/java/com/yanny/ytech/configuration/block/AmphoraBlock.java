package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AmphoraBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class AmphoraBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.box(3/16.0, 0, 3/16.0, 13/16.0, 1, 13/16.0);

    public AmphoraBlock() {
        super(Properties.copy(Blocks.TERRACOTTA));
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public BlockState updateShape(BlockState pState, @NotNull Direction pDirection, @NotNull BlockState pNeighborState,
                                  @NotNull LevelAccessor pLevel, @NotNull BlockPos pPos, @NotNull BlockPos pNeighborPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }

        return super.updateShape(pState, pDirection, pNeighborState, pLevel, pPos, pNeighborPos);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new AmphoraBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof AmphoraBlockEntity amphoraBlockEntity) {
            return amphoraBlockEntity.onUse(level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof AmphoraBlockEntity amphoraBlockEntity) {
                Containers.dropContents(level, pos, NonNullList.withSize(1, amphoraBlockEntity.getItem()));
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(WATERLOGGED);
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.AMPHORA))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(5, 13, 11, 16).texture("#1");
                        case DOWN -> faceBuilder.uvs(5, 5, 11, 11).texture("#0");
                    }
                })
                .from(5, 0, 5).to(11, 3, 11)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(3, 4, 13, 14).texture("#1");
                        case UP, DOWN -> faceBuilder.uvs(3, 3, 13, 13).texture("#0");
                    }
                })
                .from(3, 6, 3).to(13, 16, 13)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(6, 2, 10, 4).texture("#1");
                    }
                })
                .from(5.999f, 16, 5.999f).to(10.001f, 18, 10.001f)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(4, 10, 12, 13).texture("#1");
                        case DOWN -> faceBuilder.uvs(4, 4, 12, 12).texture("#2");
                    }
                })
                .from(4, 3, 4).to(12, 6, 12)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(5, 0, 11, 2).texture("#1");
                        case UP -> faceBuilder.uvs(5, 5, 11, 11).texture("#2");
                        case DOWN -> faceBuilder.uvs(5, 5, 11, 11).texture("#0");
                    }
                })
                .from(4.999f, 18, 4.999f).to(11.001f, 19.999f, 11.001f)
                .end()
                .texture("particle", Utils.modBlockLoc("amphora/amphora_side"))
                .texture("0", Utils.modBlockLoc("amphora/amphora_base"))
                .texture("1", Utils.modBlockLoc("amphora/amphora_side"))
                .texture("2", Utils.modBlockLoc("amphora/amphora_top"));
        provider.simpleBlock(YTechBlocks.AMPHORA.get(), model);
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.AMPHORA)).parent(model);
    }
}
