package com.yanny.ytech.configuration.block;

import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.configuration.block_entity.StrainerBlockEntity;
import com.yanny.ytech.registration.YTechBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StrainerBlock extends MachineBlock {
    public StrainerBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new StrainerBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pLevel.getBlockEntity(pPos) instanceof StrainerBlockEntity blockEntity) {
            blockEntity.onRandomTick(pLevel, pPos);
        }
    }

    public static void registerModel(@NotNull BlockStateProvider provider) {
        provider.models().getBuilder(Utils.getPath(YTechBlocks.STRAINER) + "_net")
                .parent(provider.models().getExistingFile(Utils.mcBlockLoc("block")))
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(0, 0, 2, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(2, 0, 4, 2).texture("#0");
                    }
                })
                .from(0, 0, 0).to(2, 16, 2)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(0, 0, 2, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(2, 0, 4, 2).texture("#0");
                    }
                })
                .from(14, 0, 0).to(16, 16, 2)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST-> faceBuilder.uvs(0, 0, 2, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(2, 0, 4, 2).texture("#0");
                    }
                })
                .from(14, 0, 14).to(16, 16, 16)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, WEST, SOUTH, EAST -> faceBuilder.uvs(0, 0, 2, 16).texture("#0");
                        case UP, DOWN -> faceBuilder.uvs(2, 0, 4, 2).texture("#0");
                    }
                })
                .from(0, 0, 14).to(2, 16, 16)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case EAST, WEST -> faceBuilder.uvs(2, 0, 14, 15).texture("#1");
                    }
                })
                .from(1, 0, 2).to(1, 15, 14)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case EAST, WEST -> faceBuilder.uvs(2, 0, 14, 15).texture("#1");
                    }
                })
                .from(15, 0, 2).to(15, 15, 14)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(2, 0, 14, 15).texture("#1");
                    }
                })
                .from(2, 0, 1).to(14, 15, 1)
                .end()
                .element().allFaces((direction, faceBuilder) -> {
                    switch(direction) {
                        case NORTH, SOUTH -> faceBuilder.uvs(2, 0, 14, 15).texture("#1");
                    }
                })
                .from(2, 0, 15).to(14, 15, 15)
                .end()

                .texture("particles", Utils.mcBlockLoc("oak_log"))
                .texture("0", Utils.mcBlockLoc("oak_log"))
                .texture("1", Utils.modBlockLoc("strainer/strainer_mesh"));


        ModelFile model = provider.models().cubeTop(Utils.getPath(YTechBlocks.STRAINER),
                Utils.mcBlockLoc("oak_log"),
                Utils.modBlockLoc("strainer/strainer_top")
        );
        provider.simpleBlock(YTechBlocks.STRAINER.get(), model);
        provider.itemModels().getBuilder(Utils.getPath(YTechBlocks.STRAINER)).parent(model);
    }
}
