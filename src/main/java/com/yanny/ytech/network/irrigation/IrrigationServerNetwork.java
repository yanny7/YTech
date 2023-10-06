package com.yanny.ytech.network.irrigation;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.server.ServerNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.text.MessageFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IrrigationServerNetwork extends ServerNetwork<IrrigationServerNetwork, IIrrigationBlockEntity> {
    private static final String TAG_PROVIDERS = "providers";
    private static final String TAG_CONSUMERS = "consumers";
    private static final String TAG_STORAGES = "storages";
    private static final String TAG_BLOCK_POS = "pos";
    private static final String TAG_INFLOW = "inflow";
    private static final String TAG_OUTFLOW = "outflow";
    private static final String TAG_FLOW = "flow";
    private static final String TAG_FLUID_TANK = "fluidHolder";
    private static final Logger LOGGER = LogUtils.getLogger();

    @NotNull private final HashMap<BlockPos, Integer> providers = new HashMap<>();
    @NotNull private final HashMap<BlockPos, Integer> consumers = new HashMap<>();
    @NotNull private final Set<BlockPos> storages = new HashSet<>();
    @NotNull private final FluidTank fluidHandler;
    private int inflow = 0;
    private int outflow = 0;

    public IrrigationServerNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange, @NotNull Consumer<Integer> onRemove) {
        super(networkId, onChange, onRemove);
        fluidHandler = createFluidTank(networkId);
        load(tag);
    }

    public IrrigationServerNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull Consumer<Integer> onRemove) {
        super(networkId, onChange, onRemove);
        fluidHandler = createFluidTank(networkId);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Id:{0,number}, C:{1,number}, P:{2,number}, S: {3,number} {4,number}/{5,number}, {6,number}/{7,number}",
                getNetworkId(), consumers.size(), providers.size(), storages.size(), inflow, outflow, fluidHandler.getFluidAmount(), fluidHandler.getCapacity());
    }

    @Override
    public boolean canAttach(@NotNull IIrrigationBlockEntity entity) {
        return true;
    }

    @Override
    public boolean canAttach(@NotNull IrrigationServerNetwork network) {
        return true;
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        if (tag.contains(TAG_PROVIDERS) && tag.getTagType(TAG_PROVIDERS) != 0) {
            tag.getList(TAG_PROVIDERS, ListTag.TAG_COMPOUND).forEach((t) ->
                    providers.put(NetworkUtils.loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS)), ((CompoundTag) t).getInt(TAG_FLOW)));
        }
        if (tag.contains(TAG_CONSUMERS) && tag.getTagType(TAG_CONSUMERS) != 0) {
            tag.getList(TAG_CONSUMERS, ListTag.TAG_COMPOUND).forEach((t) ->
                    consumers.put(NetworkUtils.loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS)), ((CompoundTag) t).getInt(TAG_FLOW)));
        }
        if (tag.contains(TAG_STORAGES) && tag.getTagType(TAG_STORAGES) != 0) {
            tag.getList(TAG_STORAGES, ListTag.TAG_COMPOUND).forEach((t) -> storages.add(NetworkUtils.loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS))));
        }
        if (tag.contains(TAG_INFLOW) && tag.getTagType(TAG_INFLOW) != 0) {
            inflow = tag.getInt(TAG_INFLOW);
        }
        if (tag.contains(TAG_OUTFLOW) && tag.getTagType(TAG_OUTFLOW) != 0) {
            inflow = tag.getInt(TAG_OUTFLOW);
        }
        if (tag.contains(TAG_FLUID_TANK) && tag.getTagType(TAG_FLUID_TANK) != 0) {
            fluidHandler.setCapacity(storages.size() * YTechMod.CONFIGURATION.getBaseFluidStoragePerBlock());
            fluidHandler.readFromNBT(tag.getCompound(TAG_FLUID_TANK));
        }

        LOGGER.debug("Network {}: {}", getNetworkId(), this);
    }

    @NotNull
    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        ListTag providersTag = new ListTag();
        ListTag consumersTag = new ListTag();
        ListTag storagesTag = new ListTag();

        providers.forEach((pos, stress) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(pos));
            t.putInt(TAG_FLOW, stress);
            providersTag.add(t);
        });
        consumers.forEach((pos, stress) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(pos));
            t.putInt(TAG_FLOW, stress);
            consumersTag.add(t);
        });
        storages.forEach((pos) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(pos));
            storagesTag.add(t);
        });
        tag.put(TAG_PROVIDERS, providersTag);
        tag.put(TAG_CONSUMERS, consumersTag);
        tag.put(TAG_STORAGES, storagesTag);
        tag.putInt(TAG_INFLOW, inflow);
        tag.putInt(TAG_OUTFLOW, outflow);
        tag.put(TAG_FLUID_TANK, fluidHandler.writeToNBT(new CompoundTag()));
        return tag;
    }

    @Override
    public void addAll(@NotNull IrrigationServerNetwork network, @NotNull Level level) {
        network.providers.forEach((pos, value) -> {
            providers.put(pos, value);

            if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity irrigationBlockEntity) {
                irrigationBlockEntity.setNetworkId(getNetworkId());
            }
        });
        network.consumers.forEach((pos, value) -> {
            consumers.put(pos, value);

            if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity irrigationBlockEntity) {
                irrigationBlockEntity.setNetworkId(getNetworkId());
            }
        });
        network.storages.forEach((pos) -> {
            storages.add(pos);

            if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity irrigationBlockEntity) {
                irrigationBlockEntity.setNetworkId(getNetworkId());
            }
        });

        inflow += network.inflow;
        outflow += network.outflow;
        fluidHandler.setCapacity(fluidHandler.getCapacity() + network.fluidHandler.getCapacity());
        fluidHandler.fill(network.fluidHandler.getFluid(), IFluidHandler.FluidAction.EXECUTE);
    }

    @Override
    public boolean update(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        boolean wasChange = false;
        int value = entity.getFlow();

        switch (entity.getNetworkType()) {
            case PROVIDER -> {
                int oldCapacity = inflow;

                inflow = inflow - providers.get(blockPos) + value;
                providers.put(blockPos, value);

                if (oldCapacity != inflow) {
                    wasChange = true;
                }
            }
            case CONSUMER -> {
                int oldStress = outflow;

                outflow = outflow - providers.get(blockPos) + value;
                consumers.put(blockPos, value);

                if (oldStress != outflow) {
                    wasChange = true;
                }
            }
        }

        return wasChange;
    }

    @NotNull
    @Override
    public List<IrrigationServerNetwork> remove(@NotNull Function<Integer, List<Integer>> idsGetter, @NotNull Consumer<Integer> onRemove, @NotNull IIrrigationBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        Map<BlockPos, Integer> providerBlocks = new HashMap<>(providers);
        Map<BlockPos, Integer> consumerBlocks = new HashMap<>(consumers);
        Set<BlockPos> storageBlocks = new HashSet<>(storages);
        BlockPos blockPos = blockEntity.getBlockPos();
        double fluidPerBlock = storageBlockCount() > 0 ? fluidHandler.getFluidAmount() / (double)storageBlockCount() : 0;

        // remove splitting block
        providerBlocks.remove(blockPos);
        consumerBlocks.remove(blockPos);
        storageBlocks.remove(blockPos);
        remove(blockEntity);

        List<BlockPos> neighbors = blockEntity.getValidNeighbors().stream().filter(pos -> providerBlocks.containsKey(pos) || consumerBlocks.containsKey(pos) || storageBlocks.contains(pos)).collect(Collectors.toList());

        if ((neighbors.size() == 1) || (providerBlocks.isEmpty() && consumerBlocks.isEmpty() && storageBlocks.isEmpty()) || level == null) { // if we are not splitting
            fluidHandler.setFluid(new FluidStack(Fluids.WATER, (int) (storageBlockCount() * fluidPerBlock)));
            return List.of();
        }

        List<Integer> ids = idsGetter.apply(neighbors.size() - 1);
        BlockPos neighbor = neighbors.remove(0); // remove first network (will be our network)

        clear();
        insertConnectedPositions(this, providerBlocks, consumerBlocks, storageBlocks, neighbor, level); // re-insert blocks
        fluidHandler.setFluid(new FluidStack(Fluids.WATER, (int) (storageBlockCount() * fluidPerBlock)));

        return neighbors.stream()
                .map((pos) -> {
                    if ((providerBlocks.isEmpty() && consumerBlocks.isEmpty() && storageBlocks.isEmpty())
                            || (!providerBlocks.containsKey(pos) && !consumerBlocks.containsKey(pos) && !storageBlocks.contains(pos))) {
                        return null;
                    }

                    IrrigationServerNetwork network = new IrrigationServerNetwork(ids.remove(0), onChange, onRemove);
                    insertConnectedPositions(network, providerBlocks, consumerBlocks, storageBlocks, pos, level);
                    return network;
                })
                .filter(Objects::nonNull)
                .peek((n) -> n.fluidHandler.setFluid(new FluidStack(Fluids.WATER, (int) (n.storageBlockCount() * fluidPerBlock))))
                .toList();
    }

    @Override
    public boolean isNotEmpty() {
        return !providers.isEmpty() || !consumers.isEmpty() || !storages.isEmpty();
    }

    @Override
    protected boolean isValidPosition(@NotNull IIrrigationBlockEntity blockEntity, @NotNull BlockPos pos) {
        if (providers.containsKey(pos) || consumers.containsKey(pos) || storages.contains(pos)) {
            Level level = blockEntity.getLevel();

            if (level != null && level.isLoaded(pos)) {
                if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity irrigationBlockEntity) {
                    return irrigationBlockEntity.getValidNeighbors().contains(blockEntity.getBlockPos());
                }
            }
        }

        return false;
    }

    @Override
    public void add(@NotNull IIrrigationBlockEntity entity) {
        super.add(entity);

        switch (entity.getNetworkType()) {
            case CONSUMER -> addConsumer(entity);
            case PROVIDER -> addProvider(entity);
            case STORAGE -> addStorage(entity);
        }
    }

    @Override
    public void remove(@NotNull IIrrigationBlockEntity entity) {
        switch (entity.getNetworkType()) {
            case CONSUMER -> removeConsumer(entity);
            case PROVIDER -> removeProvider(entity);
            case STORAGE -> removeStorage(entity);
        }

        super.remove(entity);
    }

    @NotNull
    public FluidTank getFluidHandler() {
        return fluidHandler;
    }

    public int storageBlockCount() {
        return storages.size();
    }

    private void addProvider(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        int flow = entity.getFlow();

        providers.put(blockPos, flow);
        inflow += flow;
    }

    private void addConsumer(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        int flow = entity.getFlow();

        consumers.put(blockPos, flow);
        outflow += flow;
    }

    private void addStorage(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();

        storages.add(blockPos);
        fluidHandler.setCapacity(YTechMod.CONFIGURATION.getBaseFluidStoragePerBlock() * storages.size());
    }

    private void removeProvider(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        int value = providers.remove(blockPos);

        entity.setNetworkId(-1);
        inflow -= value;
    }

    private void removeConsumer(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        int value = consumers.remove(blockPos);

        entity.setNetworkId(-1);
        outflow -= value;
    }

    private void removeStorage(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();

        storages.remove(blockPos);
        fluidHandler.setCapacity(YTechMod.CONFIGURATION.getBaseFluidStoragePerBlock() * storages.size());
    }

    private void clear() {
        consumers.clear();
        providers.clear();
        storages.clear();
        inflow = 0;
        outflow = 0;
        fluidHandler.setCapacity(0);
    }

    private FluidTank createFluidTank(int networkId) {
        return new FluidTank(0, (fluid) -> fluid.getFluid().isSame(Fluids.WATER)) {
            @Override
            protected void onContentsChanged() {
                onChange.accept(networkId);
            }
        };
    }

    private static void insertConnectedPositions(@NotNull IrrigationServerNetwork network, @NotNull Map<BlockPos, Integer> providerBlocks,
                                                 @NotNull Map<BlockPos, Integer> consumerBlocks, @NotNull Set<BlockPos> storageBlocks,
                                                 @NotNull BlockPos from, @NotNull Level level) {
        BlockEntity blockEntity = level.getBlockEntity(from);

        if (blockEntity instanceof IIrrigationBlockEntity block) {
            network.add(block);
            providerBlocks.remove(from);
            consumerBlocks.remove(from);
            storageBlocks.remove(from);
            block.getValidNeighbors().forEach((pos) -> {
                if (providerBlocks.containsKey(pos) || consumerBlocks.containsKey(pos) || storageBlocks.contains(pos)) {
                    insertConnectedPositions(network, providerBlocks, consumerBlocks, storageBlocks, pos, level);
                }
            });
        }
    }
}
