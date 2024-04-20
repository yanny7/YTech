package com.yanny.ytech;

import com.yanny.ytech.compatibility.TopCompatibility;
import com.yanny.ytech.configuration.SimpleEntityType;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.block.IMenuBlock;
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

import static com.yanny.ytech.configuration.SpearType.BY_ENTITY_TYPE;
import static com.yanny.ytech.configuration.model.SpearModel.*;
import static com.yanny.ytech.registration.Registration.HOLDER;

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

        HOLDER.simpleEntities().forEach((type, holder) -> {
            switch (type) {
                case FLINT_SPEAR, COPPER_SPEAR, BRONZE_SPEAR, IRON_SPEAR ->
                        event.registerEntityRenderer(holder.getEntityType(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(BY_ENTITY_TYPE.get(type))));
                case GO_AROUND -> event.registerEntityRenderer(holder.getEntityType(), GoAroundRenderer::new);
                case PEBBLE -> event.registerEntityRenderer(holder.getEntityType(), ThrownItemRenderer::new);
                default -> throw new IllegalStateException("Missing simple entity renderer!");
            }
        });
        HOLDER.entities().forEach((type, holder) -> {
            switch (type) {
                case DEER -> event.registerEntityRenderer(holder.getEntityType(), (context) -> new DeerRenderer<>(context, 0.5f));
                default -> throw new IllegalStateException("Missing entity renderer!");
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        HOLDER.simpleEntities().forEach((type, holder) -> {
            switch (type) {
                case FLINT_SPEAR -> event.registerLayerDefinition(LAYER_LOCATIONS.get(BY_ENTITY_TYPE.get(type)), () -> createLayer(0, 0));
                case COPPER_SPEAR -> event.registerLayerDefinition(LAYER_LOCATIONS.get(BY_ENTITY_TYPE.get(type)), () -> createLayer(0, 6));
                case BRONZE_SPEAR -> event.registerLayerDefinition(LAYER_LOCATIONS.get(BY_ENTITY_TYPE.get(type)), () -> createLayer(0, 12));
                case IRON_SPEAR -> event.registerLayerDefinition(LAYER_LOCATIONS.get(BY_ENTITY_TYPE.get(type)), () -> createLayer(0, 18));
                case GO_AROUND, PEBBLE -> {}
                default -> throw new IllegalStateException("Missing simple entity layer definitions!");
            }
        });
        HOLDER.entities().forEach((type, holder) -> {
            switch (type) {
                case DEER -> event.registerLayerDefinition(DeerModel.LAYER_LOCATION, DeerModel::createBodyLayer);
                default -> throw new IllegalStateException("Missing entity layer definitions!");
            }
        });
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
        Registration.HOLDER.entities().forEach((type, holder) -> event.put(holder.getEntityType(), holder.object.getAttributes()));
        event.put(Registration.entityType(SimpleEntityType.GO_AROUND), GoAroundEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        Registration.HOLDER.entities().forEach((type, holder) -> event.register(holder.getEntityType(), holder.object.spawnPlacement,
                holder.object.heightMapType, holder.object.spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR));
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
