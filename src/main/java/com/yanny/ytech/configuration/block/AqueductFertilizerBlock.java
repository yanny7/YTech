package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.*;
import com.yanny.ytech.configuration.block_entity.AqueductFertilizerBlockEntity;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.configuration.screen.AqueductFertilizerScreen;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.Tags;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class AqueductFertilizerBlock extends AqueductHydratorBlock implements IMenuBlock {
    public AqueductFertilizerBlock(Holder.SimpleBlockHolder holder) {
        super(holder);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        if (holder instanceof Holder.EntitySimpleBlockHolder blockHolder) {
            return new AqueductFertilizerBlockEntity(holder, blockHolder.getBlockEntityType(), pos, blockState);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult trace) {
        if (!level.isClientSide) {
            NetworkHooks.openScreen((ServerPlayer) player, getMenuProvider(state, level, pos), pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        if (!level.isClientSide) {
            return AqueductConsumerBlock::createAqueductConsumerTicker;
        } else {
            return null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @NotNull
    @Override
    public AbstractContainerScreen<AbstractContainerMenu> getScreen(@NotNull AbstractContainerMenu container, @NotNull Inventory inventory, @NotNull Component title) {
        AbstractContainerScreen<?> screen = new AqueductFertilizerScreen(container, inventory, title);
        //noinspection unchecked
        return (AbstractContainerScreen<AbstractContainerMenu>)screen;
    }

    @NotNull
    public static TextureHolder[] getTexture() {
        return List.of(
                new TextureHolder(-1, -1, Utils.modBlockLoc("aqueduct/aqueduct_fertilizer")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("aqueduct/aqueduct_valve")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("terracotta_bricks")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("invisible")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("aqueduct/aqueduct_fertilizer_working"))
        ).toArray(TextureHolder[]::new);
    }

    public static void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ModelFile base = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case EAST -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case SOUTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("0", textures[0])
                .texture("2", textures[2])
                .texture("particle", textures[0]);
        ModelFile waterlogged = provider.models().getBuilder(holder.key + "_waterlogged")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case EAST -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case SOUTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#2");
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#4");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#4");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("2", textures[2])
                .texture("4", textures[4])
                .texture("particle", textures[0]);
        ModelFile overlay = provider.models().getBuilder(holder.key + "_overlay")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    if (Objects.requireNonNull(direction) == Direction.NORTH) {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                    } else {
                        faceBuilder.uvs(0, 0, 16, 16).texture("#3");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .renderType("minecraft:cutout")
                .texture("1", textures[1])
                .texture("3", textures[3]);
        ModelFile inventory = provider.models().getBuilder(holder.key + "_inventory")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case EAST -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case SOUTH -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case WEST -> faceBuilder.uvs(0, 0, 16, 16).texture("#1");
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 16, 16).end()
                .texture("0", textures[0])
                .texture("1", textures[1])
                .texture("particle", textures[0]);

        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(holder.block.get());
        builder.part().modelFile(base).addModel().condition(BlockStateProperties.WATERLOGGED, false).end();
        builder.part().modelFile(waterlogged).addModel().condition(BlockStateProperties.WATERLOGGED, true).end();
        PROPERTY_BY_DIRECTION.forEach((dir, value) -> builder.part().modelFile(overlay).rotationY(ANGLE_BY_DIRECTION.get(dir)).addModel().condition(value, true).end());

        provider.itemModels().getBuilder(holder.key).parent(inventory);
    }

    public static void registerRecipe(Holder.SimpleBlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('#', Registration.item(SimpleBlockType.AQUEDUCT_HYDRATOR))
                .define('R', MaterialItemType.ROD.itemTag.get(MaterialType.BRONZE))
                .define('S', MaterialItemType.PLATE.itemTag.get(MaterialType.BRONZE))
                .define('B', MaterialItemType.BOLT.itemTag.get(MaterialType.BRONZE))
                .define('H', MaterialItemType.HAMMER.groupTag)
                .define('F', MaterialItemType.FILE.groupTag)
                .define('C', Tags.Items.CHESTS)
                .pattern("HCF")
                .pattern("S#S")
                .pattern("RBR")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(SimpleBlockType.TERRACOTTA_BRICKS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }
}
