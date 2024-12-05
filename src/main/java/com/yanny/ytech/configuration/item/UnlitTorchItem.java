package com.yanny.ytech.configuration.item;

import com.yanny.ytech.registration.YTechBlockTags;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.ChatFormatting.DARK_GRAY;

public class UnlitTorchItem extends Item {
    public UnlitTorchItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("text.ytech.hover.unlit_torch1").withStyle(DARK_GRAY));
        pTooltipComponents.add(Component.translatable("text.ytech.hover.unlit_torch2").withStyle(DARK_GRAY));
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
