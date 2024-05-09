package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.entity.FowlEntity;
import com.yanny.ytech.configuration.model.FowlModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FowlRenderer extends MobRenderer<FowlEntity, FowlModel> {
    public FowlRenderer(@NotNull EntityRendererProvider.Context context) {
        super(context, new FowlModel(context.bakeLayer(FowlModel.LAYER_LOCATION)), 0.3f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull FowlEntity entity) {
        return Utils.modLoc("textures/entity/fowl.png");
    }

    @Override
    protected float getBob(FowlEntity entity, float partialTick) {
        float flap = Mth.lerp(partialTick, entity.oFlap, entity.flap);
        float flapSpeed = Mth.lerp(partialTick, entity.oFlapSpeed, entity.flapSpeed);
        return (Mth.sin(flap) + 1.0F) * flapSpeed;
    }
}
