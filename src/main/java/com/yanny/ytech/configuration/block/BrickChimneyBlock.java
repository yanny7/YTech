package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.BrickChimneyBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BrickChimneyBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.box(3/16.0, 0, 3/16.0, 13/16.0, 1, 13/16.0);

    public BrickChimneyBlock(Properties properties) {
        super(properties.strength(2.0f, 2.0f));
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState blockState) {
        return true;
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BrickChimneyBlockEntity(pos, state);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!level.isClientSide && !state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof BrickChimneyBlockEntity blockEntity) {
            blockEntity.onRemove();
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.BRICK_CHIMNEY))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(3, 0, 13, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(3, 3, 13, 13).texture("#1").cullface(direction);
                    }
                })
                .from(3, 0, 3).to(13, 16, 13).end()
                .texture("particle", Utils.modBlockLoc("bricks"))
                .texture("0", Utils.modBlockLoc("bricks"))
                .texture("1", Utils.modBlockLoc("machine/primitive_smelter_top"));
        provider.getVariantBuilder(YTechBlocks.BRICK_CHIMNEY.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.BRICK_CHIMNEY)).parent(model);
    }
}
