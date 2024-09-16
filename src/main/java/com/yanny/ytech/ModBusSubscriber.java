package com.yanny.ytech;

import com.yanny.ytech.compatibility.CuriosCapability;
import com.yanny.ytech.compatibility.TopCompatibility;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.block_entity.IrrigationBlockEntity;
import com.yanny.ytech.configuration.data_component.BasketContents;
import com.yanny.ytech.configuration.entity.*;
import com.yanny.ytech.configuration.item.BasketItem;
import com.yanny.ytech.configuration.item.SpearItem;
import com.yanny.ytech.configuration.model.*;
import com.yanny.ytech.configuration.renderer.*;
import com.yanny.ytech.configuration.screen.AqueductFertilizerScreen;
import com.yanny.ytech.configuration.screen.PrimitiveAlloySmelterScreen;
import com.yanny.ytech.configuration.screen.PrimitiveSmelterScreen;
import com.yanny.ytech.configuration.tooltip.ClientBasketTooltip;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.network.irrigation.IrrigationUtils;
import com.yanny.ytech.registration.*;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.stream.Stream;

import static com.yanny.ytech.configuration.model.SpearModel.*;

@EventBusSubscriber(modid = YTechMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModBusSubscriber {
    private static final String PROTOCOL_VERSION = "1";

    @SubscribeEvent
    public static void commonSetupEvent(@NotNull FMLCommonSetupEvent event) {
        if (ModList.get().isLoaded("curios")) {
            CuriosCapability.register();
        }
        TopCompatibility.register();
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(@NotNull FMLClientSetupEvent event) {
        ItemProperties.register(YTechItems.BASKET.get(), BasketItem.FILLED_PREDICATE,
                (stack, level, entity, seed) -> BasketItem.getFullnessDisplay(stack));
        YTechItems.SPEARS.items().forEach((item) -> ItemProperties.register(item.get(), SpearItem.THROWING_PREDICATE,
                (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(YTechMenuTypes.AQUEDUCT_FERTILIZER.get(), AqueductFertilizerScreen::new);
        event.register(YTechMenuTypes.PRIMITIVE_ALLOY_SMELTER.get(), PrimitiveAlloySmelterScreen::new);
        event.register(YTechMenuTypes.PRIMITIVE_SMELTER.get(), PrimitiveSmelterScreen::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterClientTooltipComponentFactories(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(BasketContents.BasketTooltip.class, ClientBasketTooltip::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions clientItemExtensions = new IClientItemExtensions() {
            @NotNull
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return YTechRenderer.INSTANCE;
            }
        };

        event.registerItem(clientItemExtensions, YTechItems.SPEARS.items().stream().map(DeferredHolder::get).toArray(Item[]::new));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderer(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.AQUEDUCT.get(), AqueductRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.BRONZE_ANVIL.get(), BronzeAnvilRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.MILLSTONE.get(), MillstoneRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.POTTERS_WHEEL.get(), PottersWheelRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.DRYING_RACK.get(), DryingRackRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.TANNING_RACK.get(), TanningRackRenderer::new);

        event.registerEntityRenderer(YTechEntityTypes.FLINT_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.FLINT)));
        event.registerEntityRenderer(YTechEntityTypes.COPPER_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.COPPER)));
        event.registerEntityRenderer(YTechEntityTypes.BRONZE_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.BRONZE)));
        event.registerEntityRenderer(YTechEntityTypes.IRON_SPEAR.get(), context -> new SpearRenderer(context, LAYER_LOCATIONS.get(SpearType.IRON)));
        event.registerEntityRenderer(YTechEntityTypes.PEBBLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.GO_AROUND.get(), GoAroundRenderer::new);

        event.registerEntityRenderer(YTechEntityTypes.AUROCHS.get(), AurochsRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.DEER.get(), DeerRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.FOWL.get(), FowlRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.MOUFLON.get(), MouflonRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.SABER_TOOTH_TIGER.get(), SaberToothTigerRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.TERROR_BIRD.get(), TerrorBirdRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.WILD_BOAR.get(), WildBoarRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.WOOLLY_MAMMOTH.get(), WoollyMammothRenderer::new);
        event.registerEntityRenderer(YTechEntityTypes.WOOLLY_RHINO.get(), WoollyRhinoRenderer::new);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.BRONZE), () -> createLayer(0, 12));
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.COPPER), () -> createLayer(0, 6));
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.FLINT), () -> createLayer(0, 0));
        event.registerLayerDefinition(LAYER_LOCATIONS.get(SpearType.IRON), () -> createLayer(0, 18));

        event.registerLayerDefinition(AurochsModel.LAYER_LOCATION, AurochsModel::createBodyLayer);
        event.registerLayerDefinition(DeerModel.LAYER_LOCATION, DeerModel::createBodyLayer);
        event.registerLayerDefinition(FowlModel.LAYER_LOCATION, FowlModel::createBodyLayer);
        event.registerLayerDefinition(MouflonModel.LAYER_LOCATION, MouflonModel::createBodyLayer);
        event.registerLayerDefinition(SaberToothTigerModel.LAYER_LOCATION, SaberToothTigerModel::createBodyLayer);
        event.registerLayerDefinition(TerrorBirdModel.LAYER_LOCATION, TerrorBirdModel::createBodyLayer);
        event.registerLayerDefinition(WildBoarModel.LAYER_LOCATION, WildBoarModel::createBodyLayer);
        event.registerLayerDefinition(WoollyMammothModel.LAYER_LOCATION, WoollyMammothModel::createBodyLayer);
        event.registerLayerDefinition(WoollyRhinoModel.LAYER_LOCATION, WoollyRhinoModel::createBodyLayer);
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
        event.put(YTechEntityTypes.AUROCHS.get(), AurochsEntity.createAttributes().build());
        event.put(YTechEntityTypes.DEER.get(), DeerEntity.createAttributes().build());
        event.put(YTechEntityTypes.FOWL.get(),  FowlEntity.createAttributes().build());
        event.put(YTechEntityTypes.GO_AROUND.get(), GoAroundEntity.createAttributes().build());
        event.put(YTechEntityTypes.MOUFLON.get(), MouflonEntity.createAttributes().build());
        event.put(YTechEntityTypes.SABER_TOOTH_TIGER.get(), SaberToothTigerEntity.createAttributes().build());
        event.put(YTechEntityTypes.TERROR_BIRD.get(), TerrorBirdEntity.createAttributes().build());
        event.put(YTechEntityTypes.WILD_BOAR.get(), WildBoarEntity.createAttributes().build());
        event.put(YTechEntityTypes.WOOLLY_MAMMOTH.get(), WoollyMammothEntity.createAttributes().build());
        event.put(YTechEntityTypes.WOOLLY_RHINO.get(), WoollyRhinoEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(RegisterSpawnPlacementsEvent event) {
        event.register(YTechEntityTypes.AUROCHS.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.DEER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.FOWL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.MOUFLON.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.SABER_TOOTH_TIGER.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.TERROR_BIRD.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.WILD_BOAR.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.WOOLLY_MAMMOTH.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
        event.register(YTechEntityTypes.WOOLLY_RHINO.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);

        event.register(EntityType.CHICKEN, ModBusSubscriber::removeAnimalPredicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityType.COW, ModBusSubscriber::removeAnimalPredicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityType.PIG, ModBusSubscriber::removeAnimalPredicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(EntityType.SHEEP, ModBusSubscriber::removeAnimalPredicate, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();
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
        }, YTechBlocks.AQUEDUCT.get());
    }

    @SubscribeEvent
    public static void registerPayloadHandler(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);

        YTechMod.IRRIGATION_PROPAGATOR = IrrigationUtils.registerIrrigationPropagator(registrar);
    }

    public static boolean removeAnimalPredicate(EntityType<? extends Animal> pAnimal, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return false;
    }
}
