package com.yanny.ytech;

import com.yanny.ytech.compatibility.TopCompatibility;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.block.IMenuBlock;
import com.yanny.ytech.configuration.entity.DeerEntity;
import com.yanny.ytech.configuration.entity.GoAroundEntity;
import com.yanny.ytech.configuration.item.BasketItem;
import com.yanny.ytech.configuration.item.SpearItem;
import com.yanny.ytech.configuration.model.CustomRendererBakedModel;
import com.yanny.ytech.configuration.model.DeerModel;
import com.yanny.ytech.configuration.renderer.*;
import com.yanny.ytech.registration.*;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

import static com.yanny.ytech.configuration.model.SpearModel.*;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusSubscriber {
    @SubscribeEvent
    public static void commonSetup(@NotNull FMLCommonSetupEvent event) {
        TopCompatibility.register();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(@NotNull FMLClientSetupEvent event) {
        ItemProperties.register(YTechItems.BASKET.get(), BasketItem.FILLED_PREDICATE,
                (stack, level, entity, seed) -> BasketItem.getFullnessDisplay(stack));
        YTechItems.SPEARS.items().forEach((item) -> ItemProperties.register(item.get(), SpearItem.THROWING_PREDICATE,
                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));

        event.enqueueWork(() -> {
            MenuScreens.register(YTechMenuTypes.PRIMITIVE_ALLOY_SMELTER.get(), ((IMenuBlock) YTechBlocks.PRIMITIVE_ALLOY_SMELTER.get())::getScreen);
            MenuScreens.register(YTechMenuTypes.PRIMITIVE_SMELTER.get(), ((IMenuBlock) YTechBlocks.PRIMITIVE_SMELTER.get())::getScreen);
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderer(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.AQUEDUCT_FERTILIZER.get(), AqueductRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.BRONZE_ANVIL.get(), BronzeAnvilRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.MILLSTONE.get(), MillstoneRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.DRYING_RACK.get(), DryingRackRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.TANNING_RACK.get(), TanningRackRenderer::new);

        event.registerEntityRenderer(YTechEntityTypes.FLINT_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.FLINT)));
        event.registerEntityRenderer(YTechEntityTypes.COPPER_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.COPPER)));
        event.registerEntityRenderer(YTechEntityTypes.BRONZE_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.BRONZE)));
        event.registerEntityRenderer(YTechEntityTypes.IRON_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.IRON)));
        event.registerEntityRenderer(YTechEntityTypes.PEBBLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.GO_AROUND.get(), GoAroundRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.DEER.get(), DeerRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.BRONZE), () -> createLayer(0, 12));
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.COPPER), () -> createLayer(0, 6));
        event.registerLayerDefinition(DeerModel.LAYER_LOCATION, DeerModel::createBodyLayer);
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.FLINT), () -> createLayer(0, 0));
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.IRON), () -> createLayer(0, 18));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerModel(@NotNull ModelEvent.RegisterAdditional event) {
        for (SpearType spearType : SpearType.values()) {
            event.register(MODEL_IN_HAND_LOCATIONS.get(spearType));
        }
    }

    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(YTechEntityTypes.DEER.get(), DeerEntity.createAttributes().build());
        event.put(YTechEntityTypes.GO_AROUND.get(), GoAroundEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        event.register(YTechEntityTypes.DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
        Stream.of(SpearType.values()).forEach(spearType -> {
            ModelResourceLocation modelLocation = MODEL_LOCATIONS.get(spearType);
            BakedModel existingModel = modelRegistry.get(modelLocation);

            if (existingModel == null) {
                throw new RuntimeException("Missing model for " + spearType);
            } else {
                modelRegistry.put(modelLocation, new CustomRendererBakedModel(existingModel));
            }
        });
    }
}
