package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.container.AqueductFertilizerMenu;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AqueductFertilizerBlockEntity extends AqueductHydratorBlockEntity implements MenuProvider, IMenuBlockEntity {
    private static final String TAG_FERTILIZER = "fertilizer";
    private static final String TAG_ITEMS = "items";

    @NotNull protected final MachineItemStackHandler itemStackHandler;
    @NotNull protected final ContainerData containerData;
    private int fertilizer = 0;

    public AqueductFertilizerBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(YTechBlockEntityTypes.AQUEDUCT_FERTILIZER.get(), pos, blockState);
        itemStackHandler = createItemStackHandler();
        containerData = createContainerData();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        fertilizer = tag.getInt(TAG_FERTILIZER);

        if (tag.contains(TAG_ITEMS)) {
            itemStackHandler.deserializeNBT(tag.getCompound(TAG_ITEMS));
        }
    }

    @Override
    public void tick(@NotNull ServerLevel level) {
        super.tick(level);

        if (isHydrating()) {
            if ((fertilizer == 0) && level.getGameTime() % 20 == 0) {
                if (useFertilizer()) {
                    fertilizer = YTechMod.CONFIGURATION.getFertilizerDuration();
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                }
            } else if (fertilizer > 0) {
                fertilizer--;

                if (fertilizer == 0) {
                    if (useFertilizer()) {
                        fertilizer = YTechMod.CONFIGURATION.getFertilizerDuration();
                        setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                    }
                } else {
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                }
            }

            if (fertilizer > 0 && level.random.nextInt(YTechMod.CONFIGURATION.getApplyFertilizerChance()) == 0) {
                int x = worldPosition.getX();
                int y = worldPosition.getY();
                int z = worldPosition.getZ();
                BlockPos randomPos = BlockPos.randomBetweenClosed(level.random, 1, x - 4, y, z - 4, x + 4, y, z + 4).iterator().next();

                if (randomPos != null) {
                    BlockState state = level.getBlockState(randomPos);

                    if (state.getBlock() instanceof FarmBlock) {
                        randomPos = randomPos.above();
                        state = level.getBlockState(randomPos); //apply to crop
                    }

                    if (state.getBlock() instanceof BonemealableBlock bonemealableBlock) {
                        if (bonemealableBlock.isValidBonemealTarget(level, randomPos, state)) {
                            if (bonemealableBlock.isBonemealSuccess(level, level.random, randomPos, state)) {
                                bonemealableBlock.performBonemeal(level, level.random, randomPos, state);
                                level.sendParticles(
                                        ParticleTypes.HAPPY_VILLAGER,
                                        randomPos.getX() + 0.5, randomPos.getY() + 0.5, randomPos.getZ() + 0.5,
                                        10,
                                        0, 0, 0,
                                        0
                                );
                            }
                        }
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ytech.aqueduct_fertilizer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AqueductFertilizerMenu(pContainerId, pPlayer, worldPosition, itemStackHandler, createContainerData());
    }

    @NotNull
    public MachineItemStackHandler getItemStackHandler() {
        return itemStackHandler;
    }

    @NotNull
    public  MachineItemStackHandler createItemStackHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(80, 32, (itemStackHandler, slot, itemStack) -> itemStack.is(YTechItemTags.FERTILIZER))
                .setOnChangeListener(this::setChanged)
                .build();
    }

    @NotNull
    @Override
    public ContainerData createContainerData() {
        return new SimpleContainerData(0);
    }

    @Override
    public int getDataSize() {
        return containerData.getCount();
    }

    public boolean isFertilizing() {
        return fertilizer > 0 && isHydrating();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt(TAG_FERTILIZER, fertilizer);
        tag.put(TAG_ITEMS, itemStackHandler.serializeNBT());
    }

    private boolean useFertilizer() {
        return !itemStackHandler.extractItem(0, 1, false).isEmpty();
    }
}
