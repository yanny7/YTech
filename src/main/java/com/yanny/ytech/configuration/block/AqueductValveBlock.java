package com.yanny.ytech.configuration.block;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.AqueductValveBlockEntity;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.network.generic.NetworkUtils;
import com.yanny.ytech.network.irrigation.IrrigationServerNetwork;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class AqueductValveBlock extends IrrigationBlock {
    public AqueductValveBlock() {
        super(Properties.ofFullCopy(Blocks.TERRACOTTA));
    }

    @NotNull
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        return new AqueductValveBlockEntity(pos, blockState);
    }

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
        return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, blockPlaceContext.getHorizontalDirection().getClockWise());
    }

    @SuppressWarnings("deprecation")
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock,
                                @NotNull BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);

        if (!level.isClientSide && level.getBlockEntity(pos) instanceof AqueductValveBlockEntity blockEntity) {
            IrrigationServerNetwork network = YTechMod.IRRIGATION_PROPAGATOR.server().getNetwork(blockEntity);

            if (network != null) {
                blockEntity.neighborChanged();
            }
        }
    }

    @Override
    public List<BlockPos> getValidNeighbors(@NotNull BlockState blockState, @NotNull BlockPos pos) {
        return NetworkUtils.getDirections(List.of(Direction.EAST, Direction.WEST), pos, blockState.getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    @Override
    public void animateTick(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getBlockEntity(pos) instanceof AqueductValveBlockEntity valveBlockEntity && valveBlockEntity.getFlow() > 0 && random.nextInt(10) == 0) {
            level.playLocalSound(pos, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, random.nextFloat() * 0.25F + 0.75F, random.nextFloat() + 0.5F, false);
        }
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(HORIZONTAL_FACING);
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        ResourceLocation valveTexture = Utils.modBlockLoc("aqueduct/aqueduct_valve");
        ResourceLocation bricksTexture = Utils.modBlockLoc("terracotta_bricks");
        String name = Utils.getId(YTechBlocks.AQUEDUCT_VALVE);

        ModelFile model = provider.models()
                .cube(name, bricksTexture, bricksTexture, bricksTexture, bricksTexture, valveTexture, valveTexture)
                .texture("particle", valveTexture);
        provider.horizontalBlock(YTechBlocks.AQUEDUCT_VALVE.get(), model);
        provider.itemModels().getBuilder(name).parent(model);
    }

    public static void registerRecipe(RecipeOutput recipeConsumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.AQUEDUCT_VALVE.get())
                .define('#', YTechItemTags.TERRACOTTA_BRICKS)
                .define('R', YTechItemTags.RODS.tag)
                .define('H', YTechItemTags.HAMMERS.tag)
                .define('S', YTechItemTags.SAWS.tag)
                .pattern("###")
                .pattern("HRS")
                .pattern("###")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.TERRACOTTA_BRICKS))
                .save(recipeConsumer, Utils.modLoc(YTechBlocks.AQUEDUCT_VALVE));
    }
}
