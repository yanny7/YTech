package com.yanny.ytech.registration;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yanny.ytech.configuration.MaterialType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.function.Consumer;

public class YTechFluidType extends FluidType {
    private final MaterialType material;

    public YTechFluidType(MaterialType material) {
        super(getProperties(material));
        this.material = material;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getFlowingTexture() {
                return new ResourceLocation("forge", "block/milk_flowing");
            }

            @Override
            public ResourceLocation getStillTexture() {
                return new ResourceLocation("forge", "block/milk_still");
            }

            @Override
            public int getTintColor() {
                return material.color;
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level, int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                float r = ((material.color >> 16) & 0xff) / 255f;
                float g = ((material.color >> 8) & 0xff) / 255f;
                float b = (material.color & 0xff) / 255f;
                return new Vector3f(r, g, b);
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick, float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(0.1f);
                RenderSystem.setShaderFogEnd(1.1f);
            }
        });
    }

    @Override
    public boolean isVaporizedOnPlacement(Level level, BlockPos pos, FluidStack stack) {
        return false;
    }

    private static Properties getProperties(MaterialType material) {
        return FluidType.Properties.create();
    }
}
