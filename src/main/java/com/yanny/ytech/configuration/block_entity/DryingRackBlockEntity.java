package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.recipe.DryingRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class DryingRackBlockEntity extends BlockEntity implements BlockEntityTicker<DryingRackBlockEntity> {
    private final SimpleProgressHandler<DryingRecipe> progressHandler;

    public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(YTechBlockEntityTypes.DRYING_RACK.get(), pos, blockState);
        progressHandler = new SimpleProgressHandler<>(YTechRecipeTypes.DRYING.get());
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
                progressHandler.setupCrafting(level, holdingItemStack, DryingRecipe::dryingTime);
            } else {
                Block.popResourceFromFace(level, pos, hitResult.getDirection(), progressHandler.getItem());
                progressHandler.clear();
            }

            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            level.blockEntityChanged(pos);
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull DryingRackBlockEntity blockEntity) {
        Function<DryingRecipe, Boolean> canProcess = (recipe) -> !(level.isRaining() && YTechMod.CONFIGURATION.noDryingDuringRain());
        Function<DryingRecipe, Float> getStep = (recipe) -> {
            Holder<Biome> biome = level.getBiome(pos);

            if (biome.tags().anyMatch(t -> YTechMod.CONFIGURATION.getSlowDryingBiomes().contains(t))) {
                return 0.5F;
            } else if (biome.tags().anyMatch(t -> YTechMod.CONFIGURATION.getFastDryingBiomes().contains(t))) {
                return 2F;
            }

            return 1F;
        };
        BiConsumer<Container, DryingRecipe> onFinish = (container, recipe) -> {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), recipe.assemble(container, level.registryAccess()));
            level.sendBlockUpdated(pos, state, state, Block.UPDATE_ALL);
            level.blockEntityChanged(pos);
        };

        if (progressHandler.tick(level, canProcess, getStep, onFinish)) {
            setChanged(level, pos, state);
        }
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
