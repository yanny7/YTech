package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.FowlEntity;
import com.yanny.ytech.configuration.model.FowlModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.ChickenRenderState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FowlRenderer extends MobRenderer<FowlEntity, ChickenRenderState, FowlModel> {
    public FowlRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new FowlModel(context.bakeLayer(FowlModel.LAYER_LOCATION)), 0.3f);
    }

    @NotNull
    @Override
    public ChickenRenderState createRenderState() {
        return new ChickenRenderState();
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull ChickenRenderState entity) {
        return Utils.modLoc("textures/entity/fowl.png");
    }
}
