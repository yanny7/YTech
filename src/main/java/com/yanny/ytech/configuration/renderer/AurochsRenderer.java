package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.AurochsEntity;
import com.yanny.ytech.configuration.model.AurochsModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class AurochsRenderer extends MobRenderer<AurochsEntity, AurochsModel> {
    public AurochsRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new AurochsModel(context.bakeLayer(AurochsModel.LAYER_LOCATION)), 0.7f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull AurochsEntity entity) {
        return Utils.modLoc("textures/entity/aurochs.png");
    }
}
