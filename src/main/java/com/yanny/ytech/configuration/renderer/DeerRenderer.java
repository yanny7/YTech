package com.yanny.ytech.configuration.renderer;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.model.DeerModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DeerRenderer<T extends Animal> extends MobRenderer<T, DeerModel<T>> {
    public DeerRenderer(@NotNull EntityRendererProvider.Context context, float shadowRadius) {
        super(context, new DeerModel<>(context.bakeLayer(DeerModel.LAYER_LOCATION)), shadowRadius);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull T entity) {
        return Utils.modLoc("textures/entity/deer.png");
    }
}
