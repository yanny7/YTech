package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.entity.GoAroundEntity;
import com.yanny.ytech.configuration.recipe.MillingRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MillstoneBlockEntity extends BlockEntity {
    private static final String TAG_INPUT = "input";
    private static final String TAG_RESULT = "result";
    private static final String TAG_BONUS_CHANCE = "bonusChance";
    private static final String TAG_IS_MILLING = "isMilling";
    private static final String TAG_IS_LEASHED = "isLeashed";

    private ItemStack input = ItemStack.EMPTY;
    private ItemStack result = ItemStack.EMPTY;
    private float bonusChance = 0f;
    private boolean isMilling = false;
    private boolean isLeashed = false;
    @Nullable private GoAroundEntity entity = null;
    private final RecipeManager.CachedCheck<RecipeInput, MillingRecipe> quickCheck;

    public MillstoneBlockEntity(BlockPos pos, BlockState blockState) {
        super(YTechBlockEntityTypes.MILLSTONE.get(), pos, blockState);
        quickCheck = RecipeManager.createCheck(YTechRecipeTypes.MILLING.get());
    }

    public InteractionResult onUse(@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            if (!isLeashed) {
                int x = pos.getX();
                int y = pos.getY();
                int z = pos.getZ();
                List<Mob> mobList = level.getEntitiesOfClass(Mob.class, new AABB(x - 7.0, y - 7.0, z - 7.0, x + 7.0, y + 7.0, z + 7.0));

                for (Mob mob : mobList) {
                    if (mob.getLeashHolder() == player && mob.getPassengers().isEmpty()) {
                        mob.dropLeash(true, false);
                        GoAroundEntity mimic = new GoAroundEntity(mob, pos, level);
                        level.addFreshEntity(mimic);
                        mimic.startRiding(mob, true);
                        break;
                    }
                }
            }

            ItemStack holdingItemStack = player.getItemInHand(hand);

            if (result.isEmpty() && isLeashed && !holdingItemStack.isEmpty()) {
                Optional<RecipeHolder<MillingRecipe>> millingRecipe = quickCheck.getRecipeFor(new SingleRecipeInput(holdingItemStack), serverLevel);

                millingRecipe.ifPresent((r) -> {
                    EquipmentSlot slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

                    input = holdingItemStack.copyWithCount(holdingItemStack.getCount() - 1);
                    result = r.value().result();
                    bonusChance = r.value().bonusChance();
                    isMilling = true;
                    player.setItemSlot(slot, ItemStack.EMPTY);
                    setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
                });
            }

            if (player.isCrouching() && player.getItemInHand(hand).isEmpty() && isLeashed && entity != null) {
                entity.stopRiding();
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        input = ItemStack.parseOptional(provider, tag.getCompound(TAG_INPUT));
        result = ItemStack.parseOptional(provider, tag.getCompound(TAG_RESULT));
        bonusChance = tag.getFloat(TAG_BONUS_CHANCE);
        isMilling = tag.getBoolean(TAG_IS_MILLING);
        isLeashed = tag.getBoolean(TAG_IS_LEASHED);
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag(@NotNull HolderLookup.Provider provider) {
        CompoundTag tag = super.getUpdateTag(provider);
        saveAdditional(tag, provider);
        return tag;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean isMilling() {
        return isMilling;
    }

    public void onFinished() {
        if (level != null && !result.isEmpty()) {
            Block.popResource(level, getBlockPos(), result.copy());

            if (level.random.nextFloat() < bonusChance) {
                Block.popResource(level, getBlockPos(), result.copyWithCount(1));
            }

            if (input.isEmpty()) {
                isMilling = false;
                result = ItemStack.EMPTY;
            } else {
                input.setCount(input.getCount() - 1);
            }

            level.playSound(null, getBlockPos(), SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, level.random.nextFloat() * 0.25F + 0.75F, level.random.nextFloat() + 0.5F);
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
        }
    }

    public void removeLeash() {
        if (level != null) {
            isLeashed = false;
            entity = null;
            isMilling = false;
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
        }
    }

    public void setLeashed(@NotNull GoAroundEntity entity) {
        if (level != null) {
            this.entity = entity;
            isLeashed = true;

            if (!input.isEmpty() && !result.isEmpty()) {
                isMilling = true;
            }

            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_ALL);
            setChanged(level, worldPosition, Blocks.AIR.defaultBlockState());
        }
    }

    public void onRemove() {
        if (entity != null) {
            entity.stopRiding();
        }
    }

    public boolean isLeashed() {
        return isLeashed;
    }

    public ItemStack getInputItem() {
        return this.input;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        tag.put(TAG_INPUT, input.saveOptional(provider));
        tag.put(TAG_RESULT, result.saveOptional(provider));
        tag.putFloat(TAG_BONUS_CHANCE, bonusChance);
        tag.putBoolean(TAG_IS_MILLING, isMilling);
        tag.putBoolean(TAG_IS_LEASHED, isLeashed);
    }
}
