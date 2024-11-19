package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.ToolRackBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ToolRackBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE_WEST = Shapes.box(0, 0, 0, 2/16.0, 1, 1);
    private static final VoxelShape SHAPE_EAST = Shapes.box(14/16.0, 0, 0, 1, 1, 1);
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(0, 0, 0, 1, 1, 2/16.0);
    private static final VoxelShape SHAPE_NORTH = Shapes.box(0, 0, 14/16.0, 1, 1, 1);
    public static final DirectionProperty HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public ToolRackBlock() {
        super(Properties.ofFullCopy(Blocks.OAK_PLANKS));
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return switch (pState.getValue(HORIZONTAL_FACING)) {
            case DOWN, UP -> throw new IllegalStateException();
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_EAST;
            case EAST -> SHAPE_WEST;
        };
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ToolRackBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ToolRackBlockEntity toolRackBlockEntity) {
            return toolRackBlockEntity.onUse(level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof ToolRackBlockEntity toolRackBlockEntity) {
                Containers.dropContents(level, pos, toolRackBlockEntity.getItems());
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(HORIZONTAL_FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(HORIZONTAL_FACING);
    }

    public static int getIndex(int @Nullable [] position) {
        if (validPosition(position)) {
            return position[1] * 4 + position[0];
        } else {
            return -1;
        }
    }

    public static int @Nullable [] getPosition(BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        Vec3 position = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

        if (hit.getDirection().getAxis() == Direction.Axis.X) {
            position = new Vec3(position.z, position.y, position.x);
        }

        if (hit.getDirection().getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
            position = new Vec3(1 - position.x, position.y, position.z);
        }

        int x = (int) Math.floor(position.x * 4);
        int y = (int) Math.floor(position.y * 4);

        if (x >= 0 && x < 4 && y >= 0 && y < 4) {
            return new int[]{x, y};
        } else {
            return null;
        }
    }

    public static int @Nullable [] getPosition(int index) {
        if (index >= 0 && index < 16) {
            int y = index / 4;
            index %= 4;
            int x = index;
            return new int[]{x, y};
        } else {
            return null;
        }
    }

    public static boolean validPosition(int @Nullable [] position) {
        return position != null && position[0] >= 0 && position[0] < 4 && position[1] >= 0 && position[1] < 4;
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.TOOL_RACK))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 2, 16).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 2).texture("#1");
                    }
                })
                .from(0, 0, 14).to(16, 16, 16)
                .end()
                .texture("particle", Utils.modBlockLoc("tool_rack/tool_rack_back"))
                .texture("0", Utils.modBlockLoc("tool_rack/tool_rack"))
                .texture("1", Utils.modBlockLoc("tool_rack/tool_rack_back"));
        provider.horizontalBlock(YTechBlocks.TOOL_RACK.get(), model);
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.TOOL_RACK)).parent(model);
    }
}
