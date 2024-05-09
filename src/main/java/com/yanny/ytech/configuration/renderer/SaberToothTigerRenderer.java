package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.SaberToothTigerEntity;
import com.yanny.ytech.configuration.model.SaberToothTigerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SaberToothTigerRenderer extends MobRenderer<SaberToothTigerEntity, SaberToothTigerModel> {
    public SaberToothTigerRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new SaberToothTigerModel(context.bakeLayer(SaberToothTigerModel.LAYER_LOCATION)), 0.8f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull SaberToothTigerEntity entity) {
        return Utils.modLoc("textures/entity/saber_tooth_tiger.png");
    }
}
