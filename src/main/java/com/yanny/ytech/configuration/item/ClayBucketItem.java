package com.yanny.ytech.configuration.item;

import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.event.EventHooks;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ClayBucketItem extends BucketItem {
    public ClayBucketItem(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @NotNull
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemInHand = player.getItemInHand(hand);
        BlockHitResult blockHitResult = getPlayerPOVHitResult(level, player, getFluid() == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> interactionResult = EventHooks.onBucketUse(player, level, itemInHand, blockHitResult);

        if (interactionResult != null) {
            return interactionResult;
        }

        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemInHand);
        } else if (blockHitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemInHand);
        } else {
            BlockPos blockPos = blockHitResult.getBlockPos();
            Direction direction = blockHitResult.getDirection();
            BlockPos relativePos = blockPos.relative(direction);

            if (level.mayInteract(player, blockPos) && player.mayUseItemAt(relativePos, direction, itemInHand)) {
                if (getFluid() == Fluids.EMPTY) {
                    BlockState blockState = level.getBlockState(blockPos);

                    // allow only water and lava pickup
                    if (!blockState.hasProperty(BlockStateProperties.WATERLOGGED)) {
                        if (blockState.getBlock() instanceof LiquidBlock liquidBlock) {
                            if (!liquidBlock.getFluid().isSame(Fluids.WATER) && !liquidBlock.getFluid().isSame(Fluids.LAVA)) {
                                return InteractionResultHolder.fail(itemInHand);
                            }
                        }
                    }

                    if (blockState.getBlock() instanceof BucketPickup bucketPickup) {
                        ItemStack pickedItemStack = bucketPickup.pickupBlock(player, level, blockPos, blockState);

                        if (!pickedItemStack.isEmpty()) {
                            if (pickedItemStack.is(Items.WATER_BUCKET)) {
                                pickedItemStack = new ItemStack(YTechItems.WATER_CLAY_BUCKET.get());
                            } else if (pickedItemStack.is(Items.LAVA_BUCKET)) {
                                pickedItemStack = new ItemStack(YTechItems.LAVA_CLAY_BUCKET.get());
                            } else {
                                return InteractionResultHolder.fail(itemInHand);
                            }

                            player.awardStat(Stats.ITEM_USED.get(this));
                            bucketPickup.getPickupSound(blockState).ifPresent((soundEvent) -> player.playSound(soundEvent, 1.0F, 1.0F));
                            level.gameEvent(player, GameEvent.FLUID_PICKUP, blockPos);

                            ItemStack filledResult = ItemUtils.createFilledResult(itemInHand, player, pickedItemStack);

                            if (!level.isClientSide) {
                                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, pickedItemStack);
                            }

                            return InteractionResultHolder.sidedSuccess(filledResult, level.isClientSide());
                        }
                    }

                    return InteractionResultHolder.fail(itemInHand);
                } else {
                    BlockState blockState = level.getBlockState(blockPos);
                    BlockPos placePos = canBlockContainFluid(player, level, blockPos, blockState) ? blockPos : relativePos;

                    if (this.emptyContents(player, level, placePos, blockHitResult, itemInHand)) {
                        this.checkExtraContent(player, level, itemInHand, placePos);

                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, placePos, itemInHand);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemInHand, player), level.isClientSide());
                    } else {
                        return InteractionResultHolder.fail(itemInHand);
                    }
                }
            } else {
                return InteractionResultHolder.fail(itemInHand);
            }
        }
    }

    @NotNull
    public static ItemStack getEmptySuccessItem(@NotNull ItemStack bucketItemStack, Player player) {
        if (bucketItemStack.is(YTechItemTags.LAVA_BUCKETS) && !player.getAbilities().instabuild) {
            return ItemStack.EMPTY;
        }

        return !player.getAbilities().instabuild ? new ItemStack(YTechItems.CLAY_BUCKET.get()) : bucketItemStack;
    }
}
