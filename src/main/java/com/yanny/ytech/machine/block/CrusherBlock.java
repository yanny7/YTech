package com.yanny.ytech.machine.block;

import com.yanny.ytech.configuration.YTechConfigLoader;
import com.yanny.ytech.machine.block_entity.CrusherBlockEntity;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

class CrusherBlock extends YTechBlock {
    public CrusherBlock(YTechConfigLoader.Machine machine, YTechConfigLoader.Tier tier) {
        super(machine, tier);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return new CrusherBlockEntity(Registration.REGISTRATION_HOLDER.machine().get(machine).get(tier).blockEntityType().get(), pos, blockState);
    }
}
