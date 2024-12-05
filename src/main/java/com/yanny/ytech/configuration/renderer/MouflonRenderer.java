package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.MouflonEntity;
import com.yanny.ytech.configuration.model.MouflonModel;
import net.minecraft.client.renderer.entity.AgeableMobRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MouflonRenderer extends AgeableMobRenderer<MouflonEntity, LivingEntityRenderState, MouflonModel> {
    public MouflonRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new MouflonModel(context.bakeLayer(MouflonModel.LAYER_LOCATION)), new MouflonModel(context.bakeLayer(MouflonModel.LAYER_LOCATION)), 0.7f);
    }

    @NotNull
    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull LivingEntityRenderState entity) {
        return Utils.modLoc("textures/entity/mouflon.png");
    }
}
