package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.CraftingWorkspaceBlockEntity;
import com.yanny.ytech.registration.YTechBlockEntityTypes;
import com.yanny.ytech.registration.YTechBlocks;
import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CraftingWorkspaceBlock extends Block implements EntityBlock {
    public static final VoxelShape BOX = Shapes.box(0, 0, 0, 1/3.0, 1/3.0, 1/3.0);
    public static final Map<Integer, VoxelShape> SHAPES = new HashMap<>(27);
    private static final VoxelShape SHAPE_BASE = Shapes.box(0, 0, 0, 1, 1/16.0, 1);
    private static final Map<Integer, VoxelShape> SHAPE_CACHE = Collections.synchronizedMap(new HashMap<>());

    static {
        for (int y = 0; y < 3; y++) {
            for (int z = 0; z < 3; z++) {
                for (int x = 0; x < 3; x++) {
                    SHAPES.put(y * 9 + z * 3 + x, BOX.move(x/3.0, y/3.0, z/3.0));
                }
            }
        }

        SHAPE_CACHE.put(0, SHAPE_BASE);
    }

    public CraftingWorkspaceBlock() {
        super(Properties.of().strength(2).noOcclusion().noLootTable());
    }

    @NotNull
    @Override
    public RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @NotNull
    @Override
    public VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        Optional<CraftingWorkspaceBlockEntity> op = pLevel.getBlockEntity(pPos, YTechBlockEntityTypes.CRAFTING_WORKSPACE.get());

        if (op.isPresent()) {
            int bitmask = op.get().getBitmask();

            return SHAPE_CACHE.computeIfAbsent(bitmask, (i) -> prepareShape(bitmask));
        }

        return SHAPE_BASE;
    }

    @NotNull
    @Override
    public ItemInteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        if (pLevel instanceof ServerLevel serverLevel) {
            Optional<CraftingWorkspaceBlockEntity> op = pLevel.getBlockEntity(pPos, YTechBlockEntityTypes.CRAFTING_WORKSPACE.get());

            if (op.isPresent()) {
                return op.get().use(itemStack, pState, serverLevel, pPos, pPlayer, pHand, pHit);
            }
        }

        return ItemInteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new CraftingWorkspaceBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean propagatesSkylightDown(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos) {
        return true;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof CraftingWorkspaceBlockEntity blockEntity) {
                Containers.dropContents(level, pos, blockEntity.getItems());
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public static int getIndex(int @Nullable [] position) {
        if (validPosition(position)) {
            return position[1] * 9 + position[2] * 3 + position[0];
        } else {
            return -1;
        }
    }

    public static int @Nullable [] getPosition(BlockHitResult hit, boolean emptyHand) {
        BlockPos pos = hit.getBlockPos();
        Direction direction = hit.getDirection();
        Vec3 position = hit.getLocation().subtract(pos.getX(), pos.getY(), pos.getZ());
        Vec3i normal = direction.getNormal();
        int flag = emptyHand ? -1 : 1;
        double eps = 1 / 6.0;
        int x = (int) Math.floor(position.x * 3 + eps * normal.getX() * flag);
        int y = (int) Math.floor(position.y * 3 + eps * normal.getY() * flag);
        int z = (int) Math.floor(position.z * 3 + eps * normal.getZ() * flag);

        if (x >= 0 && x < 3 && y >= 0 && y < 3 && z >= 0 && z < 3) {
            return new int[]{x, y, z};
        } else {
            return null;
        }
    }

    public static int @Nullable [] getPosition(int index) {
        if (index >= 0 && index < 27) {
            int y = index / 9;
            index %= 9;
            int z = index / 3;
            index %= 3;
            int x = index;
            return new int[]{x, y, z};
        } else {
            return null;
        }
    }

    public static boolean validPosition(int @Nullable [] position) {
        return position != null && position[0] >= 0 && position[0] < 3 && position[1] >= 0 && position[1] < 3 && position[2] >= 0 && position[2] < 3;
    }

    private static VoxelShape prepareShape(int bitmask) {
        VoxelShape shape = SHAPE_BASE;

        for (int i = 0; i < 27; i++) {
            if ((bitmask >> i & 1) == 1) {
                shape = Shapes.or(shape, SHAPES.get(i));
            }
        }

        return shape;
    }

    public static void registerModel(BlockStateProvider provider) {
        ModelFile model = provider.models().getBuilder(Utils.getPath(YTechBlocks.CRAFTING_WORKSPACE))
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 1, 1, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(2, 1, 3, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(7, 1, 8, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(3, 5, 4, 8).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#0");
                        case DOWN -> faceBuilder.uvs(6, 3, 7, 4).texture("#0").cullface(direction);
                    }
                })
                .from(0.25f, 0, 0.25f).to(1.25f, 3, 1.25f).rotation().angle(0).axis(Direction.Axis.Y).origin(0.25f, 0, 0.25f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 1, 1, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(2, 1, 3, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(7, 1, 8, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(3, 5, 4, 8).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#0");
                        case DOWN -> faceBuilder.uvs(6, 3, 7, 4).texture("#0").cullface(direction);
                    }
                })
                .from(14.75f, 0, 0.25f).to(15.75f, 3, 1.25f).rotation().angle(0).axis(Direction.Axis.Y).origin(14.75f, 0, 0.25f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 1, 1, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(2, 1, 3, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(7, 1, 8, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(3, 5, 4, 8).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#0");
                        case DOWN -> faceBuilder.uvs(6, 3, 7, 4).texture("#0").cullface(direction);
                    }
                })
                .from(0.25f, 0, 14.75f).to(1.25f, 3, 15.75f).rotation().angle(0).axis(Direction.Axis.Y).origin(0.25f, 0, 14.75f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH -> faceBuilder.uvs(0, 1, 1, 4).texture("#0");
                        case EAST -> faceBuilder.uvs(2, 1, 3, 4).texture("#0");
                        case SOUTH -> faceBuilder.uvs(7, 1, 8, 4).texture("#0");
                        case WEST -> faceBuilder.uvs(3, 5, 4, 8).texture("#0");
                        case UP -> faceBuilder.uvs(0, 0, 1, 1).texture("#0");
                        case DOWN -> faceBuilder.uvs(6, 3, 7, 4).texture("#0").cullface(direction);
                    }
                })
                .from(14.75f, 0, 14.75f).to(15.75f, 3, 15.75f).rotation().angle(0).axis(Direction.Axis.Y).origin(14.75f, 0, 14.75f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case EAST, WEST -> faceBuilder.uvs(2, 3, 14, 4).texture("#1");
                    }
                })
                .from(0.75f, 1.75f, 1.25f).to(0.75f, 2.75f, 14.75f).rotation().angle(0).axis(Direction.Axis.Y).origin(-0.25f, 0.75f, 1.25f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case EAST, WEST -> faceBuilder.uvs(2, 3, 14, 4).texture("#1");
                    }
                })
                .from(15.25f, 1.75f, 1.25f).to(15.25f, 2.75f, 14.75f).rotation().angle(0).axis(Direction.Axis.Y).origin(14.25f, 0.75f, 1.25f).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(2, 3, 14, 4).texture("#1");
                    }
                })
                .from(1.25f, 1.75f, 15.25f).to(14.75f, 2.75f, 15.25f).rotation().angle(0).axis(Direction.Axis.Y).origin(1.25f, 0.75f, 14).end()
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(2, 3, 14, 4).texture("#1");
                    }
                })
                .from(1.25f, 1.75f, 0.75f).to(14.75f, 2.75f, 0.75f).rotation().angle(0).axis(Direction.Axis.Y).origin(1.25f, 0.75f, -0.5f).end()
                .end()
                .texture("particle", Utils.modBlockLoc("horizontal_rope"))
                .texture("0", Utils.mcBlockLoc("oak_planks"))
                .texture("1", Utils.modBlockLoc("horizontal_rope"));
        provider.getVariantBuilder(YTechBlocks.CRAFTING_WORKSPACE.get()).forAllStates((state) -> ConfiguredModel.builder().modelFile(model).build());
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.CRAFTING_WORKSPACE)).parent(model);
    }

    public static void registerRecipe(@NotNull RecipeOutput recipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, YTechBlocks.CRAFTING_WORKSPACE.get())
                .define('T', YTechItemTags.GRASS_TWINES)
                .define('S', Items.STICK)
                .pattern("TT")
                .pattern("SS")
                .unlockedBy(Utils.getHasName(), RecipeProvider.has(YTechItemTags.GRASS_TWINES))
                .save(recipeConsumer, Utils.modLoc(YTechBlocks.CRAFTING_WORKSPACE));
    }
}
