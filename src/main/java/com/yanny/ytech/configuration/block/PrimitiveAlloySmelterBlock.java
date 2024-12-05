package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.PrimitiveAlloySmelterBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

public class PrimitiveAlloySmelterBlock extends AbstractPrimitiveMachineBlock {
    public PrimitiveAlloySmelterBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState pState) {
        return new PrimitiveAlloySmelterBlockEntity(pos, pState);
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ResourceLocation casing = Utils.modBlockLoc("bricks");
        ResourceLocation top = Utils.modBlockLoc("machine/primitive_smelter_top");
        ResourceLocation face = Utils.modBlockLoc("machine/primitive_alloy_smelter_front");
        ResourceLocation faceLit = Utils.modBlockLoc("machine/primitive_alloy_smelter_front_lit");
        BlockModelBuilder model = provider.models().cube(Utils.getPath(YTechBlocks.PRIMITIVE_ALLOY_SMELTER), casing, top, face, casing, casing, casing).texture("particle", casing);
        BlockModelBuilder modelLit = provider.models().cube(Utils.getPath(YTechBlocks.PRIMITIVE_ALLOY_SMELTER) + "_lit", casing, top, faceLit, casing, casing, casing).texture("particle", casing);

        provider.getVariantBuilder(YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(LIT, false).setModels(ConfiguredModel.builder().modelFile(model).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(LIT, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(LIT, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(LIT, false).setModels(ConfiguredModel.builder().modelFile(model).rotationY(270).build())
                .partialState().with(HORIZONTAL_FACING, Direction.NORTH).with(LIT, true).setModels(ConfiguredModel.builder().modelFile(modelLit).build())
                .partialState().with(HORIZONTAL_FACING, Direction.EAST).with(LIT, true).setModels(ConfiguredModel.builder().modelFile(modelLit).rotationY(90).build())
                .partialState().with(HORIZONTAL_FACING, Direction.SOUTH).with(LIT, true).setModels(ConfiguredModel.builder().modelFile(modelLit).rotationY(180).build())
                .partialState().with(HORIZONTAL_FACING, Direction.WEST).with(LIT, true).setModels(ConfiguredModel.builder().modelFile(modelLit).rotationY(270).build());
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.PRIMITIVE_ALLOY_SMELTER)).parent(model);
    }
}
