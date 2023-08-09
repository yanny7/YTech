package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.block_entity.CrusherBlockEntity;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.IEntityBlockHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class CrusherBlock extends MachineBlock {
    public CrusherBlock(Holder holder) {
        super(holder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        if (holder instanceof IEntityBlockHolder entityHolder) {
            return new CrusherBlockEntity(holder, entityHolder.getEntityTypeRegistry().get(), pPos, pState);
        } else {
            throw new IllegalStateException("Invalid holder type");
        }
    }

    public static void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        //TODO
    }
}
