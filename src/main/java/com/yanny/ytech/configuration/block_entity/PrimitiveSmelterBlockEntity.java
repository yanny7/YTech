package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.MachineItemStackHandler;
import com.yanny.ytech.configuration.recipe.SmeltingRecipe;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class PrimitiveSmelterBlockEntity extends MachineBlockEntity {
    private static final RandomSource RANDOM = RandomSource.create(42L);
    private static final String TAG_ITEMS = "items";
    private static final String TAG_RESULT = "result";
    private static final String TAG_SMELTING_TIME = "smeltingTime";
    private static final String TAG_LEFT_SMELTING = "smeltingLeft";
    private static final String TAG_RECIPE_TEMPERATURE = "recipeTemperature";
    private static final String TAG_BURNING_TIME = "burningTime";
    private static final String TAG_LEFT_BURNING = "burningLeft";
    private static final String TAG_TEMPERATURE = "temperature";
    private static final String TAG_NR_CHIMNEY = "nrChimney";
    private static final int SLOT_INPUT = 0;
    private static final int SLOT_FUEL = 1;
    private static final int SLOT_OUTPUT = 2;
    private static final int BASE_MAX_TEMPERATURE = 700;
    private static final int BASE_MIN_TEMPERATURE = 20;
    private static final int TEMP_PER_CHIMNEY = 100;

    private int nrChimney = -1;
    private int burningTime = 600;
    private int leftBurningTime = 0;
    private int temperature = 20;
    private int leftSmelting = 0;
    private int smeltingTime = 0;
    private int recipeTemperature = 0;
    @Nullable private ItemStack recipe = ItemStack.EMPTY;

    public PrimitiveSmelterBlockEntity(Holder holder, BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        super(holder, blockEntityType, pos, blockState);
    }

    @Override
    public void tickServer(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull MachineBlockEntity blockEntity) {
        boolean isBurning = false;
        int maxTemperature = getMaxTemperature();
        AtomicBoolean hasChanged = new AtomicBoolean(false);

        if (recipe == null) {
            ItemStack input = itemStackHandler.getStackInSlot(SLOT_INPUT);

            if (!input.isEmpty()) {
                level.getRecipeManager().getRecipeFor(SmeltingRecipe.RECIPE_TYPE, new SimpleContainer(input), level).ifPresent((r) -> {
                    ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);

                    if (r.minTemperature() <= temperature && (result.isEmpty() || (ItemStack.isSameItemSameTags(result, r.result()) && result.getMaxStackSize() > result.getCount()))) {
                        recipe = input.split(1);
                        leftSmelting = smeltingTime = r.smeltingTime();
                        recipeTemperature = r.minTemperature();
                        hasChanged.set(true);
                    }
                });
            }
        } else {
            if (temperature >= recipeTemperature) {
                if (leftSmelting > 0) {
                    leftSmelting--;
                } else {
                    ItemStack result = itemStackHandler.getStackInSlot(SLOT_OUTPUT);

                    level.getRecipeManager().getRecipeFor(SmeltingRecipe.RECIPE_TYPE, new SimpleContainer(recipe), level).ifPresent((r) -> {
                        if (result.isEmpty()) {
                            itemStackHandler.setStackInSlot(SLOT_OUTPUT, r.result().copy());
                        } else {
                            result.grow(1);
                        }
                    });
                    recipe = null;
                }

                hasChanged.set(true);
            }
        }

        if (leftBurningTime > 0) {
            leftBurningTime--;
            isBurning = true;
            hasChanged.set(true);
        } else {
            ItemStack fuel = itemStackHandler.getStackInSlot(SLOT_FUEL);

            if (!fuel.isEmpty() && !itemStackHandler.getStackInSlot(SLOT_INPUT).isEmpty()) {
                leftBurningTime = burningTime = ForgeHooks.getBurnTime(fuel, RecipeType.BLASTING);
                fuel.shrink(1);
                isBurning = true;

                if (fuel.isEmpty()) {
                    itemStackHandler.setStackInSlot(SLOT_FUEL, fuel.getCraftingRemainingItem());
                }

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

            if (tag.contains(TAG_RESULT)) {
                recipe = ItemStack.of(tag.getCompound(TAG_RESULT));
            } else {
                recipe = null;
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

    public void onRemove() {
        if (level != null && level.getBlockEntity(worldPosition.above()) instanceof BrickChimneyBlockEntity chimney) {
            chimney.onRemove();
        }
    }

    public boolean isProcessing() {
        return recipe != null && !recipe.isEmpty();
    }

    public int temperature() {
        return temperature;
    }

    @Nullable
    public ItemStack processingItem() {
        return recipe;
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

            if (recipe != null) {
                CompoundTag itemStack = new CompoundTag();

                recipe.save(itemStack);
                tag.put(TAG_RESULT, itemStack);
            }
        } else {
            tag.putInt(TAG_NR_CHIMNEY, nrChimney);
        }
    }

    @NotNull
    @Override
    public MachineItemStackHandler createItemStackHandler() {
        return new MachineItemStackHandler.Builder()
                .addInputSlot(55, 16, this::canInput)
                .addInputSlot(55, 52, (itemStack -> ForgeHooks.getBurnTime(itemStack, RecipeType.BLASTING) > 0))
                .addOutputSlot(116, 35)
                .setOnChangeListener(this::setChanged)
                .build();
    }

    @Override
    protected @NotNull ContainerData createContainerData() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> Math.round(leftBurningTime / (float)burningTime * 100);
                    case 1 -> getMaxTemperature();
                    case 2 -> temperature;
                    case 3 -> leftSmelting > 0 ? Math.round((smeltingTime - leftSmelting) / (float)smeltingTime * 100) : 0;
                    case 4 -> (recipe != null && !recipe.isEmpty()) ? 1 : 0;
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

    private void setPoweredState(@NotNull Level level, @NotNull BlockState blockState, @NotNull BlockPos pos, boolean powered) {
        if (powered != blockState.getValue(BlockStateProperties.POWERED)) {
            BlockState newState = blockState.setValue(BlockStateProperties.POWERED, powered);

            level.setBlock(pos, newState, Block.UPDATE_ALL);
            setChanged(level, worldPosition, newState);
        }
    }

    private boolean canInput(ItemStack itemStack) {
        if (level != null) {
            return level.getRecipeManager().getRecipeFor(SmeltingRecipe.RECIPE_TYPE, new SimpleContainer(itemStack), level).isPresent();
        } else {
            return false;
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
