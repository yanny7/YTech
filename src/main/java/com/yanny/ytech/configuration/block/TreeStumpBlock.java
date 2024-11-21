package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.TreeStumpBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

public class TreeStumpBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE_BOTTOM = Shapes.box(0, 0, 0, 1, 4/16.0, 1);
    private static final VoxelShape SHAPE_TOP = Shapes.box(2/16.0, 4/16.0, 2/16.0, 14/16.0, 1, 14/16.0);
    private static final VoxelShape SHAPE = Shapes.or(SHAPE_BOTTOM, SHAPE_TOP);

    public TreeStumpBlock() {
        super(Properties.copy(Blocks.OAK_WOOD));
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
        return new TreeStumpBlockEntity(blockPos, blockState);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof TreeStumpBlockEntity treeStumpBlock) {
            return treeStumpBlock.onUse(state, level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof TreeStumpBlockEntity treeStumpBlock) {
                Containers.dropContents(level, pos, NonNullList.withSize(1, treeStumpBlock.getItem()));
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.TREE_STUMP))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 16, 4).texture("#1").cullface(direction);
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#1").cullface(direction);
                    }
                })
                .from(0, 0, 0).to(16, 4, 16)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 12, 12).texture("#2");
                        case UP -> faceBuilder.uvs(2, 2, 14, 14).texture("#3").cullface(direction);
                    }
                })
                .from(2, 4, 2).to(14, 16, 14)
                .end()
                .texture("particle", Utils.mcBlockLoc("oak_log"))
                .texture("1", Utils.mcBlockLoc("cobblestone"))
                .texture("2", Utils.mcBlockLoc("oak_log"))
                .texture("3", Utils.modBlockLoc("tree_stump_top"));
        provider.simpleBlock(YTechBlocks.TREE_STUMP.get(), model);
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.TREE_STUMP)).parent(model);
    }
}
