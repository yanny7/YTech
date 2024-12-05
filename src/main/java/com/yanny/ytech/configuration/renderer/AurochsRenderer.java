package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.AurochsEntity;
import com.yanny.ytech.configuration.model.AurochsModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class AurochsRenderer extends AgeableMobRenderer<AurochsEntity, LivingEntityRenderState, AurochsModel> {
    public AurochsRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new AurochsModel(context.bakeLayer(AurochsModel.LAYER_LOCATION)), new AurochsModel(context.bakeLayer(AurochsModel.LAYER_LOCATION)), 0.7f);
    }

    @NotNull
    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull LivingEntityRenderState state) {
        return Utils.modLoc("textures/entity/aurochs.png");
    }
}
