package com.yanny.ytech.machine.block_entity;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.IMachineBlockEntity;
import com.yanny.ytech.machine.container.ContainerMenuFactory;
import com.yanny.ytech.machine.container.handler.MachineContainerHandler;
import com.yanny.ytech.machine.container.handler.MachineItemStackHandler;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBlockEntity extends BlockEntity implements IMachineBlockEntity, BlockEntityTicker<MachineBlockEntity>, MenuProvider {
    private static final String TAG_ITEMS = "items";

    protected final YTechConfigLoader.Machine machine;
    protected final YTechConfigLoader.Tier tier;

    protected final MachineContainerHandler containerHandler;
    protected final LazyOptional<IItemHandler> itemHandler;

    public MachineBlockEntity(BlockEntityType<? extends BlockEntity> blockEntityType, BlockPos pos, BlockState blockState, YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(blockEntityType, pos, blockState);
        this.machine = machine;
        this.tier = tier;
        this.containerHandler = getContainerHandler();
        itemHandler = LazyOptional.of(containerHandler::getPlayerItemStackHandler);

        LogUtils.getLogger().warn("Created MachineBlockEntity at {}", pos);
    }

    @Override
    public YTechConfigLoader.Tier getTier() {
        return tier;
    }

    @Override
    public void tick(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull MachineBlockEntity pBlockEntity) {

    }

    @NotNull
    @Override
    public Component getDisplayName() {
        return Component.translatable(Registration.REGISTRATION_HOLDER.machine().get(machine).get(tier).block().getId().toLanguageKey("block"));
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return ContainerMenuFactory.create(pContainerId, pPlayer, getBlockPos(), machine, tier);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_ITEMS)) {
            containerHandler.getMachineItemStackHandler().deserializeNBT(tag.getCompound(TAG_ITEMS));
        }
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER && side == null) {
            return itemHandler.cast();
        } else {
            return super.getCapability(cap, side);
        }
    }

    @NotNull
    public MachineItemStackHandler getPlayerItemStackHandler() {
        return containerHandler.getPlayerItemStackHandler();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put(TAG_ITEMS, containerHandler.getMachineItemStackHandler().serializeNBT());
    }

    @NotNull abstract protected MachineContainerHandler getContainerHandler();
}
