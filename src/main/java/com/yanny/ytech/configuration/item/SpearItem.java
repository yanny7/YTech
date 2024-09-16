package com.yanny.ytech.configuration.item;

import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.SpearEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Position;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpearItem extends Item implements ProjectileItem {
    public static final ResourceLocation THROWING_PREDICATE = Utils.modLoc("throwing");
    private final SpearType spearType;

    public SpearItem(SpearType spearType) {
        super(new Properties()
                .durability(spearType.durability)
                .attributes(createAttributes(spearType))
                .component(DataComponents.TOOL, createToolProperties()));
        this.spearType = spearType;
    }

    @Override
    public boolean canAttackBlock(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        return !player.isCreative();
    }

    @NotNull
    @Override
    public UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.SPEAR;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 36000;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            int throwTime = this.getUseDuration(stack, entity) - timeLeft;

            if (throwTime >= spearType.throwThreshold) {
                float riptideLevel = EnchantmentHelper.getTridentSpinAttackStrength(stack, player);

                if (riptideLevel <= 0 || player.isInWaterOrRain()) {
                    if (!(stack.getDamageValue() >= stack.getMaxDamage() - 1)) {
                        Holder<SoundEvent> holder = EnchantmentHelper.pickHighestLevel(stack, EnchantmentEffectComponents.TRIDENT_SOUND).orElse(SoundEvents.TRIDENT_THROW);

                        if (!level.isClientSide) {
                            stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(entity.getUsedItemHand()));

                            if (riptideLevel == 0) {
                                SpearEntity spearEntity = new SpearEntity(level, player, stack, spearType);
                                spearEntity.shootFromRotation(
                                        player,
                                        player.getXRot(),
                                        player.getYRot(),
                                        0.0F,
                                        spearType.shootPower + riptideLevel * 0.5F,
                                        spearType.accuracy
                                );

                                if (player.hasInfiniteMaterials()) {
                                    spearEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                }

                                level.addFreshEntity(spearEntity);
                                level.playSound(null, spearEntity, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);

                                if (!player.getAbilities().instabuild) {
                                    player.getInventory().removeItem(stack);
                                }
                            }
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));

                        if (riptideLevel > 0) {
                            float f7 = player.getYRot();
                            float f = player.getXRot();
                            float f1 = -Mth.sin(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                            float f2 = -Mth.sin(f * ((float) Math.PI / 180F));
                            float f3 = Mth.cos(f7 * ((float) Math.PI / 180F)) * Mth.cos(f * ((float) Math.PI / 180F));
                            float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);

                            f1 *= riptideLevel / f4;
                            f2 *= riptideLevel / f4;
                            f3 *= riptideLevel / f4;
                            player.push(f1, f2, f3);
                            player.startAutoSpinAttack(20, 8.0F, stack);

                            if (player.onGround()) {
                                player.move(MoverType.SELF, new Vec3(0.0D, 1.1999999F, 0.0D));
                            }

                            level.playSound(null, player, holder.value(), SoundSource.PLAYERS, 1.0F, 1.0F);
                        }
                    }
                }
            }
        }
    }

    @NotNull
    @Override
    public InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.getDamageValue() >= itemstack.getMaxDamage() - 1) {
            return InteractionResultHolder.fail(itemstack);
        } else if (EnchantmentHelper.getTridentSpinAttackStrength(itemstack, player) > 0 && !player.isInWaterOrRain()) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
        return true;
    }

    @Override
    public int getEnchantmentValue(@NotNull ItemStack itemStack) {
        return 1;
    }

    @NotNull
    @Override
    public Projectile asProjectile(@NotNull Level level, @NotNull Position position, @NotNull ItemStack itemStack, @NotNull Direction direction) {
        SpearEntity spearEntity = new SpearEntity(level, position.x(), position.y(), position.z(), itemStack.copyWithCount(1), spearType);
        spearEntity.pickup = AbstractArrow.Pickup.ALLOWED;
        return spearEntity;
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_TRIDENT_ACTIONS.contains(itemAbility);
    }

    private static Tool createToolProperties() {
        return new Tool(List.of(), 1.0F, 2);
    }

    private static ItemAttributeModifiers createAttributes(SpearType spearType) {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(BASE_ATTACK_DAMAGE_ID, spearType.baseDamage, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED,
                        new AttributeModifier(BASE_ATTACK_SPEED_ID, spearType.attackSpeed, AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.MAINHAND
                )
                .build();
    }
}
