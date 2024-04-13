package com.yanny.ytech;

import com.yanny.ytech.compatibility.TopCompatibility;
import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.SimpleBlockType;
import com.yanny.ytech.configuration.SimpleEntityType;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.block.IMenuBlock;
import com.yanny.ytech.configuration.block_entity.IrrigationBlockEntity;
import com.yanny.ytech.configuration.entity.GoAroundEntity;
import com.yanny.ytech.configuration.item.BasketItem;
import com.yanny.ytech.configuration.item.SpearItem;
import com.yanny.ytech.configuration.model.CustomRendererBakedModel;
import com.yanny.ytech.configuration.model.DeerModel;
import com.yanny.ytech.configuration.renderer.*;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.irrigation.IrrigationUtils;
import com.yanny.ytech.network.kinetic.KineticUtils;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

import static com.yanny.ytech.configuration.SpearType.BY_ENTITY_TYPE;
import static com.yanny.ytech.configuration.model.SpearModel.*;
import static com.yanny.ytech.registration.Registration.HOLDER;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusSubscriber {
    private static final String PROTOCOL_VERSION = "1";

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(@NotNull FMLClientSetupEvent event) {
        TopCompatibility.register();
        ItemProperties.register(Registration.item(SimpleItemType.BASKET), BasketItem.FILLED_PREDICATE,
                (stack, level, entity, seed) -> BasketItem.getFullnessDisplay(stack));

        HOLDER.items().get(MaterialItemType.SPEAR).forEach((material, holder) -> ItemProperties.register(holder.item.get(), SpearItem.THROWING_PREDICATE,
                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));

        event.enqueueWork(() -> {
            GeneralUtils.mapToStream(HOLDER.blocks()).forEach((blockHolder) -> {
                if (blockHolder instanceof Holder.MenuEntityBlockHolder holder && holder.block.get() instanceof IMenuBlock menuBlock) {
                    MenuScreens.register(holder.getMenuType(), menuBlock::getScreen);
                }
            });
            HOLDER.simpleBlocks().values().forEach((blockHolder) -> {
                if (blockHolder instanceof Holder.MenuEntitySimpleBlockHolder holder && holder.block.get() instanceof IMenuBlock menuBlock) {
                    MenuScreens.register(holder.getMenuType(), menuBlock::getScreen);
                }
            });
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderer(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        HOLDER.simpleBlocks().forEach((blockType, blockHolder) -> {
            if (blockHolder instanceof Holder.EntitySimpleBlockHolder holder) {
                switch (blockType) {
                    case MILLSTONE -> event.registerBlockEntityRenderer(holder.getBlockEntityType(), MillstoneRenderer::new);
                    case BRONZE_ANVIL -> event.registerBlockEntityRenderer(holder.getBlockEntityType(), BronzeAnvilRenderer::new);
                    case AQUEDUCT -> event.registerBlockEntityRenderer(holder.getBlockEntityType(), AqueductRenderer::new);
                }
            }
        });
        HOLDER.blocks().forEach((blockType, map) -> map.forEach((material, blockHolder) -> {
            if (blockHolder instanceof Holder.EntityBlockHolder holder) {
                switch (blockType) {
                    case SHAFT, WATER_WHEEL -> event.registerBlockEntityRenderer(holder.getBlockEntityType(), KineticRenderer::new);
                    case DRYING_RACK -> event.registerBlockEntityRenderer(holder.getBlockEntityType(), DryingRackRenderer::new);
                    case TANNING_RACK -> event.registerBlockEntityRenderer(holder.getBlockEntityType(), TanningRackRenderer::new);
                }
            }
        }));
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

    @SubscribeEvent
    public static void onRegisterCap(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlock(Capabilities.FluidHandler.BLOCK, (level, pos, state, be, side) -> {
            if (!level.isClientSide && be instanceof IrrigationBlockEntity irrigationBlockEntity) {
                IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(irrigationBlockEntity);

                if (network != null) {
                    return network.getFluidHandler();
                }
            }

            return null;
        }, Registration.block(SimpleBlockType.AQUEDUCT));
    }

    @SubscribeEvent
    public static void registerPayloadHandler(final RegisterPayloadHandlerEvent event) {
        final IPayloadRegistrar registrar = event.registrar(YTechMod.MOD_ID).versioned(PROTOCOL_VERSION);

        YTechMod.KINETIC_PROPAGATOR = KineticUtils.registerKineticPropagator(registrar);
        YTechMod.IRRIGATION_PROPAGATOR = IrrigationUtils.registerIrrigationPropagator(registrar);
    }
}
