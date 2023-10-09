package com.yanny.ytech.configuration.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.MaterialItemType;
import com.yanny.ytech.configuration.SimpleBlockType;
import com.yanny.ytech.configuration.TextureHolder;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AqueductHydratorBlockEntity;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.registration.Holder;
import com.yanny.ytech.registration.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class AqueductHydratorBlock extends IrrigationBlock {
    public AqueductHydratorBlock() {
        super(Properties.copy(Blocks.TERRACOTTA));
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        Holder.SimpleBlockHolder blockHolder = Registration.HOLDER.simpleBlocks().get(SimpleBlockType.AQUEDUCT_HYDRATOR);

        if (blockHolder instanceof Holder.EntitySimpleBlockHolder holder) {
            return new AqueductHydratorBlockEntity(holder.entityType.get(), pos, blockState);
        } else {
            throw new IllegalStateException("Invalid holder type!");
        }
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext blockPlaceContext) {
        BlockState state = defaultBlockState();
        Level level = blockPlaceContext.getLevel();
        BlockPos pos = blockPlaceContext.getClickedPos();
        boolean hasNorthConnection = isValidForConnection(level, pos, Direction.NORTH);
        boolean hasEastConnection = isValidForConnection(level, pos, Direction.EAST);
        boolean hasSouthConnection = isValidForConnection(level, pos, Direction.SOUTH);
        boolean hasWestConnection = isValidForConnection(level, pos, Direction.WEST);

        state = state.setValue(NORTH, hasNorthConnection);
        state = state.setValue(EAST, hasEastConnection);
        state = state.setValue(SOUTH, hasSouthConnection);
        state = state.setValue(WEST, hasWestConnection);

        return state;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState,
                                  @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        boolean hasNorthConnection = isValidForConnection(level, pos, Direction.NORTH);
        boolean hasEastConnection = isValidForConnection(level, pos, Direction.EAST);
        boolean hasSouthConnection = isValidForConnection(level, pos, Direction.SOUTH);
        boolean hasWestConnection = isValidForConnection(level, pos, Direction.WEST);

        state = state.setValue(NORTH, hasNorthConnection);
        state = state.setValue(EAST, hasEastConnection);
        state = state.setValue(SOUTH, hasSouthConnection);
        state = state.setValue(WEST, hasWestConnection);

        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock,
                                @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AqueductHydratorBlockEntity blockEntity) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

            if (network != null) {
                blockEntity.neighborChanged();
            }
        }
    }

    @Override
    public List<BlockPos> getValidNeighbors(@NotNull BlockState blockState, @NotNull BlockPos pos) {
        return Direction.Plane.HORIZONTAL.stream().map((dir) -> pos.offset(dir.getNormal())).toList();
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getBlockEntity(pos) instanceof AqueductHydratorBlockEntity hydratorBlock && hydratorBlock.isHydrating()) {
            if (random.nextInt(10) == 0) {
                level.playLocalSound(pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.defaultFluidState() : Fluids.EMPTY.defaultFluidState();
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> entityType) {
        if (!level.isClientSide) {
            return AqueductHydratorBlock::createAqueductHydratorTicker;
        } else {
            return null;
        }
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, Fluids.EMPTY.defaultFluidState()); // prevent fluid spawning after block break
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(EAST).add(WEST).add(SOUTH).add(NORTH).add(WATERLOGGED);
    }

    @NotNull
    public static TextureHolder[] getTexture() {
        return List.of(
                new TextureHolder(-1, -1, Utils.modBlockLoc("aqueduct_hydrator")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("aqueduct_valve")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("terracotta_bricks")),
                new TextureHolder(-1, -1, Utils.modBlockLoc("invisible"))
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

        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(holder.block.get()).part().modelFile(base).addModel().end();
        PROPERTY_BY_DIRECTION.forEach((dir, value) -> builder.part().modelFile(overlay).rotationY(ANGLE_BY_DIRECTION.get(dir)).addModel().condition(value, true).end());

        provider.itemModels().getBuilder(holder.key).parent(inventory);
    }

    public static void registerRecipe(Holder.SimpleBlockHolder holder, Consumer<FinishedRecipe> recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, holder.block.get())
                .define('#', Registration.item(SimpleBlockType.TERRACOTTA_BRICKS))
                .define('R', MaterialItemType.ROD.groupTag)
                .define('S', MaterialItemType.PLATE.groupTag)
                .define('H', MaterialItemType.HAMMER.groupTag)
                .pattern("#R#")
                .pattern("SHS")
                .pattern("#R#")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(SimpleBlockType.TERRACOTTA_BRICKS.itemTag))
                .save(recipeConsumer, Utils.modLoc(holder.key));
    }

    private static void createAqueductHydratorTicker(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockEntity blockEntity) {
        if (blockEntity instanceof AqueductHydratorBlockEntity block) {
            block.tick((ServerLevel) level);
        }
    }
}
