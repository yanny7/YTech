package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractPrimitiveMachineBlockEntity extends MachineBlockEntity {
    public static final int TEMP_PER_CHIMNEY = 50;
    private static final RandomSource RANDOM = RandomSource.create(42L);
    private static final String TAG_ITEMS = "items";
    private static final String TAG_SMELTING_TIME = "smeltingTime";
    private static final String TAG_LEFT_SMELTING = "smeltingLeft";
    private static final String TAG_RECIPE_TEMPERATURE = "recipeTemperature";
    private static final String TAG_BURNING_TIME = "burningTime";
    private static final String TAG_LEFT_BURNING = "burningLeft";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_NR_CHIMNEY = "nrChimney";
    private static final int BASE_MAX_TEMPERATURE = 900;
    private static final int BASE_MIN_TEMPERATURE = 20;

    private final RecipeType<?> recipeType;
    private int nrChimney = -1;
    private int burningTime = 0;
    private int leftBurningTime = 0;
    protected int temperature = 0;
    protected int leftSmelting = 0;
    protected int smeltingTime = 0;
    protected int recipeTemperature = 0;

    public AbstractPrimitiveMachineBlockEntity(Holder holder, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState, RecipeType<?> recipeType) {
        super(holder, blockEntityType, pos, blockState);
        this.recipeType = recipeType;
    }

    @Override
    public void tickServer(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {
        boolean isBurning = false;
        int maxTemperature = getMaxTemperature();
        AtomicBoolean hasChanged = new AtomicBoolean(false);

        if (!hasActiveRecipe()) {
            if (hasItemsInInput()) {
                startRecipe(hasChanged);
            }
        } else {
            if (temperature >= recipeTemperature) {
                if (leftSmelting > 0) {
                    leftSmelting--;
                } else {
                    finishRecipe();
                }

                hasChanged.set(true);
            }
        }

        if (leftBurningTime > 0) {
            leftBurningTime--;
            isBurning = true;
            hasChanged.set(true);
        } else {
            ItemStack fuel = itemStackHandler.getStackInSlot(getFuelSlot());

            if (!fuel.isEmpty() && ((hasItemsInInput() && isValidRecipeInInput()) || hasActiveRecipe())) {
                leftBurningTime = burningTime = CommonHooks.getBurnTime(fuel, RecipeType.BLASTING);
                isBurning = true;

                if (fuel.hasCraftingRemainingItem()) {
                    itemStackHandler.setStackInSlot(getFuelSlot(), fuel.getCraftingRemainingItem());
                }

                fuel.shrink(1);
                setPoweredState(level, blockState, pos, true);
            } else {
                setPoweredState(level, blockState, pos, false);
            }
        }

        int oldTemperature = temperature;

        if (isBurning && (maxTemperature > temperature)) {
            temperature = Math.min(maxTemperature, temperature + 1);
        } else if (!isBurning || (maxTemperature < temperature)) {
            temperature = Math.max(BASE_MIN_TEMPERATURE, temperature - 1);
        }

        if (temperature != oldTemperature) {
            hasChanged.set(true);
        }

        if (hasChanged.get()) {
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
        }
    }

    @Override
    public void tickClient(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {
        if (blockState.getValue(BlockStateProperties.POWERED) && RANDOM.nextInt(Math.max(Math.round(4 - nrChimney / 2f), 1)) == 0) {
            for (int i = 0; i < RANDOM.nextInt(2) + 2; ++i) {
                makeParticles(level, pos, nrChimney);
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (level != null && !level.isClientSide && nrChimney == -1) {
            nrChimney = detectChimneys(level);
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (level == null || !level.isClientSide) { // server side
            if (tag.contains(TAG_ITEMS)) {
                itemStackHandler.deserializeNBT(tag.getCompound(TAG_ITEMS));
            }

            // Try to load if exists, otherwise load in onLoad method
            if (tag.contains(TAG_NR_CHIMNEY)) {
                nrChimney = tag.getInt(TAG_NR_CHIMNEY);
            }

            burningTime = tag.getInt(TAG_BURNING_TIME);
            leftBurningTime = tag.getInt(TAG_LEFT_BURNING);
            smeltingTime = tag.getInt(TAG_SMELTING_TIME);
            leftSmelting = tag.getInt(TAG_LEFT_SMELTING);
            temperature = tag.getInt(TAG_TEMPERATURE);
            recipeTemperature = tag.getInt(TAG_RECIPE_TEMPERATURE);
        } else { // client side
            nrChimney = tag.getInt(TAG_NR_CHIMNEY);
        }
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        tag.putInt(TAG_NR_CHIMNEY, nrChimney);
        return tag;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull ContainerData createContainerData() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> Math.round(leftBurningTime / (float)burningTime * 100);
                    case 1 -> getMaxTemperature();
                    case 2 -> temperature;
                    case 3 -> leftSmelting > 0 ? Math.round((smeltingTime - leftSmelting) / (float)smeltingTime * 100) : 0;
                    case 4 -> hasActiveRecipe() ? 1 : 0;
                    case 5 -> (getBlockState().getValue(BlockStateProperties.POWERED) ? 1 : 0);
                    default -> throw new IllegalStateException("Invalid index");
                };
            }

            @Override
            public void set(int index, int value) {}

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    public void onRemove() {
        if (level != null && level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
            chimney.onRemove();
        }
    }

    public int temperature() {
        return temperature;
    }

    public int progress() {
        return Math.round((smeltingTime - leftSmelting) / (float)smeltingTime * 100);
    }

    public void chimneyAdded() {
        if (level != null) {
            nrChimney++;
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void chimneyRemoved() {
        if (level != null) {
            nrChimney--;
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);

        if (level == null || !level.isClientSide) {
            tag.put(TAG_ITEMS, itemStackHandler.serializeNBT());
            tag.putInt(TAG_NR_CHIMNEY, nrChimney);
            tag.putInt(TAG_BURNING_TIME, burningTime);
            tag.putInt(TAG_LEFT_BURNING, leftBurningTime);
            tag.putInt(TAG_SMELTING_TIME, smeltingTime);
            tag.putInt(TAG_LEFT_SMELTING, leftSmelting);
            tag.putInt(TAG_TEMPERATURE, temperature);
            tag.putInt(TAG_RECIPE_TEMPERATURE, recipeTemperature);
        } else {
            tag.putInt(TAG_NR_CHIMNEY, nrChimney);
        }
    }

    protected abstract boolean hasItemsInInput();

    protected abstract int getFuelSlot();

    protected abstract void startRecipe(@NotNull AtomicBoolean hasChanged);

    protected abstract void finishRecipe();

    public abstract boolean hasActiveRecipe();

    protected abstract boolean isValidRecipeInInput();

    private void setPoweredState(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos pos, boolean powered) {
        if (powered != blockState.getValue(BlockStateProperties.POWERED)) {
            BlockState newState = blockState.setValue(BlockStateProperties.POWERED, powered);

            level.setBlock(pos, newState, Block.UPDATE_ALL);
            setChanged(level, worldPosition, newState);
        }
    }

    private int detectChimneys(@NotNull Level level) {
        int nrChimney = 0;
        BlockPos blockPos = worldPosition.mutable();

        while (level.getBlockEntity(blockPos = blockPos.above()) instanceof BrickChimneyBlockEntity blockEntity) {
            nrChimney++;
            blockEntity.setMaster(worldPosition);
        }

        return nrChimney;
    }

    private int getMaxTemperature() {
        return BASE_MAX_TEMPERATURE + nrChimney * TEMP_PER_CHIMNEY;
    }

    private static void makeParticles(@NotNull Level level, @NotNull BlockPos pos, int offset) {
        level.addAlwaysVisibleParticle(
                ParticleTypes.CAMPFIRE_COSY_SMOKE, true,
                pos.getX() + 0.5D + RANDOM.nextDouble() / 3.0D * (RANDOM.nextBoolean() ? 1 : -1),
                pos.getY() + 0.5D + offset + RANDOM.nextDouble() + RANDOM.nextDouble(),
                pos.getZ() + 0.5D + RANDOM.nextDouble() / 3.0D * (RANDOM.nextBoolean() ? 1 : -1),
                0.0D, 0.07D + 0.02 * offset, 0.0D
        );
    }
}
