package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.WildBoarEntity;
import com.yanny.ytech.configuration.model.WildBoarModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class WildBoarRenderer extends MobRenderer<WildBoarEntity, WildBoarModel> {
    public WildBoarRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new WildBoarModel(context.bakeLayer(WildBoarModel.LAYER_LOCATION)), 0.7f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull WildBoarEntity entity) {
        return Utils.modLoc("textures/entity/wild_boar.png");
    }
}
