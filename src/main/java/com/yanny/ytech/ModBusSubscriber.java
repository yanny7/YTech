package com.yanny.ytech;

import com.yanny.ytech.compatibility.TopCompatibility;
import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block.IMenuBlock;
import com.yanny.ytech.configuration.item.BasketItem;
import com.yanny.ytech.configuration.model.DeerModel;
import com.yanny.ytech.configuration.recipe.TagStackIngredient;
import com.yanny.ytech.configuration.renderer.*;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

import static com.yanny.ytech.registration.Registration.HOLDER;

@Mod.EventBusSubscriber(modid = YTechMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBusSubscriber {
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void clientSetup(@NotNull FMLClientSetupEvent event) {
        TopCompatibility.register();
        ItemProperties.register(Registration.item(SimpleItemType.BASKET), Utils.modLoc("filled"), (stack, level, entity, seed) -> BasketItem.getFullnessDisplay(stack));

        event.enqueueWork(() -> {
            GeneralUtils.mapToStream(HOLDER.blocks()).forEach((blockHolder) -> {
                if (blockHolder instanceof Holder.MenuEntityBlockHolder holder && holder.block.get() instanceof IMenuBlock menuBlock) {
                    MenuScreens.register(holder.menuType.get(), menuBlock::getScreen);
                }
            });
            HOLDER.simpleBlocks().values().forEach((blockHolder) -> {
                if (blockHolder instanceof Holder.MenuEntitySimpleBlockHolder holder && holder.block.get() instanceof IMenuBlock menuBlock) {
                    MenuScreens.register(holder.menuType.get(), menuBlock::getScreen);
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
                    case MILLSTONE -> event.registerBlockEntityRenderer(holder.entityType.get(), MillstoneRenderer::new);
                    case BRONZE_ANVIL -> event.registerBlockEntityRenderer(holder.entityType.get(), BronzeAnvilRenderer::new);
                }
            }
        });
        HOLDER.blocks().forEach((blockType, map) -> map.forEach((material, blockHolder) -> {
            if (blockHolder instanceof Holder.EntityBlockHolder holder) {
                switch (blockType) {
                    case SHAFT, WATER_WHEEL -> event.registerBlockEntityRenderer(holder.entityType.get(), KineticRenderer::new);
                    case DRYING_RACK -> event.registerBlockEntityRenderer(holder.entityType.get(), DryingRackRenderer::new);
                    case TANNING_RACK -> event.registerBlockEntityRenderer(holder.entityType.get(), TanningRackRenderer::new);
                }
            }
        }));
        HOLDER.entities().forEach((type, holder) -> {
            switch (type) {
                case DEER -> event.registerEntityRenderer(holder.entityType.get(), (context) -> new DeerRenderer<>(context, 0.5f));
                default -> throw new IllegalStateException("Missing entity renderer!");
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
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
        event.register(Utils.modBlockLoc("millstone_upper_part"));
    }

    @SubscribeEvent
    public static void registerRecipeSerializers(@NotNull RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(Utils.modLoc("tag_stack"), TagStackIngredient.SERIALIZER);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        Registration.HOLDER.entities().forEach((type, holder) -> event.put(holder.entityType.get(), holder.object.getAttributes()));
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event) {
        Registration.HOLDER.entities().forEach((type, holder) -> event.register(holder.entityType.get(), holder.object.spawnPlacement,
                holder.object.heightMapType, holder.object.spawnPredicate, SpawnPlacementRegisterEvent.Operation.OR));
    }
}
