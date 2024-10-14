package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.recipe.RemainingShapedRecipe;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LEVEL;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;

public class FirePitBlock extends Block {
    private static final VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, 2/16.0, 1);

    public FirePitBlock() {
        super(Properties.ofFullCopy(Blocks.STONE).sound(SoundType.WOOD).noOcclusion().hasPostProcess(FirePitBlock::always).lightLevel(FirePitBlock::getLightLevel));
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(LIT).add(LEVEL);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void animateTick(@NotNull BlockState pState, @NotNull Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (pState.getValue(LIT)) {
            if (level.isRainingAt(pos.above())) {
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.25 + random.nextFloat() / 2, pos.getY() + 0.3, pos.getZ() + 0.25 + random.nextFloat() / 2, 0, 0, 0);
            }

            if (pState.getValue(LEVEL) > 0) {
                level.addParticle(ParticleTypes.FLAME, pos.getX() + 0.25 + random.nextFloat() / 2, pos.getY() + 0.2, pos.getZ() + 0.25 + random.nextFloat() / 2, 0, 0, 0);

                if (random.nextInt(16 - pState.getValue(LEVEL)) == 0) {
                    level.addAlwaysVisibleParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, false, pos.getX() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1),
                            pos.getY() + random.nextDouble() + random.nextDouble(), pos.getZ() + 0.5 + random.nextDouble() / 3.0 * (random.nextBoolean() ? 1 : -1), 0.0, 0.07, 0.0);
                }

                if (random.nextInt(10) == 0) {
                    level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
                }

                if (random.nextInt(5) == 0) {
                    for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                        level.addParticle(ParticleTypes.LAVA, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, random.nextFloat() / 2, 5.0E-5, random.nextFloat() / 2.0F);
                    }
                }
            }
        }
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return state.getValue(LIT);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (state.getValue(LEVEL) > 0) {
            if (random.nextDouble() > 0.5 && state.getValue(LIT) && level.isRainingAt(pos.above())) {
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 0.8F + random.nextFloat() * 0.4F);
                level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX() + 0.25 + random.nextFloat() / 2, pos.getY() + 0.3, pos.getZ() + 0.25 + random.nextFloat() / 2, 0, 0, 0);
                level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL);
            } else {
                level.setBlock(pos, state.setValue(LEVEL, state.getValue(LEVEL) - 1), Block.UPDATE_ALL);
            }
        } else {
            level.setBlock(pos, state.setValue(LIT, false), Block.UPDATE_ALL);
        }
    }

    @NotNull
    @Override
    public ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (level instanceof ServerLevel serverLevel) {
            if (!state.getValue(LIT) && player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.STICK) && player.getItemInHand(InteractionHand.OFF_HAND).is(Items.STICK)) {
                player.getItemInHand(InteractionHand.MAIN_HAND).shrink(1);
                player.getItemInHand(InteractionHand.OFF_HAND).shrink(1);

                if (player.getRandom().nextFloat() < 0.4f) {
                    level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0f, 0.8F + player.getRandom().nextFloat() * 0.4F);
                    level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
                } else {
                    level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0f, 0.8F + player.getRandom().nextFloat() * 0.4F);
                }

                return ItemInteractionResult.sidedSuccess(serverLevel.isClientSide);
            }

            ItemStack itemStack = player.getItemInHand(hand);
            int burnTime = itemStack.getBurnTime(null);

            if (burnTime > 0 && state.getValue(LEVEL) < 15) {
                itemStack.shrink(1);
                level.setBlock(pos, state.setValue(LEVEL, Math.min(15, state.getValue(LEVEL) + (int) Math.log10(burnTime))), Block.UPDATE_ALL);
                return ItemInteractionResult.sidedSuccess(serverLevel.isClientSide);
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return defaultBlockState().setValue(LEVEL, 2).setValue(LIT, false);
    }

    public void entityInside(BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (state.getValue(LIT) && entity instanceof LivingEntity) {
            entity.hurt(level.damageSources().campfire(), state.getValue(LEVEL) / 15f);
        }

        super.entityInside(state, level, pos, entity);
    }

    @Override
    public void onProjectileHit(@NotNull Level level, @NotNull BlockState state, @NotNull BlockHitResult hit, @NotNull Projectile projectile) {
        BlockPos pos = hit.getBlockPos();

        if (!level.isClientSide && projectile.isOnFire() && projectile.mayInteract(level, pos) && !state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, true), Block.UPDATE_ALL);
        }
    }

    @Override
    public boolean isPathfindable(@NotNull BlockState pState, @NotNull PathComputationType pType) {
        return false;
    }

    public static void registerModel(BlockStateProvider provider) {
        ResourceLocation magmaTexture = Utils.mcBlockLoc("magma");
        ResourceLocation andesiteTexture = Utils.mcBlockLoc("andesite");
        ResourceLocation logTexture = Utils.mcBlockLoc("oak_log");
        ResourceLocation logTopTexture = Utils.mcBlockLoc("oak_log_top");
        ResourceLocation basaltTexture = Utils.mcBlockLoc("smooth_basalt");
        String name = Utils.getPath(YTechBlocks.FIRE_PIT);
        ModelFile base_lit = provider.models().getBuilder(name + "_base_lit")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case SOUTH -> faceBuilder.uvs(3, 0, 5, 1).texture("#2");
                        case WEST -> faceBuilder.uvs(2, 1, 4, 2).texture("#2");
                        case UP -> faceBuilder.uvs(11, 2, 13, 4).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(13, 8, 15, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(3.47487f, 0, 11.8033f).to(5.47487f, 1, 13.8033f).rotation().angle(45).axis(Direction.Axis.Y).origin(4.47487f, 0.5f, 12.8033f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case UP -> faceBuilder.uvs(0, 0, 2, 2).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(1.35355f, 0, 9.68198f).to(3.35355f, 1, 11.68198f).rotation().angle(22.5f).axis(Direction.Axis.Y).origin(2.35355f, 0.5f, 10.68198f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 3, 1).texture("#2");
                        case UP -> faceBuilder.uvs(10, 6, 13, 8).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(0, 0, 3, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(13, 0, 7).to(15, 1, 10).rotation().angle(0).axis(Direction.Axis.Y).origin(14, 0.5f, 8.5f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 3, 1).texture("#2");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case UP -> faceBuilder.uvs(0, 6, 3, 8).texture("#2");
                        case DOWN -> faceBuilder.uvs(0, 0, 3, 2).texture("#2");
                    }
                })
                .from(9.63693f, 0, 10.84404f).to(12.63693f, 1, 12.84404f).rotation().angle(45).axis(Direction.Axis.Y).origin(11.13693f, 0.5f, 11.84404f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 3, 1).texture("#2");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 3, 2).texture("#2");
                    }
                })
                .from(6.63173f, 0, 0.83883f).to(9.63173f, 1, 2.83883f).rotation().angle(0).axis(Direction.Axis.Y).origin(8.13173f, 0.5f, 1.83883f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(5, 4, 8, 6).texture("#2");
                        case EAST -> faceBuilder.uvs(4, 8, 7, 10).texture("#2");
                        case SOUTH -> faceBuilder.uvs(13, 6, 16, 8).texture("#2");
                        case WEST -> faceBuilder.uvs(10, 6, 13, 8).texture("#2");
                        case UP -> faceBuilder.uvs(9, 2, 12, 5).texture("#2");
                        case DOWN -> faceBuilder.uvs(11, 9, 14, 12).texture("#2");
                    }
                })
                .from(2.74264f, 0, 2.03553f).to(5.74264f, 2, 5.03553f).rotation().angle(45).axis(Direction.Axis.Y).origin(4.24264f, 1, 3.53553f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 2).texture("#2");
                        case EAST -> faceBuilder.uvs(0, 3, 2, 5).texture("#2");
                        case SOUTH -> faceBuilder.uvs(3, 9, 5, 11).texture("#2");
                        case WEST -> faceBuilder.uvs(11, 1, 13, 3).texture("#2");
                        case UP -> faceBuilder.uvs(4, 1, 6, 3).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 4, 9, 6).texture("#2");
                    }
                })
                .from(12.10022f, 0.25f, 4.54521f).to(14.10022f, 2.25f, 6.54521f).rotation().angle(22.5f).axis(Direction.Axis.Y).origin(13.10022f, 1.25f, 5.54521f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 4, 2, 6).texture("#2");
                        case EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 2, 2).texture("#2");
                        case UP -> faceBuilder.uvs(4, 9, 6, 11).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 8, 9, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(10.12655f, 0.25f, 2.03034f).to(12.12655f, 2.25f, 4.03034f).rotation().angle(-45).axis(Direction.Axis.Y).origin(11.12655f, 1.25f, 3.03034f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(14, 4, 16, 6).texture("#2");
                        case EAST -> faceBuilder.uvs(5, 4, 8, 6).texture("#2");
                        case SOUTH -> faceBuilder.uvs(4, 0, 6, 2).texture("#2");
                        case WEST -> faceBuilder.uvs(9, 2, 12, 4).texture("#2");
                        case UP -> faceBuilder.uvs(11, 6, 13, 9).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 7, 9, 10).texture("#2");
                    }
                })
                .from(0.83581f, 0, 6.1924f).to(2.83581f, 2, 9.1924f).rotation().angle(0).axis(Direction.Axis.Y).origin(1.83581f, 1, 7.6924f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(3, 0, 6, 2).texture("#2");
                        case EAST -> faceBuilder.uvs(9, 0, 11, 2).texture("#2");
                        case SOUTH -> faceBuilder.uvs(0, 2, 3, 4).texture("#2");
                        case WEST -> faceBuilder.uvs(5, 2, 7, 4).texture("#2");
                        case UP -> faceBuilder.uvs(4, 4, 7, 6).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 3, 10, 5).texture("#2");
                    }
                })
                .from(6.19657f, 0, 13.0391f).to(9.19657f, 2, 15.0391f).rotation().angle(0).axis(Direction.Axis.Y).origin(7.69657f, 1, 14.0391f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 10, 10).texture("#0").emissivity(15, 8);
                    }
                })
                .from(3, 0.01f, 3).to(13, 0.01f, 13).rotation().angle(-45).axis(Direction.Axis.Y).origin(8, 0.01f, 8).end()
                .end()
                .texture("particle", magmaTexture)
                .texture("0", magmaTexture)
                .texture("2", andesiteTexture);
        ModelFile base = provider.models().getBuilder(name + "_base")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case SOUTH -> faceBuilder.uvs(3, 0, 5, 1).texture("#2");
                        case WEST -> faceBuilder.uvs(2, 1, 4, 2).texture("#2");
                        case UP -> faceBuilder.uvs(11, 2, 13, 4).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(13, 8, 15, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(3.47487f, 0, 11.8033f).to(5.47487f, 1, 13.8033f).rotation().angle(45).axis(Direction.Axis.Y).origin(4.47487f, 0.5f, 12.8033f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case UP -> faceBuilder.uvs(0, 0, 2, 2).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(0, 0, 2, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(1.35355f, 0, 9.68198f).to(3.35355f, 1, 11.68198f).rotation().angle(22.5f).axis(Direction.Axis.Y).origin(2.35355f, 0.5f, 10.68198f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 3, 1).texture("#2");
                        case UP -> faceBuilder.uvs(10, 6, 13, 8).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(0, 0, 3, 2).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(13, 0, 7).to(15, 1, 10).rotation().angle(0).axis(Direction.Axis.Y).origin(14, 0.5f, 8.5f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 3, 1).texture("#2");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case UP -> faceBuilder.uvs(0, 6, 3, 8).texture("#2");
                        case DOWN -> faceBuilder.uvs(0, 0, 3, 2).texture("#2");
                    }
                })
                .from(9.63693f, 0, 10.84404f).to(12.63693f, 1, 12.84404f).rotation().angle(45).axis(Direction.Axis.Y).origin(11.13693f, 0.5f, 11.84404f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(0, 0, 3, 1).texture("#2");
                        case EAST, WEST -> faceBuilder.uvs(0, 0, 2, 1).texture("#2");
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 3, 2).texture("#2");
                    }
                })
                .from(6.63173f, 0, 0.83883f).to(9.63173f, 1, 2.83883f).rotation().angle(0).axis(Direction.Axis.Y).origin(8.13173f, 0.5f, 1.83883f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(5, 4, 8, 6).texture("#2");
                        case EAST -> faceBuilder.uvs(4, 8, 7, 10).texture("#2");
                        case SOUTH -> faceBuilder.uvs(13, 6, 16, 8).texture("#2");
                        case WEST -> faceBuilder.uvs(10, 6, 13, 8).texture("#2");
                        case UP -> faceBuilder.uvs(9, 2, 12, 5).texture("#2");
                        case DOWN -> faceBuilder.uvs(11, 9, 14, 12).texture("#2");
                    }
                })
                .from(2.74264f, 0, 2.03553f).to(5.74264f, 2, 5.03553f).rotation().angle(45).axis(Direction.Axis.Y).origin(4.24264f, 1, 3.53553f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 2, 2).texture("#2");
                        case EAST -> faceBuilder.uvs(0, 3, 2, 5).texture("#2");
                        case SOUTH -> faceBuilder.uvs(3, 9, 5, 11).texture("#2");
                        case WEST -> faceBuilder.uvs(11, 1, 13, 3).texture("#2");
                        case UP -> faceBuilder.uvs(4, 1, 6, 3).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 4, 9, 6).texture("#2");
                    }
                })
                .from(12.10022f, 0.25f, 4.54521f).to(14.10022f, 2.25f, 6.54521f).rotation().angle(22.5f).axis(Direction.Axis.Y).origin(13.10022f, 1.25f, 5.54521f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 4, 2, 6).texture("#2");
                        case EAST, SOUTH, WEST -> faceBuilder.uvs(0, 0, 2, 2).texture("#2");
                        case UP -> faceBuilder.uvs(4, 9, 6, 11).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 8, 9, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#2");
                    }
                })
                .from(10.12655f, 0.25f, 2.03034f).to(12.12655f, 2.25f, 4.03034f).rotation().angle(-45).axis(Direction.Axis.Y).origin(11.12655f, 1.25f, 3.03034f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(14, 4, 16, 6).texture("#2");
                        case EAST -> faceBuilder.uvs(5, 4, 8, 6).texture("#2");
                        case SOUTH -> faceBuilder.uvs(4, 0, 6, 2).texture("#2");
                        case WEST -> faceBuilder.uvs(9, 2, 12, 4).texture("#2");
                        case UP -> faceBuilder.uvs(11, 6, 13, 9).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 7, 9, 10).texture("#2");
                    }
                })
                .from(0.83581f, 0, 6.1924f).to(2.83581f, 2, 9.1924f).rotation().angle(0).axis(Direction.Axis.Y).origin(1.83581f, 1, 7.6924f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(3, 0, 6, 2).texture("#2");
                        case EAST -> faceBuilder.uvs(9, 0, 11, 2).texture("#2");
                        case SOUTH -> faceBuilder.uvs(0, 2, 3, 4).texture("#2");
                        case WEST -> faceBuilder.uvs(5, 2, 7, 4).texture("#2");
                        case UP -> faceBuilder.uvs(4, 4, 7, 6).texture("#2");
                        case DOWN -> faceBuilder.uvs(7, 3, 10, 5).texture("#2");
                    }
                })
                .from(6.19657f, 0, 13.0391f).to(9.19657f, 2, 15.0391f).rotation().angle(0).axis(Direction.Axis.Y).origin(7.69657f, 1, 14.0391f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case UP, DOWN -> faceBuilder.uvs(0, 0, 10, 10).texture("#0");
                    }
                })
                .from(3, 0.01f, 3).to(13, 0.01f, 13).rotation().angle(-45).axis(Direction.Axis.Y).origin(8, 0.01f, 8).end()
                .end()
                .texture("particle", basaltTexture)
                .texture("0", basaltTexture)
                .texture("2", andesiteTexture);
        ModelFile logs_down = provider.models().getBuilder(name + "_logs_up")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(5, 5, 11, 11).rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90).texture("#4");
                        case EAST -> faceBuilder.uvs(9, 4, 13, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case WEST -> faceBuilder.uvs(2, 0, 6, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(0, 0, 4, 10).texture("#3");
                        case DOWN -> faceBuilder.uvs(1, 3, 5, 13).texture("#3");
                    }
                })
                .from(8.25f, 0, 4.25f).to(10.25f, 2, 10.25f).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(5, 5, 11, 11).rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN).texture("#4");
                        case EAST -> faceBuilder.uvs(4, 5, 8, 15).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case WEST -> faceBuilder.uvs(0, 2, 4, 12).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(7, 5, 11, 15).texture("#3");
                        case DOWN -> faceBuilder.uvs(2, 6, 6, 16).texture("#3");
                    }
                })
                .from(5.25f, 0, 5).to(7.25f, 2, 11).end()
                .texture("particle", magmaTexture)
                .texture("3", logTexture)
                .texture("4", logTopTexture);
        ModelFile logs_up = provider.models().getBuilder(name + "_logs_down")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 0, 4, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case EAST, WEST -> faceBuilder.uvs(5, 5, 11, 11).texture("#4");
                        case SOUTH -> faceBuilder.uvs(12, 3, 16, 13).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(6, 1, 10, 11).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case DOWN -> faceBuilder.uvs(8, 4, 12, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                    }
                })
                .from(5.75f, 2, 8).to(10.75f, 4, 10).end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(5, 3, 9, 13).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case EAST, WEST -> faceBuilder.uvs(5, 5, 11, 11).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#4");
                        case SOUTH -> faceBuilder.uvs(1, 4, 5, 14).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case UP -> faceBuilder.uvs(9, 0, 13, 10).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                        case DOWN -> faceBuilder.uvs(9, 6, 13, 16).rotation(ModelBuilder.FaceRotation.CLOCKWISE_90).texture("#3");
                    }
                })
                .from(5, 2, 5.5f).to(10, 4, 7.5f).end()
                .texture("particle", magmaTexture)
                .texture("3", logTexture)
                .texture("4", logTopTexture);

        MultiPartBlockStateBuilder builder = provider.getMultipartBuilder(YTechBlocks.FIRE_PIT.get());

        builder.part().modelFile(base).addModel().condition(LIT, false).end();
        builder.part().modelFile(base_lit).addModel().condition(LIT, true).end();
        builder.part().modelFile(logs_down).addModel().condition(LEVEL, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15).end();
        builder.part().modelFile(logs_up).addModel().condition(LEVEL, 9, 10, 11, 12, 13, 14, 15).end();

        provider.itemModels().getBuilder(name).parent(provider.itemModels().getExistingFile(Utils.mcItemLoc("generated")))
                .texture("layer0", Utils.modItemLoc("fire_pit"));
    }

    public static void registerRecipe(RecipeOutput consumer) {
        RemainingShapedRecipe.Builder.shaped(RecipeCategory.MISC, YTechBlocks.FIRE_PIT.get())
                .define('S', Items.STICK)
                .define('P', YTechItemTags.PEBBLES)
                .pattern("SS")
                .pattern("PP")
                .unlockedBy("has_pebble", RecipeProvider.has(YTechItemTags.PEBBLES))
                .save(consumer, Utils.modLoc(YTechBlocks.FIRE_PIT));
    }

    private static boolean always(BlockState s, BlockGetter g, BlockPos p) {
        return true;
    }

    private static int getLightLevel(BlockState state) {
        return state.getValue(LIT) ? Math.min(15, state.getValue(LEVEL) * 2 + 1) : 0;
    }
}
