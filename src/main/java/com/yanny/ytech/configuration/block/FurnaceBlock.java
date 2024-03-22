package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.block_entity.FurnaceBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IEntityBlockHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class FurnaceBlock extends MachineBlock {
    public FurnaceBlock(Holder holder) {
        super(holder, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).requiresCorrectToolForDrops().strength(3.5F));
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        if (holder instanceof IEntityBlockHolder entityHolder) {
            return new FurnaceBlockEntity(holder, entityHolder.getBlockEntityType(), pos, blockState);
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    public static void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull RecipeOutput recipeConsumer) {
        //TODO
    }
}
