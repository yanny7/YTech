package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

public class ReinforcedBrickChimneyBlock extends BrickChimneyBlock {
    private static final VoxelShape SHAPE = Shapes.box(1/16.0, 0, 1/16.0, 15/16.0, 1, 15/16.0);

    public ReinforcedBrickChimneyBlock(Properties properties) {
        super(properties);
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.REINFORCED_BRICK_CHIMNEY))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(1, 0, 15, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(1, 1, 15, 15).texture("#1").cullface(direction);
                    }
                })
                .from(1, 0, 1).to(15, 16, 15).end()
                .texture("particle", Utils.modBlockLoc("reinforced_bricks"))
                .texture("0", Utils.modBlockLoc("reinforced_bricks"))
                .texture("1", Utils.modBlockLoc("machine/primitive_smelter_top"));
        provider.getVariantBuilder(YTechBlocks.REINFORCED_BRICK_CHIMNEY.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.REINFORCED_BRICK_CHIMNEY)).parent(model);
    }
}
