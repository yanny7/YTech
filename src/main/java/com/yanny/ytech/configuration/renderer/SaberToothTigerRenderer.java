package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.SaberToothTigerEntity;
import com.yanny.ytech.configuration.model.SaberToothTigerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SaberToothTigerRenderer extends MobRenderer<SaberToothTigerEntity, LivingEntityRenderState, SaberToothTigerModel> {
    public SaberToothTigerRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new SaberToothTigerModel(context.bakeLayer(SaberToothTigerModel.LAYER_LOCATION)), 0.8f);
    }

    @NotNull
    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull LivingEntityRenderState entity) {
        return Utils.modLoc("textures/entity/saber_tooth_tiger.png");
    }
}
