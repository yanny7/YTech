package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.TerrorBirdEntity;
import com.yanny.ytech.configuration.model.TerrorBirdModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class TerrorBirdRenderer extends MobRenderer<TerrorBirdEntity, TerrorBirdModel> {
    public TerrorBirdRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new TerrorBirdModel(context.bakeLayer(TerrorBirdModel.LAYER_LOCATION)), 0.8f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull TerrorBirdEntity entity) {
        return Utils.modLoc("textures/entity/terror_bird.png");
    }
}
