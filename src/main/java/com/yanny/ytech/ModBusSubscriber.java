package com.yanny.ytech;

import com.yanny.ytech.compatibility.TopCompatibility;
import com.yanny.ytech.configuration.SpearType;
import com.yanny.ytech.configuration.block_entity.WellPulleyBlockEntity;
import com.yanny.ytech.configuration.entity.*;
import com.yanny.ytech.configuration.item.BasketItem;
import com.yanny.ytech.configuration.item.DiviningRodItem;
import com.yanny.ytech.configuration.item.SpearItem;
import com.yanny.ytech.configuration.model.*;
import com.yanny.ytech.configuration.renderer.*;
import com.yanny.ytech.configuration.screen.AqueductFertilizerScreen;
import com.yanny.ytech.configuration.screen.PrimitiveAlloySmelterScreen;
import com.yanny.ytech.configuration.screen.PrimitiveSmelterScreen;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechEntityTypes;
import com.yanny.ytech.registration.YTechItems;
import com.yanny.ytech.registration.YTechMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelAccessor;
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
        event.enqueueWork(() -> {
            ItemProperties.register(YTechItems.BASKET.get(), BasketItem.FILLED_PREDICATE,
                    (stack, level, entity, seed) -> BasketItem.getFullnessDisplay(stack));
            ItemProperties.register(YTechItems.DIVINING_ROD.get(), DiviningRodItem.ABUNDANCE_PREDICATE,
                    (stack, level, entity, seed) -> entity != null ? (float) WellPulleyBlockEntity.getWaterAbundance(entity.getOnPos()) : 1);
            YTechItems.SPEARS.values().forEach((item) -> ItemProperties.register(item.get(), SpearItem.THROWING_PREDICATE,
                    (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F));

            MenuScreens.register(YTechMenuTypes.AQUEDUCT_FERTILIZER.get(), AqueductFertilizerScreen::new);
            MenuScreens.register(YTechMenuTypes.PRIMITIVE_ALLOY_SMELTER.get(), PrimitiveAlloySmelterScreen::new);
            MenuScreens.register(YTechMenuTypes.PRIMITIVE_SMELTER.get(), PrimitiveSmelterScreen::new);
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerEntityRenderer(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.AQUEDUCT.get(), AqueductRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.BRONZE_ANVIL.get(), BronzeAnvilRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.DRYING_RACK.get(), DryingRackRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.CRAFTING_WORKSPACE.get(), CraftingWorkspaceRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.FIRE_PIT.get(), FirePitRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.MILLSTONE.get(), MillstoneRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.POTTERS_WHEEL.get(), PottersWheelRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.TANNING_RACK.get(), TanningRackRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.TOOL_RACK.get(), ToolRackRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.TREE_STUMP.get(), TreeStumpRenderer::new);
        event.registerBlockEntityRenderer(YTechBlockEntityTypes.WOODEN_BOX.get(), WoodenBoxRenderer::new);

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
        event.put(YTechEntityTypes.FOWL.get(), FowlEntity.createAttributes().build());
        event.put(YTechEntityTypes.GO_AROUND.get(), GoAroundEntity.createAttributes().build());
        event.put(YTechEntityTypes.MOUFLON.get(), MouflonEntity.createAttributes().build());
        event.put(YTechEntityTypes.SABER_TOOTH_TIGER.get(), SaberToothTigerEntity.createAttributes().build());
        event.put(YTechEntityTypes.TERROR_BIRD.get(), TerrorBirdEntity.createAttributes().build());
        event.put(YTechEntityTypes.WILD_BOAR.get(), WildBoarEntity.createAttributes().build());
        event.put(YTechEntityTypes.WOOLLY_MAMMOTH.get(), WoollyMammothEntity.createAttributes().build());
        event.put(YTechEntityTypes.WOOLLY_RHINO.get(), WoollyRhinoEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        event.register(YTechEntityTypes.AUROCHS.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.FOWL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.MOUFLON.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.SABER_TOOTH_TIGER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.TERROR_BIRD.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.WILD_BOAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.WOOLLY_MAMMOTH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(YTechEntityTypes.WOOLLY_RHINO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

        event.register(EntityType.CHICKEN, ModBusSubscriber::removeAnimalPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityType.COW, ModBusSubscriber::removeAnimalPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityType.PIG, ModBusSubscriber::removeAnimalPredicate, SpawnPlacementRegisterEvent.Operation.AND);
        event.register(EntityType.SHEEP, ModBusSubscriber::removeAnimalPredicate, SpawnPlacementRegisterEvent.Operation.AND);
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

    public static boolean removeAnimalPredicate(EntityType<? extends Animal> pAnimal, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return YTechMod.CONFIGURATION.removeVanillaMobs();
    }
}
