package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.WoollyRhinoEntity;
import com.yanny.ytech.configuration.model.WoollyRhinoModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class WoollyRhinoRenderer extends MobRenderer<WoollyRhinoEntity, LivingEntityRenderState, WoollyRhinoModel> {
    public WoollyRhinoRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new WoollyRhinoModel(context.bakeLayer(WoollyRhinoModel.LAYER_LOCATION)), 0.7f);
    }

    @NotNull
    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull LivingEntityRenderState entity) {
        return Utils.modLoc("textures/entity/woolly_rhino.png");
    }
}
