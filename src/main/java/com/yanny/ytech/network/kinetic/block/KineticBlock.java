package com.yanny.ytech.network.kinetic.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.ConfigLoader;
import com.yanny.ytech.network.kinetic.KineticUtils;
import com.yanny.ytech.network.kinetic.common.IKineticBlockEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class KineticBlock extends BaseEntityBlock implements IProbeInfoProvider {
    protected final ConfigLoader.KineticMaterial material;

    protected KineticBlock(Properties properties, ConfigLoader.KineticMaterial material) {
        super(properties);
        this.material = material;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRemove(@NotNull BlockState oldBlockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newBlockState, boolean b) {
        if (!level.isClientSide) {
            if (oldBlockState.hasBlockEntity() && (!oldBlockState.is(newBlockState.getBlock()) || !newBlockState.hasBlockEntity())) {
                if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity && kineticBlockEntity.getNetworkId() >= 0) {
                    kineticBlockEntity.onRemove();
                }
            } else if (oldBlockState.hasBlockEntity() && oldBlockState.is(newBlockState.getBlock())) {
                if (level.getBlockEntity(pos) instanceof IKineticBlockEntity kineticBlockEntity) {
                    kineticBlockEntity.onChangedState(oldBlockState, newBlockState);
                }
            }
        }

        super.onRemove(oldBlockState, level, pos, newBlockState, b);
    }

    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(YTechMod.MOD_ID, getClass().getName());
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
        if (!level.isClientSide && level.getBlockEntity(probeHitData.getPos()) instanceof IKineticBlockEntity blockEntity) {
            KineticUtils.addKineticInfo(probeInfo, blockEntity);
        }
    }
}
