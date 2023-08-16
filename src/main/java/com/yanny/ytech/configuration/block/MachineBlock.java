package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.MachineBlockEntity;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public abstract class MachineBlock extends BaseEntityBlock {
    protected final Holder holder;

    public MachineBlock(Holder holder) {
        super(BlockBehaviour.Properties.of());
        this.holder = holder;
    }

    @NotNull
    public RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> entityType) {
        return level.isClientSide ? null : createTickerHelper(entityType, entityType, MachineBlock::createMachineTicker);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.POWERED);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        return defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getOpposite())
                .setValue(BlockStateProperties.POWERED, false);
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult trace) {
        if (!level.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, getMenuProvider(state, level, pos), pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public static void createMachineTicker(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof MachineBlockEntity block) {
            block.tick(level, pos, state, block);
        }
    }

    public static void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ResourceLocation casing = textures[0];
        ResourceLocation face = textures[1];
        ResourceLocation facePowered = textures[2];
        BlockModelBuilder model = provider.models().orientableWithBottom(holder.key, casing, face, casing, casing);
        BlockModelBuilder modelPowered = provider.models().orientableWithBottom(holder.key + "_powered", casing, facePowered, casing, casing);

        model.element().allFaces((direction, faceBuilder) -> {
            switch (direction) {
                case UP -> faceBuilder.texture("#top").uvs(0, 0, 8, 16).cullface(direction);
                case DOWN -> faceBuilder.texture("#bottom").uvs(0, 0, 8, 16).cullface(direction);
                case SOUTH, WEST, EAST -> faceBuilder.texture("#side").uvs(8, 0, 16, 16).cullface(direction);
                case NORTH -> faceBuilder.texture("#front").cullface(direction);
            }
        });
        modelPowered.element().allFaces((direction, faceBuilder) -> {
            switch (direction) {
                case UP -> faceBuilder.texture("#top").uvs(0, 0, 8, 16).cullface(direction);
                case DOWN -> faceBuilder.texture("#bottom").uvs(0, 0, 8, 16).cullface(direction);
                case SOUTH, WEST, EAST -> faceBuilder.texture("#side").uvs(8, 0, 16, 16).cullface(direction);
                case NORTH -> faceBuilder.texture("#front").cullface(direction);
            }
        });
        provider.getVariantBuilder(holder.block.get())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(POWERED, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(270).build())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(POWERED, true).setModels(ConfiguredModel.builder().modelFile(modelPowered).rotationY(270).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    public static TextureHolder[] getTexture(String tierName, String machine) {
        return List.of(
                new TextureHolder(-1, -1, Utils.modBlockLoc("casing/" + tierName)),
                new TextureHolder(-1, -1, Utils.modBlockLoc("machine/" + tierName + "_" + machine)),
                new TextureHolder(-1, -1, Utils.modBlockLoc("machine/" + tierName + "_" + machine + "_powered"))
        ).toArray(TextureHolder[]::new);
    }
}
