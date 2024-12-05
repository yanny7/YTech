package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.recipe.TanningRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TanningRackBlockEntity extends BlockEntity {
    private final SimpleProgressHandler<TanningRecipe> progressHandler;

    public TanningRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(YTechBlockEntityTypes.TANNING_RACK.get(), pos, blockState);
        progressHandler = new SimpleProgressHandler<>(YTechRecipeTypes.TANNING.get());
    }

    public int getProgress() {
        return progressHandler.getProgress();
    }

    public ItemStack getItem() {
        return progressHandler.getItem();
    }

    public InteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level instanceof ServerLevel serverLevel) {
            ItemStack holdingItemStack = player.getItemInHand(hand);

            if (progressHandler.isEmpty()) {
                progressHandler.setupCrafting(serverLevel, holdingItemStack, TanningRecipe::hitCount);
            } else {
                Function<TanningRecipe, Boolean> canProcess = (recipe) -> recipe.tool().items().isEmpty() || recipe.tool().test(holdingItemStack);
                Function<TanningRecipe, Float> getStep = (recipe) -> 1F;
                BiConsumer<SingleRecipeInput, TanningRecipe> onFinish = (container, recipe) -> {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), recipe.assemble(container, level.registryAccess()));
                };

                if (!progressHandler.tick(serverLevel, canProcess, getStep, onFinish)) {
                    Block.popResourceFromFace(level, pos, hitResult.getDirection(), progressHandler.getItem());
                    progressHandler.clear();
                } else {
                    player.getItemInHand(hand).hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                }
            }

            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        progressHandler.load(tag, provider);
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

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        progressHandler.save(tag, provider);
    }
}
