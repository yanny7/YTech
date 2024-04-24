package com.yanny.ytech.configuration.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AqueductBlockEntity;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.network.irrigation.IrrigationClientNetwork;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class AqueductBlock extends IrrigationBlock implements BucketPickup, LiquidBlockContainer {
    private static final VoxelShape SHAPE_BOTTOM = Shapes.box(0, 0, 0, 1, 2/16.0, 1);
    private static final VoxelShape SHAPE_NORTH_SIDE = Shapes.box(0, 0, 0, 1, 1, 2/16.0);
    private static final VoxelShape SHAPE_EAST_SIDE = Shapes.box(14/16.0, 0, 0, 1, 1, 1);
    private static final VoxelShape SHAPE_SOUTH_SIDE = Shapes.box(0, 0, 14/16.0, 1, 1, 1);
    private static final VoxelShape SHAPE_WEST_SIDE = Shapes.box(0, 0, 0, 2/16.0, 1, 1);

    private static final VoxelShape SHAPE_NORTH_WEST_SIDE = Shapes.join(SHAPE_WEST_SIDE, SHAPE_NORTH_SIDE, BooleanOp.AND);
    private static final VoxelShape SHAPE_NORTH_EAST_SIDE = Shapes.join(SHAPE_NORTH_SIDE, SHAPE_EAST_SIDE, BooleanOp.AND);
    private static final VoxelShape SHAPE_SOUTH_EAST_SIDE = Shapes.join(SHAPE_EAST_SIDE, SHAPE_SOUTH_SIDE, BooleanOp.AND);
    private static final VoxelShape SHAPE_SOUTH_WEST_SIDE = Shapes.join(SHAPE_SOUTH_SIDE, SHAPE_WEST_SIDE, BooleanOp.AND);

    private static final BooleanProperty NORTH_EAST = BooleanProperty.create("north_east");
    private static final BooleanProperty NORTH_WEST = BooleanProperty.create("north_west");
    private static final BooleanProperty SOUTH_EAST = BooleanProperty.create("south_east");
    private static final BooleanProperty SOUTH_WEST = BooleanProperty.create("south_west");

    private final Map<BlockState, VoxelShape> shapesCache;

    public AqueductBlock() {
        super(Properties.copy(Blocks.TERRACOTTA));
        this.shapesCache = this.getShapeForEachState(AqueductBlock::calculateShape);
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @SuppressWarnings("deprecation")
    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return this.shapesCache.get(state);
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(EAST).add(WEST).add(SOUTH).add(NORTH).add(NORTH_EAST).add(NORTH_WEST).add(SOUTH_EAST).add(SOUTH_WEST);
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

        state = state.setValue(NORTH, !hasNorthConnection);
        state = state.setValue(EAST, !hasEastConnection);
        state = state.setValue(SOUTH, !hasSouthConnection);
        state = state.setValue(WEST, !hasWestConnection);

        state = state.setValue(NORTH_WEST, hasSide(level, pos.west(), NORTH) || hasSide(level, pos.north(), WEST) || !(hasWestConnection && hasNorthConnection));
        state = state.setValue(NORTH_EAST, hasSide(level, pos.north(), EAST) || hasSide(level, pos.east(), NORTH) || !(hasNorthConnection && hasEastConnection));
        state = state.setValue(SOUTH_EAST, hasSide(level, pos.east(), SOUTH) || hasSide(level, pos.south(), EAST) || !(hasEastConnection && hasSouthConnection));
        state = state.setValue(SOUTH_WEST, hasSide(level, pos.south(), WEST) || hasSide(level, pos.west(), SOUTH) || !(hasSouthConnection && hasWestConnection));

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

        state = state.setValue(NORTH, !hasNorthConnection);
        state = state.setValue(EAST, !hasEastConnection);
        state = state.setValue(SOUTH, !hasSouthConnection);
        state = state.setValue(WEST, !hasWestConnection);

        state = state.setValue(NORTH_WEST, hasSide(level, pos.west(), NORTH) || hasSide(level, pos.north(), WEST) || !(hasWestConnection && hasNorthConnection));
        state = state.setValue(NORTH_EAST, hasSide(level, pos.north(), EAST) || hasSide(level, pos.east(), NORTH) || !(hasNorthConnection && hasEastConnection));
        state = state.setValue(SOUTH_EAST, hasSide(level, pos.east(), SOUTH) || hasSide(level, pos.south(), EAST) || !(hasEastConnection && hasSouthConnection));
        state = state.setValue(SOUTH_WEST, hasSide(level, pos.south(), WEST) || hasSide(level, pos.west(), SOUTH) || !(hasSouthConnection && hasWestConnection));

        return state;
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return new AqueductBlockEntity(pos, blockState);
    }

    @NotNull
    @Override
    public ItemStack pickupBlock(@Nullable Player player, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state) {
        if (level instanceof ServerLevel && level.getBlockEntity(pos) instanceof AqueductBlockEntity aqueductBlock) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(aqueductBlock);

            if (network != null) {
                FluidTank tank = network.getFluidHandler();

                if (tank.getFluidAmount() > 1000) {
                    tank.drain(1000, IFluidHandler.FluidAction.EXECUTE);
                    return new ItemStack(Items.WATER_BUCKET);
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public Optional<SoundEvent> getPickupSound() {
        return Fluids.WATER.getPickupSound();
    }

    @Override
    public boolean canPlaceLiquid(@Nullable Player player, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Fluid fluid) {
        return fluid == Fluids.WATER;
    }

    @Override
    public boolean placeLiquid(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull FluidState fluidState) {
        if (fluidState.getType() == Fluids.WATER && !level.isClientSide() && level.getBlockEntity(pos) instanceof AqueductBlockEntity aqueductBlock) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(aqueductBlock);

            if (network != null) {
                FluidTank tank = network.getFluidHandler();
                tank.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (!(entity instanceof LivingEntity) || entity.getFeetBlockState().is(this)) {
            if (level.getBlockEntity(pos) instanceof AqueductBlockEntity aqueductBlockEntity) {
                double multiplier = 0.5;

                if (level.isClientSide) {
                    IrrigationClientNetwork network = YTechMod.IRRIGATION_PROPAGATOR.client().getNetwork(aqueductBlockEntity);

                    if (network != null && network.getCapacity() > 0) {
                        multiplier = Mth.clamp((1 - network.getAmount() / (double) network.getCapacity()) * 0.5, 0, 0.5);

                        if (entity.isOnFire()) {
                            entity.extinguishFire();
                        }
                    }
                } else {
                    IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(aqueductBlockEntity);

                    if (network != null && network.getFluidHandler().getCapacity() > 0) {
                        multiplier = Mth.clamp((1 - network.getFluidHandler().getFluidAmount() / (double)network.getFluidHandler().getCapacity()) * 0.5, 0, 0.5);

                        if (entity.isOnFire()) {
                            entity.extinguishFire();
                        }
                    }
                }

                entity.setDeltaMovement(entity.getDeltaMovement().multiply(new Vec3(0.5 + multiplier, 1.0, 0.5 + multiplier)));
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isPathfindable(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull PathComputationType type) {
        return false;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return false;
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getBlockEntity(pos) instanceof AqueductBlockEntity blockEntity) {
            blockEntity.onRandomTick();
        }
    }

    @Override
    public List<BlockPos> getValidNeighbors(@NotNull BlockState blockState, @NotNull BlockPos pos) {
        return Direction.Plane.HORIZONTAL.stream().map((dir) -> pos.offset(dir.getNormal())).toList();
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ResourceLocation bricks = Utils.modBlockLoc("terracotta_bricks");
        String name = Utils.getPath(YTechBlocks.AQUEDUCT);
        ModelFile base = provider.models().getBuilder(name)
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 14, 16, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 2, 16).end()
                .texture("particle", bricks)
                .texture("0", bricks);
        ModelFile side = provider.models().getBuilder(name + "_side")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(2, 0, 14, 14).texture("#0");
                        case EAST -> faceBuilder.uvs(14, 0, 16, 14).texture("#0");
                        case WEST -> faceBuilder.uvs(0, 0, 2, 14).texture("#0");
                        case UP -> faceBuilder.uvs(2, 0, 14, 2).texture("#0");
                    }
                })
                .from(2, 2, 0).to(14, 16, 2).end()
                .texture("particle", bricks)
                .texture("0", bricks);
        ModelFile edge = provider.models().getBuilder(name + "_edge")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST -> faceBuilder.uvs(14, 0, 16, 14).texture("#0");
                        case SOUTH, WEST -> faceBuilder.uvs(0, 0, 2, 14).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 2, 2).texture("#0");
                    }
                })
                .from(0, 2, 0).to(2, 16, 2).end()
                .texture("particle", bricks)
                .texture("0", bricks);

        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(YTechBlocks.AQUEDUCT.get()).part().modelFile(base).addModel().end();

        PROPERTY_BY_DIRECTION.forEach((dir, value) -> builder.part().modelFile(side).rotationY(ANGLE_BY_DIRECTION.get(dir)).addModel().condition(value, true).end());
        builder.part().modelFile(edge).rotationY(ANGLE_BY_DIRECTION.get(Direction.NORTH)).addModel().condition(NORTH_WEST, true).end();
        builder.part().modelFile(edge).rotationY(ANGLE_BY_DIRECTION.get(Direction.EAST)).addModel().condition(NORTH_EAST, true).end();
        builder.part().modelFile(edge).rotationY(ANGLE_BY_DIRECTION.get(Direction.SOUTH)).addModel().condition(SOUTH_EAST, true).end();
        builder.part().modelFile(edge).rotationY(ANGLE_BY_DIRECTION.get(Direction.WEST)).addModel().condition(SOUTH_WEST, true).end();

        ModelFile itemModel = provider.models().getBuilder(name + "_inventory")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 14, 16, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 16, 16).texture("#0");
                    }
                })
                .from(0, 0, 0).to(16, 2, 16).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 16, 14).texture("#0");
                        case EAST -> faceBuilder.uvs(14, 0, 16, 14).texture("#0");
                        case WEST -> faceBuilder.uvs(0, 0, 2, 14).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 16, 2).texture("#0");
                    }
                })
                .from(0, 2, 0).to(16, 16, 2).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 16, 14).texture("#0");
                        case EAST -> faceBuilder.uvs(0, 0, 2, 14).texture("#0");
                        case WEST -> faceBuilder.uvs(14, 0, 16, 14).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 16, 2).texture("#0");
                    }
                })
                .from(0, 2, 14).to(16, 16, 16).end()
                .texture("particle", bricks)
                .texture("0", bricks);

        provider.itemModels().getBuilder(name).parent(itemModel);
    }

    public static void registerRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.AQUEDUCT.get())
                .define('#', YTechItemTags.TERRACOTTA_BRICKS)
                .pattern("# #")
                .pattern("# #")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(recipeConsumer, Utils.modLoc(YTechBlocks.AQUEDUCT));
    }

    private static boolean hasSide(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BooleanProperty property) {
        BlockState blockState = level.getBlockState(pos);
        boolean isIrrigation = blockState.getBlock() instanceof AqueductBlock;
        return isIrrigation && blockState.hasProperty(property) && blockState.getValue(property);
    }

    private static VoxelShape calculateShape(BlockState state) {
        VoxelShape shape = SHAPE_BOTTOM;

        if (state.getValue(NORTH)) {
            shape = Shapes.join(shape, SHAPE_NORTH_SIDE, BooleanOp.OR);
        }
        if (state.getValue(EAST)) {
            shape = Shapes.join(shape, SHAPE_EAST_SIDE, BooleanOp.OR);
        }
        if (state.getValue(SOUTH)) {
            shape = Shapes.join(shape, SHAPE_SOUTH_SIDE, BooleanOp.OR);
        }
        if (state.getValue(WEST)) {
            shape = Shapes.join(shape, SHAPE_WEST_SIDE, BooleanOp.OR);
        }
        if (state.getValue(NORTH_WEST)) {
            shape = Shapes.join(shape, SHAPE_NORTH_WEST_SIDE, BooleanOp.OR);
        }
        if (state.getValue(NORTH_EAST)) {
            shape = Shapes.join(shape, SHAPE_NORTH_EAST_SIDE, BooleanOp.OR);
        }
        if (state.getValue(SOUTH_EAST)) {
            shape = Shapes.join(shape, SHAPE_SOUTH_EAST_SIDE, BooleanOp.OR);
        }
        if (state.getValue(SOUTH_WEST)) {
            shape = Shapes.join(shape, SHAPE_SOUTH_WEST_SIDE, BooleanOp.OR);
        }

        return shape;
    }
}
