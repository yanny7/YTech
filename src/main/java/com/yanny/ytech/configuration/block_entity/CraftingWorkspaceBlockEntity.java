package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.block.CraftingWorkspaceBlock;
import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CraftingWorkspaceBlockEntity extends BlockEntity {
    private static final String TAG_BITMASK = "bitmask";
    private static final String TAG_ITEMS = "items";

    private final NonNullList<ItemStack> itemList = NonNullList.withSize(27, ItemStack.EMPTY);
    private int bitmask = 0;

    public CraftingWorkspaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(YTechBlockEntityTypes.CRAFTING_WORKSPACE.get(), pPos, pBlockState);
    }

    public InteractionResult use(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        int[] pos = CraftingWorkspaceBlock.getPosition(pHit, pPlayer.getItemInHand(pHand).isEmpty());
        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if (pos != null) {
            int index = CraftingWorkspaceBlock.getIndex(pos);
            boolean wasChange = false;

            if ((bitmask >> index & 1) == 0 && !itemStack.isEmpty() && itemStack.getItem() instanceof BlockItem) {
                bitmask |= 1 << index;
                itemList.set(index, itemStack.split(1));
                wasChange = true;
            } else if ((bitmask >> index & 1) == 1 && itemStack.isEmpty()) {
                Block.popResourceFromFace(pLevel, pPos, pHit.getDirection(), itemList.get(index));
                itemList.set(index, ItemStack.EMPTY);
                bitmask &= ~(1 << index);
                wasChange = true;
            } else if (itemStack.is(YTechItemTags.HAMMERS.tag)) {
                return constructBlock(itemStack, pLevel, pPos, pPlayer, pHand, pHit);
            }

            if (wasChange) {
                pLevel.sendBlockUpdated(pPos, pState, pState, Block.UPDATE_ALL);
                pLevel.blockEntityChanged(pPos);
                return InteractionResult.CONSUME;
            }
        } else if (itemStack.is(YTechItemTags.HAMMERS.tag)) {
            return constructBlock(itemStack, pLevel, pPos, pPlayer, pHand, pHit);
        }

        return InteractionResult.PASS;
    }

    public int getBitmask() {
        return bitmask;
    }

    public NonNullList<ItemStack> getItems() {
        return itemList;
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        bitmask = pTag.getInt(TAG_BITMASK);
        ListTag list = pTag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);

        for (int i = 0; i < 27; i++) {
            itemList.set(i, ItemStack.of(list.getCompound(i)));
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

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putInt(TAG_BITMASK, bitmask);

        ListTag list = new ListTag();

        for (int i = 0; i < 27; i++) {
            list.add(i, itemList.get(i).save(new CompoundTag()));
        }

        pTag.put(TAG_ITEMS, list);
    }

    private InteractionResult constructBlock(@NotNull ItemStack itemStack, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        Container container = new SimpleContainer(itemList.toArray(ItemStack[]::new));
        Optional<WorkspaceCraftingRecipe> op = pLevel.getRecipeManager().getRecipeFor(YTechRecipeTypes.WORKSPACE_CRAFTING.get(), container, pLevel);

        if (op.isPresent()) {
            BlockItem item = (BlockItem) op.get().assemble(container, pLevel.registryAccess()).getItem();
            BlockState blockState = item.getBlock().getStateForPlacement(new BlockPlaceContext(pPlayer, pHand, ItemStack.EMPTY, pHit));

            if (blockState == null) {
                blockState = item.getBlock().defaultBlockState();
            }

            itemList.clear();
            pLevel.setBlock(pPos, blockState, Block.UPDATE_ALL);
            pLevel.blockEntityChanged(pPos);
            pLevel.playSound(null, pPos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
            pLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), pPos.getX() + 0.5, pPos.getY() + 0.5, pPos.getZ() + 0.5, 200, 0.3F, 0.3F, 0.3F, 0.15F);
            itemStack.hurtAndBreak(1, pPlayer, e -> e.broadcastBreakEvent(pHand));
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
