package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.SimpleItemType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.MillstoneBlockEntity;
import com.yanny.ytech.registration.Holder;
import net.minecraft.core.BlockPos;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class MillstoneBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(0, 0, 0, 1, 0.5, 1),
            Shapes.box(6/16.0, 0.5, 6/16.0, 10/16.0, 14/16.0, 10/16.0)
    );

    private final Holder.SimpleBlockHolder holder;

    public MillstoneBlock(Holder.SimpleBlockHolder holder) {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.STONE)
                .requiresCorrectToolForDrops()
                .strength(3.5F));
        this.holder = holder;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
        return SHAPE;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        if (holder instanceof Holder.EntitySimpleBlockHolder blockHolder) {
            return new MillstoneBlockEntity(blockHolder.entityType.get(), pos, state);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return MillstoneBlock::createMillstoneTicker;
    }

    public static void createMillstoneTicker(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof MillstoneBlockEntity block) {
            block.tick(level, pos, state, block);
        }
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                 @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof  MillstoneBlockEntity millstone) {
            return millstone.onUse(state, level, pos, player, hand, hitResult);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    public static TextureHolder[] textureHolder() {
        return List.of(
                new TextureHolder(-1, -1, Utils.mcBlockLoc("smooth_stone_slab_side")),
                new TextureHolder(-1, -1, Utils.mcBlockLoc("smooth_stone")),
                new TextureHolder(-1, -1, Utils.mcBlockLoc("oak_log")),
                new TextureHolder(-1, -1, Utils.mcBlockLoc("oak_log_top"))
        ).toArray(TextureHolder[]::new);
    }

    public static void registerModel(@NotNull Holder.SimpleBlockHolder holder, @NotNull BlockStateProvider provider) {
        ResourceLocation[] textures = holder.object.getTextures();
        ModelFile model = provider.models().getBuilder(holder.key)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 4, 16, 8).texture("#side");
                        case EAST -> faceBuilder.uvs(0, 4, 16, 8).texture("#side");
                        case SOUTH -> faceBuilder.uvs(0, 4, 16, 8).texture("#side");
                        case WEST -> faceBuilder.uvs(0, 4, 16, 8).texture("#side");
                        case UP -> faceBuilder.uvs(0, 0, 16, 16).texture("#slab");
                        case DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#slab");
                    }
                })
                .from(0, 0, 0).to(16, 4, 16).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(12, 0, 16, 10).texture("#middle");
                        case EAST -> faceBuilder.uvs(0, 0, 4, 10).texture("#middle");
                        case SOUTH -> faceBuilder.uvs(4, 0, 8, 10).texture("#middle");
                        case WEST -> faceBuilder.uvs(8, 0, 12, 10).texture("#middle");
                        case UP -> faceBuilder.uvs(5, 5, 11, 11).texture("#top");
                    }
                })
                .from(6, 4, 6).to(10, 14, 10).end()
                .texture("particle", textures[0])
                .texture("side", textures[0])
                .texture("slab", textures[1])
                .texture("middle", textures[2])
                .texture("top", textures[3]);
        provider.getVariantBuilder(holder.block.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(holder.key).parent(model);
    }

    public static void registerRecipe(@NotNull Holder.SimpleBlockHolder holder, @NotNull Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('W', ItemTags.LOGS)
                .define('S', Items.SMOOTH_STONE_SLAB)
                .define('F', SimpleItemType.SHARP_FLINT.itemTag)
                .pattern("WF")
                .pattern("S ")
                .pattern("S ")
                .unlockedBy("has_logs", RecipeProvider.has(ItemTags.LOGS))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }
}
