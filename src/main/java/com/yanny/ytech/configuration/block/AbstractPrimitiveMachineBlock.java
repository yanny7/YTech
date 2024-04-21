package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.block_entity.AbstractPrimitiveMachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.POWERED;

public abstract class AbstractPrimitiveMachineBlock extends MachineBlock {
    public AbstractPrimitiveMachineBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_RED)
                .requiresCorrectToolForDrops()
                .strength(3.5F)
                .lightLevel((state) -> state.getValue(POWERED) ? 13 : 0));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof AbstractPrimitiveMachineBlockEntity smelter) {
                NonNullList<ItemStack> items = NonNullList.withSize(smelter.getItemStackHandler().getSlots(), ItemStack.EMPTY);

                for (int index = 0; index < smelter.getItemStackHandler().getSlots(); index++) {
                    items.set(index, smelter.getItemStackHandler().getStackInSlot(index));
                }

                Containers.dropContents(level, pos, items);
                smelter.onRemove();
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(POWERED)) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE,
                        SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
            }
        }
    }

    @Override
    public boolean hasClientTicker() {
        return true;
    }
}
