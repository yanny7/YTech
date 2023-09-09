package com.yanny.ytech.configuration.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.PrimitiveSmelterBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IEntityBlockHolder;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public class PrimitiveSmelterBlock extends MachineBlock implements IProbeInfoProvider {
    public PrimitiveSmelterBlock(Holder holder) {
        super(holder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        if (holder instanceof IEntityBlockHolder entityHolder) {
            return new PrimitiveSmelterBlockEntity(holder, entityHolder.getEntityTypeRegistry().get(), pPos, pState);
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(POWERED) ? 15 : 0;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof PrimitiveSmelterBlockEntity smelter) {
                NonNullList<ItemStack> items = NonNullList.withSize(smelter.getItemStackHandler().getSlots(), ItemStack.EMPTY);

                for (int index = 0; index < smelter.getItemStackHandler().getSlots(); index++) {
                    items.set(index, smelter.getItemStackHandler().getStackInSlot(index));
                }

                Containers.dropContents(level, pos, items);
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
        if (!level.isClientSide && level.getBlockEntity(probeHitData.getPos()) instanceof PrimitiveSmelterBlockEntity blockEntity) {
            IProbeInfo verticalLayout = probeInfo.vertical();
            ItemStack item = blockEntity.processingItem();

            if (blockEntity.isProcessing() && item != null) {
                verticalLayout.horizontal().text("Processing ").text(item.getDisplayName());
                verticalLayout.horizontal().text("Progress: ").horizontal().text(Integer.toString(blockEntity.progress())).text("%");
            }

            verticalLayout.horizontal().text("Temperature: ").horizontal().text(Integer.toString(blockEntity.temperature())).text("°C");
        }
    }

    public static void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        //TODO
    }

    public static void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ResourceLocation casing = textures[0];
        ResourceLocation top = textures[1];
        ResourceLocation face = textures[2];
        ResourceLocation facePowered = textures[3];
        BlockModelBuilder model = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 3, 16, 13).texture("#1");
                        case EAST -> faceBuilder.uvs(0, 3, 16, 13).texture("#0");
                        case SOUTH -> faceBuilder.uvs(0, 3, 16, 13).texture("#0");
                        case WEST -> faceBuilder.uvs(0, 3, 16, 13).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 10, 16).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case UP -> faceBuilder.uvs(1, 1, 15, 15).texture("#0");
                    }
                })
                .from(1, 10, 1).to(15, 13, 15).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case UP -> faceBuilder.uvs(2, 2, 14, 14).texture("#2");
                    }
                })
                .from(2, 13, 2).to(14, 16, 14).end()
                .texture("0", casing)
                .texture("1", face)
                .texture("2", top);
        BlockModelBuilder modelPowered = provider.models().getBuilder(holder.key + "_powered")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 3, 16, 13).texture("#1");
                        case EAST -> faceBuilder.uvs(0, 3, 16, 13).texture("#0");
                        case SOUTH -> faceBuilder.uvs(0, 3, 16, 13).texture("#0");
                        case WEST -> faceBuilder.uvs(0, 3, 16, 13).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 10, 16).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(1, 1, 15, 4).texture("#0");
                        case UP -> faceBuilder.uvs(1, 1, 15, 15).texture("#0");
                    }
                })
                .from(1, 10, 1).to(15, 13, 15).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(2, 1, 14, 4).texture("#0");
                        case UP -> faceBuilder.uvs(2, 2, 14, 14).texture("#2");
                    }
                })
                .from(2, 13, 2).to(14, 16, 14).end()
                .texture("0", casing)
                .texture("1", facePowered)
                .texture("2", top);
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

    public static TextureHolder[] textureHolder() {
        return List.of(
                new TextureHolder(-1, -1, Utils.mcBlockLoc("bricks")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("primitive_furnace_top")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("primitive_furnace_front")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("primitive_furnace_front_powered"))
        ).toArray(TextureHolder[]::new);
    }
}