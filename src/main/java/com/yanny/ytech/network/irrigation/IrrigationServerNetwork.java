package com.yanny.ytech.network.irrigation;

import com.mojang.logging.LogUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.generic.server.ServerNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class IrrigationServerNetwork extends ServerNetwork<IrrigationServerNetwork, IIrrigationBlockEntity> {
    private static final String TAG_PROVIDERS = "providers";
    private static final String TAG_CONSUMERS = "consumers";
    private static final String TAG_STORAGES = "storages";
    private static final String TAG_FILLED_BY_RAIN = "filledByRain";
    private static final String TAG_BLOCK_POS = "pos";
    private static final String TAG_FLOW = "flow";
    private static final String TAG_FLUID_TANK = "fluidHolder";
    private static final String TAG_CAPACITY = "capacity";
    private static final Logger LOGGER = LogUtils.getLogger();

    @NotNull private final HashMap<BlockPos, Integer> providers = new HashMap<>();
    @NotNull private final Set<BlockPos> consumers = new HashSet<>();
    @NotNull private final HashMap<BlockPos, Integer> storages = new HashMap<>();
    @NotNull private final Set<BlockPos> filledByRain = new HashSet<>();
    @NotNull private final FluidTank fluidHandler;
    private int inflow = 0;

    public IrrigationServerNetwork(@NotNull CompoundTag tag, int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
        super(networkId, onChange, onRemove);
        fluidHandler = createFluidTank(networkId);
        load(tag);
    }

    public IrrigationServerNetwork(int networkId, @NotNull Consumer<Integer> onChange, @NotNull BiConsumer<Integer, ChunkPos> onRemove) {
        super(networkId, onChange, onRemove);
        fluidHandler = createFluidTank(networkId);
    }

    @Override
    public String toString() {
        return MessageFormat.format("Id:{0,number}, C:{1,number}, P:{2,number}, S:{3,number}, Y:{4,number} {5,number}, {6,number}/{7,number}",
                getNetworkId(), consumers.size(), providers.size(), storages.size(), filledByRain.size(), inflow, fluidHandler.getFluidAmount(), fluidHandler.getCapacity());
    }

    @Override
    protected boolean canAttach(@NotNull IIrrigationBlockEntity blockEntity) {
        return true;
    }

    @Override
    protected boolean canAttach(@NotNull IrrigationServerNetwork network) {
        return true;
    }

    @Override
    protected void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_PROVIDERS) && tag.getTagType(TAG_PROVIDERS) != 0) {
            tag.getList(TAG_PROVIDERS, ListTag.TAG_COMPOUND).forEach((t) ->
                    providers.put(NetworkUtils.loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS)), ((CompoundTag) t).getInt(TAG_FLOW)));
        }
        if (tag.contains(TAG_CONSUMERS) && tag.getTagType(TAG_CONSUMERS) != 0) {
            tag.getList(TAG_CONSUMERS, ListTag.TAG_COMPOUND).forEach((t) -> consumers.add(NetworkUtils.loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS))));
        }
        if (tag.contains(TAG_STORAGES) && tag.getTagType(TAG_STORAGES) != 0) {
            tag.getList(TAG_STORAGES, ListTag.TAG_COMPOUND).forEach((t) ->
                    storages.put(NetworkUtils.loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS)), ((CompoundTag) t).getInt(TAG_CAPACITY)));
        }
        if (tag.contains(TAG_FILLED_BY_RAIN) && tag.getTagType(TAG_FILLED_BY_RAIN) != 0) {
            tag.getList(TAG_FILLED_BY_RAIN, ListTag.TAG_COMPOUND).forEach((t) -> filledByRain.add(NetworkUtils.loadBlockPos(((CompoundTag) t).getCompound(TAG_BLOCK_POS))));
        }
        if (tag.contains(TAG_FLUID_TANK) && tag.getTagType(TAG_FLUID_TANK) != 0) {
            fluidHandler.setCapacity(storages.values().stream().mapToInt(Integer::intValue).sum());
            fluidHandler.readFromNBT(tag.getCompound(TAG_FLUID_TANK));
        }

        inflow = providers.values().stream().mapToInt(Integer::intValue).sum();
        LOGGER.debug("Network {}: {}", getNetworkId(), this);
    }

    @NotNull
    @Override
    protected CompoundTag save() {
        CompoundTag tag = super.save();
        ListTag providersTag = new ListTag();
        ListTag consumersTag = new ListTag();
        ListTag storagesTag = new ListTag();
        ListTag filledByRainTag = new ListTag();

        providers.forEach((pos, flow) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(pos));
            t.putInt(TAG_FLOW, flow);
            providersTag.add(t);
        });
        consumers.forEach((pos) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(pos));
            consumersTag.add(t);
        });
        storages.forEach((pos, capacity) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(pos));
            t.putInt(TAG_CAPACITY, capacity);
            storagesTag.add(t);
        });
        filledByRain.forEach((pos) -> {
            CompoundTag t = new CompoundTag();
            t.put(TAG_BLOCK_POS, NetworkUtils.saveBlockPos(pos));
            filledByRainTag.add(t);
        });
        tag.put(TAG_PROVIDERS, providersTag);
        tag.put(TAG_CONSUMERS, consumersTag);
        tag.put(TAG_STORAGES, storagesTag);
        tag.put(TAG_FILLED_BY_RAIN, filledByRainTag);
        tag.put(TAG_FLUID_TANK, fluidHandler.writeToNBT(new CompoundTag()));
        return tag;
    }

    @Override
    protected void appendNetwork(@NotNull IrrigationServerNetwork network, @NotNull Level level) {
        network.providers.forEach((pos, value) -> {
            providers.put(pos, value);

            if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity irrigationBlockEntity) {
                addBlockEntity(irrigationBlockEntity);
            }
        });
        network.consumers.forEach((pos) -> {
            consumers.add(pos);

            if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity irrigationBlockEntity) {
                addBlockEntity(irrigationBlockEntity);
            }
        });
        network.storages.forEach((pos, value) -> {
            storages.put(pos, value);

            if (level.getBlockEntity(pos) instanceof IIrrigationBlockEntity irrigationBlockEntity) {
                addBlockEntity(irrigationBlockEntity);
            }
        });
        filledByRain.addAll(network.filledByRain);

        inflow += network.inflow;
        fluidHandler.setCapacity(fluidHandler.getCapacity() + network.fluidHandler.getCapacity());
        fluidHandler.setFluid(new FluidStack(Fluids.WATER, fluidHandler.getFluidAmount() + network.fluidHandler.getFluidAmount()));
    }

    @Override
    protected boolean updateBlockEntity(@NotNull IIrrigationBlockEntity blockEntity) {
        BlockPos blockPos = blockEntity.getBlockPos();
        boolean wasChange = false;

        switch (blockEntity.getNetworkType()) {
            case PROVIDER -> {
                int oldCapacity = inflow;
                int value = blockEntity.getFlow();

                providers.put(blockPos, value);
                inflow = providers.values().stream().mapToInt(Integer::intValue).sum();

                if (oldCapacity != inflow) {
                    wasChange = true;
                }
            }
            case CONSUMER -> consumers.add(blockPos);
            case STORAGE -> {
                boolean oldValue = filledByRain.contains(blockPos);
                boolean rainFilling = blockEntity.validForRainFilling();

                if (oldValue != rainFilling) {
                    if (rainFilling) {
                        filledByRain.add(blockPos);
                    } else {
                        filledByRain.remove(blockPos);
                    }

                    wasChange = true;
                }
            }
        }

        return wasChange;
    }

    @NotNull
    @Override
    protected List<IrrigationServerNetwork> removeBlockEntity(@NotNull Function<Integer, List<Integer>> idsGetter, @NotNull BiConsumer<Integer, ChunkPos> onRemove,
                                                              @NotNull IIrrigationBlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        Map<BlockPos, Integer> providerBlocks = new HashMap<>(providers);
        Set<BlockPos> consumerBlocks = new HashSet<>(consumers);
        Map<BlockPos, Integer> storageBlocks = new HashMap<>(storages);
        Set<BlockPos> filledByRainBlocks = new HashSet<>(filledByRain);
        BlockPos blockPos = blockEntity.getBlockPos();
        double fluidPerBlock = storageBlockCount() > 0 ? fluidHandler.getFluidAmount() / (double)storageBlockCount() : 0;

        // remove splitting block
        providerBlocks.remove(blockPos);
        consumerBlocks.remove(blockPos);
        storageBlocks.remove(blockPos);
        filledByRainBlocks.remove(blockPos);
        removeBlockEntity(blockEntity);

        List<BlockPos> neighbors = blockEntity.getValidNeighbors().stream().filter(pos -> providerBlocks.containsKey(pos) || consumerBlocks.contains(pos) || storageBlocks.containsKey(pos)).collect(Collectors.toList());

        if ((neighbors.size() == 1) || (providerBlocks.isEmpty() && consumerBlocks.isEmpty() && storageBlocks.isEmpty()) || level == null) { // if we are not splitting
            fluidHandler.setFluid(new FluidStack(Fluids.WATER, (int) (storageBlockCount() * fluidPerBlock)));
            return List.of();
        }

        List<Integer> ids = idsGetter.apply(neighbors.size() - 1);
        BlockPos neighbor = neighbors.remove(0); // remove first network (will be our network)

        clear();
        insertConnectedPositions(this, providerBlocks, consumerBlocks, storageBlocks, filledByRainBlocks, neighbor, level); // re-insert blocks
        fluidHandler.setFluid(new FluidStack(Fluids.WATER, (int) (storageBlockCount() * fluidPerBlock)));

        return neighbors.stream()
                .map((pos) -> {
                    if ((providerBlocks.isEmpty() && consumerBlocks.isEmpty() && storageBlocks.isEmpty())
                            || (!providerBlocks.containsKey(pos) && !consumerBlocks.contains(pos) && !storageBlocks.containsKey(pos))) {
                        return null;
                    }

                    IrrigationServerNetwork network = new IrrigationServerNetwork(ids.remove(0), onChange, onRemove);
                    insertConnectedPositions(network, providerBlocks, consumerBlocks, storageBlocks, filledByRainBlocks, pos, level);
                    return network;
                })
                .filter(Objects::nonNull)
                .peek((n) -> n.fluidHandler.setFluid(new FluidStack(Fluids.WATER, (int) (n.storageBlockCount() * fluidPerBlock))))
                .toList();
    }

    @Override
    protected boolean isNotEmpty() {
        return !providers.isEmpty() || !consumers.isEmpty() || !storages.isEmpty();
    }

    @Override
    protected boolean isValidPosition(@NotNull IIrrigationBlockEntity blockEntity, @NotNull BlockPos pos) {
        if (providers.containsKey(pos) || consumers.contains(pos) || storages.containsKey(pos)) {
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
    protected void addBlockEntity(@NotNull IIrrigationBlockEntity blockEntity) {
        super.addBlockEntity(blockEntity);

        switch (blockEntity.getNetworkType()) {
            case CONSUMER -> addConsumer(blockEntity);
            case PROVIDER -> addProvider(blockEntity);
            case STORAGE -> addStorage(blockEntity);
        }

        if (blockEntity.validForRainFilling()) {
            filledByRain.add(blockEntity.getBlockPos());
        }
    }

    @Override
    protected void removeBlockEntity(@NotNull IIrrigationBlockEntity blockEntity) {
        switch (blockEntity.getNetworkType()) {
            case CONSUMER -> removeConsumer(blockEntity);
            case PROVIDER -> removeProvider(blockEntity);
            case STORAGE -> removeStorage(blockEntity);
        }

        filledByRain.remove(blockEntity.getBlockPos());
        super.removeBlockEntity(blockEntity);
    }

    public void tick(@NotNull ServerLevel level) {
        int amount = 0;

        // rain filling
        if (YTechMod.CONFIGURATION.shouldRainingFillAqueduct() && level.isRaining() && level.getGameTime() % YTechMod.CONFIGURATION.getRainingFillPerNthTick() == 0) {
            amount += YTechMod.CONFIGURATION.getRainingFillAmount() * filledByRainCount();
        }

        if (level.getGameTime() % YTechMod.CONFIGURATION.getValveFillPerNthTick() == 0) {
            amount += inflow;
        }

        if (amount > 0 && fluidHandler.getFluidAmount() < fluidHandler.getCapacity()) {
            fluidHandler.fill(new FluidStack(Fluids.WATER, amount), IFluidHandler.FluidAction.EXECUTE);
        }
    }

    @NotNull
    public FluidTank getFluidHandler() {
        return fluidHandler;
    }

    public int storageBlockCount() {
        return storages.size();
    }

    public int filledByRainCount() {
        return filledByRain.size();
    }

    private void addProvider(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        int flow = entity.getFlow();

        providers.put(blockPos, flow);
        inflow += flow;
    }

    private void addConsumer(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        consumers.add(blockPos);
    }

    private void addStorage(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        int capacity = entity.getCapacity();

        storages.put(blockPos, capacity);
        fluidHandler.setCapacity(fluidHandler.getCapacity() + capacity);
    }

    private void removeProvider(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        int value = providers.remove(blockPos);

        inflow -= value;
    }

    private void removeConsumer(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();
        consumers.remove(blockPos);
    }

    private void removeStorage(@NotNull IIrrigationBlockEntity entity) {
        BlockPos blockPos = entity.getBlockPos();

        storages.remove(blockPos);
        fluidHandler.setCapacity(fluidHandler.getCapacity() - entity.getCapacity());
    }

    private void clear() {
        consumers.clear();
        providers.clear();
        storages.clear();
        filledByRain.clear();
        inflow = 0;
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
                                                 @NotNull Set<BlockPos> consumerBlocks, @NotNull Map<BlockPos, Integer> storageBlocks,
                                                 @NotNull Set<BlockPos> filledByRainBlocks, @NotNull BlockPos from, @NotNull Level level) {
        BlockEntity blockEntity = level.getBlockEntity(from);

        if (blockEntity instanceof IIrrigationBlockEntity block) {
            network.addBlockEntity(block);
            providerBlocks.remove(from);
            consumerBlocks.remove(from);
            storageBlocks.remove(from);
            filledByRainBlocks.remove(from);
            block.getValidNeighbors().forEach((pos) -> {
                if (providerBlocks.containsKey(pos) || consumerBlocks.contains(pos) || storageBlocks.containsKey(pos)) {
                    insertConnectedPositions(network, providerBlocks, consumerBlocks, storageBlocks, filledByRainBlocks, pos, level);
                }
            });
        }
    }
}
