package com.yanny.ytech.configuration.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class CustomRendererBakedModel implements BakedModel {
    @NotNull private final BakedModel existingModel;

    public CustomRendererBakedModel(@NotNull BakedModel existingModel) {
        this.existingModel = existingModel;
    }

    @NotNull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, @NotNull RandomSource random) {
        //noinspection deprecation
        return existingModel.getQuads(state, direction, random);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return existingModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return existingModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return existingModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @NotNull
    @Override
    public TextureAtlasSprite getParticleIcon() {
        //noinspection deprecation
        return existingModel.getParticleIcon();
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public ItemTransforms getTransforms() {
        return existingModel.getTransforms();
    }

    @NotNull
    @Override
    public ItemOverrides getOverrides() {
        return existingModel.getOverrides();
    }
}
