package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.registration.YTechBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class FirePitBlockEntity  extends BlockEntity implements BlockEntityTicker<FirePitBlockEntity> {
    private static final String TAG_ITEM = "item";
    private static final String TAG_COOKING_TIME = "CookingTime";
    private static final String TAG_COOKING_TOTAL_TIME = "CookingTotalTime";

    private ItemStack item = ItemStack.EMPTY;
    private float cookingProgress = 0;
    private int cookingTime = 0;
    private final RecipeManager.CachedCheck<Container, CampfireCookingRecipe> quickCheck;

    public FirePitBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(YTechBlockEntityTypes.FIRE_PIT.get(), pPos, pBlockState);
        quickCheck = RecipeManager.createCheck(RecipeType.CAMPFIRE_COOKING);
    }

    public ItemStack getItem() {
        return item;
    }

    public int getProgress() {
        return Math.round(cookingProgress / cookingTime * 100);
    }

    public InteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack holdingItemStack = player.getItemInHand(hand);

            if (item.isEmpty()) {
                quickCheck.getRecipeFor(new SimpleContainer(holdingItemStack), level).ifPresent((r) -> {
                    item = holdingItemStack.split(1);
                    cookingTime = r.getCookingTime();
                    cookingProgress = 0;
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
                });
            } else {
                Block.popResourceFromFace(level, pos, hitResult.getDirection(), item);
                item = ItemStack.EMPTY;
                cookingTime = 0;
                cookingProgress = 0;
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FirePitBlockEntity blockEntity) {
        boolean wasChange = false;
        int heatLevel = state.getValue(BlockStateProperties.LEVEL);

        if (!item.isEmpty() && state.getValue(BlockStateProperties.LIT) && heatLevel > 0) {
            wasChange = true;
            cookingProgress = cookingProgress + (heatLevel / 15f);

            if (cookingProgress >= cookingTime) {
                Container container = new SimpleContainer(item);
                ItemStack result = quickCheck.getRecipeFor(container, level).map((r) -> r.assemble(container, level.registryAccess())).orElse(item);

                if (result.isItemEnabled(level.enabledFeatures())) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), result);
                    item = ItemStack.EMPTY;
                    cookingTime = 0;
                    cookingProgress = 0;
                    level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
                }
            }
        }

        if (wasChange) {
            setChanged(level, pos, state);
        }

    }

    public void load(@NotNull CompoundTag tag) {
        super.load(tag);

        if (tag.contains(TAG_ITEM)) {
            item = ItemStack.of(tag.getCompound(TAG_ITEM));
        }

        if (tag.contains(TAG_COOKING_TIME)) {
            cookingProgress = tag.getFloat(TAG_COOKING_TIME);
        }

        if (tag.contains(TAG_COOKING_TOTAL_TIME)) {
            cookingTime = tag.getInt(TAG_COOKING_TOTAL_TIME);
        }
    }

    @NotNull
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        CompoundTag itemTag = new CompoundTag();
        item.save(itemTag);
        tag.put(TAG_ITEM, itemTag);
        tag.putFloat(TAG_COOKING_TIME, this.cookingProgress);
        tag.putInt(TAG_COOKING_TOTAL_TIME, this.cookingTime);
    }
}
