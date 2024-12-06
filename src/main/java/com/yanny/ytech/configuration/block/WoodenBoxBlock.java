package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.WoodenBoxBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WoodenBoxBlock extends Block implements EntityBlock {
    public static final VoxelShape BOX = Shapes.box(0, 0, 0, 1/4.0, 1/4.0, 1/4.0);
    private static final VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, 12/16.0, 1);
    public static final EnumProperty<Direction> HORIZONTAL_FACING = BlockStateProperties.HORIZONTAL_FACING;

    public WoodenBoxBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new WoodenBoxBlockEntity(blockPos, blockState);
    }

    @NotNull
    @Override
    public InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                       @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof WoodenBoxBlockEntity WoodenBoxBlockEntity) {
            return WoodenBoxBlockEntity.onUse(level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof WoodenBoxBlockEntity woodenBoxBlockEntity) {
                Containers.dropContents(level, pos, woodenBoxBlockEntity.getItems());
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
            return position[1] * 3 + position[0];
        } else {
            return -1;
        }
    }

    public static int @Nullable [] getPosition(BlockHitResult hit) {
        BlockPos pos = hit.getBlockPos();
        Vec3 position = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());

        if (position.x < 1/8.0 || position.x > 7/8.0 || position.z < 1/8.0 || position.z > 7/8.0) {
            return null;
        }

        position.subtract(1/8.0, 0, 1/8.0);
        position.multiply(16/12.0, 1, 16/12.0);

        int x = (int) Math.floor(position.x * 3);
        int z = (int) Math.floor(position.z * 3);

        if (x >= 0 && x < 3 && z >= 0 && z < 3) {
            return new int[]{x, z};
        } else {
            return null;
        }
    }

    public static int @Nullable [] getPosition(int index) {
        if (index >= 0 && index < 9) {
            int z = index / 3;
            index %= 3;
            int x = index;
            return new int[]{x, z};
        } else {
            return null;
        }
    }

    public static boolean validPosition(int @Nullable [] position) {
        return position != null && position[0] >= 0 && position[0] < 3 && position[1] >= 0 && position[1] < 3;
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.WOODEN_BOX))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(0, 10, 16, 16).texture("#1").cullface(direction);
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#1").cullface(direction);
                    }
                })
                .from(0, 0, 0).to(16, 6, 16)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 4, 16, 10).texture("#1").cullface(direction);
                        case SOUTH -> faceBuilder.uvs(0, 4, 16, 10).texture("#1");
                        case EAST -> faceBuilder.uvs(14, 4, 16, 10).texture("#1").cullface(direction);
                        case WEST -> faceBuilder.uvs(0, 4, 2, 10).texture("#1").cullface(direction);
                        case UP -> faceBuilder.uvs(0, 1, 16, 3).texture("#0");
                    }
                })
                .from(0, 6, 0).to(16, 12, 2)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 4, 16, 10).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 4, 16, 10).texture("#1").cullface(direction);
                        case EAST -> faceBuilder.uvs(0, 4, 2, 10).texture("#1").cullface(direction);
                        case WEST -> faceBuilder.uvs(14, 4, 16, 10).texture("#1").cullface(direction);
                        case UP -> faceBuilder.uvs(0, 13, 16, 15).texture("#0");
                    }
                })
                .from(0, 6, 14).to(16, 12, 16)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case EAST -> faceBuilder.uvs(2, 4, 14, 10).texture("#1");
                        case WEST -> faceBuilder.uvs(2, 4, 14, 10).texture("#1").cullface(direction);
                        case UP -> faceBuilder.uvs(0, 1, 12, 3).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#0");
                    }
                })
                .from(0, 6, 2).to(2, 12, 14)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case WEST -> faceBuilder.uvs(2, 4, 14, 10).texture("#1");
                        case EAST -> faceBuilder.uvs(2, 4, 14, 10).texture("#1").cullface(direction);
                        case UP -> faceBuilder.uvs(1, 12, 13, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#0");
                    }
                })
                .from(14, 6, 2).to(16, 12, 14)
                .end()
                .texture("particle", Utils.modBlockLoc("wooden_box"))
                .texture("0", Utils.mcBlockLoc("oak_planks"))
                .texture("1", Utils.modBlockLoc("wooden_box"));
        provider.getVariantBuilder(YTechBlocks.WOODEN_BOX.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.WOODEN_BOX)).parent(model);
    }
}
