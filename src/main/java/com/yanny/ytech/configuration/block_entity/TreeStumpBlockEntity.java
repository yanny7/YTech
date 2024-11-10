package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.recipe.ChoppingRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class TreeStumpBlockEntity extends BlockEntity {
    private final SimpleProgressHandler<ChoppingRecipe> progressHandler;

    public TreeStumpBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(YTechBlockEntityTypes.TREE_STUMP.get(), pPos, pBlockState);
        progressHandler = new SimpleProgressHandler<>(YTechRecipeTypes.CHOPPING.get());
    }

    public int getProgress() {
        return progressHandler.getProgress();
    }

    public ItemStack getItem() {
        return progressHandler.getItem();
    }

    public InteractionResult onUse(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                   @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide) {
            ItemStack holdingItemStack = player.getItemInHand(hand);

            if (progressHandler.isEmpty()) {
                if (!progressHandler.setupCrafting(level, holdingItemStack, ChoppingRecipe::hitCount)) {
                    progressHandler.setupCrafting(level, player.getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND), ChoppingRecipe::hitCount);
                }
            } else {
                Function<ChoppingRecipe, Boolean> canProcess = (recipe) -> recipe.tool().isEmpty() || recipe.tool().test(holdingItemStack);
                Function<ChoppingRecipe, Float> getStep = (recipe) -> 1F;
                BiConsumer<Container, ChoppingRecipe> onFinish = (container, recipe) -> {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), recipe.assemble(container, level.registryAccess()));
                };

                if (!progressHandler.tick(level, canProcess, getStep, onFinish)) {
                    Block.popResourceFromFace(level, pos, hitResult.getDirection(), progressHandler.getItem());
                    progressHandler.clear();
                } else {
                    player.getItemInHand(hand).hurtAndBreak(1, player, (e) -> e.broadcastBreakEvent(hand));
                }
            }

            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            level.blockEntityChanged(pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        progressHandler.load(tag);
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

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        progressHandler.save(tag);
    }
}
