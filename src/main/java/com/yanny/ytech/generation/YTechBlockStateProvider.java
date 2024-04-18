package com.yanny.ytech.generation;

import com.yanny.ytech.GeneralUtils;
import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.block.BronzeAnvilBlock;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.yanny.ytech.registration.Registration.HOLDER;

class YTechBlockStateProvider extends BlockStateProvider {
    public YTechBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, YTechMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        BronzeAnvilBlock.registerModel(this);

        GeneralUtils.mapToStream(HOLDER.blocks()).forEach((m) -> m.object.registerModel(m, this));
        HOLDER.simpleBlocks().values().forEach((h) -> h.object.registerModel(h, this));
    }
}
