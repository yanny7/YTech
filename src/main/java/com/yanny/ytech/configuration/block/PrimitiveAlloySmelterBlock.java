package com.yanny.ytech.configuration.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.PrimitiveAlloySmelterBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IEntityBlockHolder;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
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

public class PrimitiveAlloySmelterBlock extends AbstractPrimitiveMachineBlock implements IProbeInfoProvider {
    public PrimitiveAlloySmelterBlock(Holder holder) {
        super(holder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState pState) {
        if (holder instanceof IEntityBlockHolder entityHolder) {
            return new PrimitiveAlloySmelterBlockEntity(holder, entityHolder.getEntityTypeRegistry().get(), pos, pState);
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(YTechMod.MOD_ID, getClass().getName());
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
        if (!level.isClientSide && level.getBlockEntity(probeHitData.getPos()) instanceof PrimitiveAlloySmelterBlockEntity blockEntity) {
            IProbeInfo verticalLayout = probeInfo.vertical();

            if (blockEntity.hasActiveRecipe()) {
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
        BlockModelBuilder model = provider.models().cube(holder.key, casing, top, face, casing, casing, casing).texture("particle", casing);
        BlockModelBuilder modelPowered = provider.models().cube(holder.key + "_powered", casing, top, facePowered, casing, casing, casing).texture("particle", casing);

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
                new TextureHolder(-1, -1, Utils.modBlockLoc("machine/primitive_smelter_top")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("machine/primitive_smelter_front")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("machine/primitive_smelter_front_powered"))
        ).toArray(TextureHolder[]::new);
    }
}
