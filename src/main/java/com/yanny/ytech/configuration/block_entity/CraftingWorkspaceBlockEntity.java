package com.yanny.ytech.configuration.block_entity;

import com.yanny.ytech.configuration.block.CraftingWorkspaceBlock;
import com.yanny.ytech.configuration.recipe.WorkspaceCraftingRecipe;
import com.yanny.ytech.configuration.renderer.FakeCraftingWorkspaceLevel;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechItemTags;
import com.yanny.ytech.registration.YTechRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.objenesis.instantiator.ObjectInstantiator;

import java.util.Optional;

public class CraftingWorkspaceBlockEntity extends BlockEntity {
    private static final FakeCraftingWorkspaceLevel FAKE_LEVEL;

    static {
        Objenesis objenesis = new ObjenesisStd(); // or ObjenesisSerializer
        ObjectInstantiator<FakeCraftingWorkspaceLevel> instantiator = objenesis.getInstantiatorOf(FakeCraftingWorkspaceLevel.class);
        FAKE_LEVEL = instantiator.newInstance();
        FAKE_LEVEL.init();
    }

    private static final String TAG_BITMASK = "bitmask";
    private static final String TAG_ITEMS = "items";
    private static final String TAG_BLOCK_STATES = "blockStates";

    private final NonNullList<ItemStack> itemList = NonNullList.withSize(27, ItemStack.EMPTY);
    private final NonNullList<BlockState> blockStates = NonNullList.withSize(27, Blocks.AIR.defaultBlockState());
    private int bitmask = 0;

    public CraftingWorkspaceBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(YTechBlockEntityTypes.CRAFTING_WORKSPACE.get(), pPos, pBlockState);
    }

    public InteractionResult use(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        int[] pos = CraftingWorkspaceBlock.getPosition(pHit, pPlayer.getItemInHand(pHand).isEmpty());
        ItemStack itemStack = pPlayer.getItemInHand(pHand);

        if (pos != null) {
            int index = CraftingWorkspaceBlock.getIndex(pos);
            BlockPos fakePos = new BlockPos(pos[0] + 1, pos[1] + 1, pos[2] + 1);
            boolean wasChange = false;

            if ((bitmask >> index & 1) == 0 && !itemStack.isEmpty() && !itemStack.is(YTechItemTags.HAMMERS.tag)) {
                bitmask |= 1 << index;

                if (itemStack.getItem() instanceof BlockItem blockItem) {
                    FAKE_LEVEL.setData(pPos, pLevel, itemList, blockStates);

                    Block block = blockItem.getBlock();
                    BlockHitResult hit = new BlockHitResult(pHit.getLocation(), pHit.getDirection(), fakePos, true);
                    BlockState state = block.getStateForPlacement(new BlockPlaceContext(FAKE_LEVEL, pPlayer, pHand, itemStack, hit));

                    if (state == null) {
                        state = block.defaultBlockState();
                    }

                    blockStates.set(index, state);
                    updateNeighbors(state, pos, fakePos);
                    FAKE_LEVEL.clearData();
                }

                itemList.set(index, itemStack.split(1));
                wasChange = true;
            } else if ((bitmask >> index & 1) == 1 && itemStack.isEmpty()) {
                Block.popResourceFromFace(pLevel, pPos, pHit.getDirection(), itemList.get(index));

                FAKE_LEVEL.setData(pPos, pLevel, itemList, blockStates);
                blockStates.set(index, Blocks.AIR.defaultBlockState());
                updateNeighbors(Blocks.AIR.defaultBlockState(), pos, fakePos);
                FAKE_LEVEL.clearData();

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

    public NonNullList<BlockState> getBlockStates() {
        return blockStates;
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        bitmask = pTag.getInt(TAG_BITMASK);
        ListTag list = pTag.getList(TAG_ITEMS, Tag.TAG_COMPOUND);
        ListTag states = pTag.getList(TAG_BLOCK_STATES, Tag.TAG_COMPOUND);

        for (int i = 0; i < 27; i++) {
            itemList.set(i, ItemStack.of(list.getCompound(i)));
            blockStates.set(i, NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), states.getCompound(i)));
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
        ListTag states = new ListTag();

        for (int i = 0; i < 27; i++) {
            list.add(i, itemList.get(i).save(new CompoundTag()));
            states.add(i, NbtUtils.writeBlockState(blockStates.get(i)));
        }

        pTag.put(TAG_ITEMS, list);
        pTag.put(TAG_BLOCK_STATES, states);
    }

    private InteractionResult constructBlock(@NotNull ItemStack itemStack, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        Container container = new SimpleContainer(itemList.toArray(ItemStack[]::new));
        Optional<RecipeHolder<WorkspaceCraftingRecipe>> op = pLevel.getRecipeManager().getRecipeFor(YTechRecipeTypes.WORKSPACE_CRAFTING.get(), container, pLevel);

        if (op.isPresent()) {
            BlockItem item = (BlockItem) op.get().value().assemble(container, pLevel.registryAccess()).getItem();
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

    private void updateNeighbors(BlockState state, int[] pos, BlockPos fakePos) {
        for (Direction value : Direction.values()) {
            int[] neighborPos = new int[]{pos[0] + value.getNormal().getX(), pos[1] + value.getNormal().getY(), pos[2] + value.getNormal().getZ()};

            if (CraftingWorkspaceBlock.validPosition(neighborPos)) {
                int neighborIndex = CraftingWorkspaceBlock.getIndex(neighborPos);

                if (neighborIndex >= 0 && neighborIndex < 27) {
                    if (itemList.get(neighborIndex).getItem() instanceof BlockItem neighborBlock) {
                        blockStates.set(neighborIndex, neighborBlock.getBlock().updateShape(blockStates.get(neighborIndex), value.getOpposite(), state, FAKE_LEVEL, fakePos.offset(value.getNormal()), fakePos));
                    }
                }
            }
        }
    }
}
