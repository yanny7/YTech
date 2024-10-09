package com.yanny.ytech.configuration.item;

import com.yanny.ytech.registration.YTechBlockTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

public class UnlitTorchItem extends Item {
    public UnlitTorchItem() {
        super(new Properties());
    }

    @NotNull
    @Override
    public InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();

        if (player != null && context.getLevel() instanceof ServerLevel level) {
            BlockState blockState = level.getBlockState(context.getClickedPos());

            if (blockState.is(YTechBlockTags.FIRE_SOURCE) && (!blockState.hasProperty(BlockStateProperties.LIT) || blockState.getValue(BlockStateProperties.LIT))) {
                context.getPlayer().setItemInHand(context.getHand(), new ItemStack(Items.TORCH, context.getItemInHand().getCount()));
                return InteractionResult.CONSUME;
            } else if (blockState.is(YTechBlockTags.SOUL_FIRE_SOURCE) && (!blockState.hasProperty(BlockStateProperties.LIT) || blockState.getValue(BlockStateProperties.LIT))) {
                context.getPlayer().setItemInHand(context.getHand(), new ItemStack(Items.SOUL_TORCH, context.getItemInHand().getCount()));
                return InteractionResult.CONSUME;
            }
        }

        return super.useOn(context);
    }
}
