package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AqueductFertilizerBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class AqueductFertilizerBlock extends AqueductHydratorBlock {
    public AqueductFertilizerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return new AqueductFertilizerBlockEntity(pos, blockState);
    }

    @NotNull
    @Override
    protected InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                               @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            player.openMenu(Objects.requireNonNull(getMenuProvider(state, level, pos)), pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof AqueductFertilizerBlockEntity blockEntity) {
                Containers.dropContents(level, pos, blockEntity.getItemStackHandler().getItems());
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ResourceLocation fertilizerTexture = Utils.modBlockLoc("aqueduct/aqueduct_fertilizer");
        ResourceLocation valveTexture = Utils.modBlockLoc("aqueduct/aqueduct_valve");
        ResourceLocation bricksTexture = Utils.modBlockLoc("terracotta_bricks");
        ResourceLocation invisibleTexture = Utils.modBlockLoc("invisible");
        ResourceLocation workingTexture = Utils.modBlockLoc("aqueduct/aqueduct_fertilizer_working");
        String name = Utils.getPath(YTechBlocks.AQUEDUCT_FERTILIZER);
        ModelFile base = provider.models().getBuilder(name)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#2").cullface(direction);
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0").cullface(direction);
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("0", fertilizerTexture)
                .texture("2", bricksTexture)
                .texture("particle", fertilizerTexture);
        ModelFile activated = provider.models().getBuilder(name + "_activated")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, WEST, SOUTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#2").cullface(direction);
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#4").cullface(direction);
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("2", bricksTexture)
                .texture("4", workingTexture)
                .texture("particle", fertilizerTexture);
        ModelFile overlay = provider.models().getBuilder(name + "_overlay")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    if (Objects.requireNonNull(direction) == Direction.NORTH) {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                    } else {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#3");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .renderType("minecraft:cutout")
                .texture("1", valveTexture)
                .texture("3", invisibleTexture);
        ModelFile inventory = provider.models().getBuilder(name + "_inventory")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("0", fertilizerTexture)
                .texture("1", valveTexture)
                .texture("particle", fertilizerTexture);

        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(YTechBlocks.AQUEDUCT_FERTILIZER.get());
        builder.part().modelFile(base).addModel().condition(AqueductConsumerBlock.ACTIVATED, false).end();
        builder.part().modelFile(activated).addModel().condition(AqueductConsumerBlock.ACTIVATED, true).end();
        PROPERTY_BY_DIRECTION.forEach((dir, value) -> builder.part().modelFile(overlay).rotationY(ANGLE_BY_DIRECTION.get(dir)).addModel().condition(value, true).end());

        provider.itemModels().getBuilder(name).parent(inventory);
    }
}
