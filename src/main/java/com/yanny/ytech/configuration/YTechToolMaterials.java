package com.yanny.ytech.configuration;

import com.yanny.ytech.registration.YTechItemTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public enum YTechToolMaterials {
    IRON(MaterialType.IRON, ToolMaterial.IRON), // BlockTags.INCORRECT_FOR_IRON_TOOL, 250, 6.0F, 2.0F, 14
    GOLD(MaterialType.GOLD, ToolMaterial.GOLD), // BlockTags.INCORRECT_FOR_GOLD_TOOL, 32, 12.0F, 0.0F, 22
    COPPER(MaterialType.COPPER, BlockTags.INCORRECT_FOR_STONE_TOOL, 160, 5.0F, 0.5F, 10),
    BRONZE(MaterialType.BRONZE, BlockTags.INCORRECT_FOR_IRON_TOOL, 200, 32.0F, 1.5F, 15),
    LEAD(MaterialType.LEAD, BlockTags.INCORRECT_FOR_STONE_TOOL, 16, 3.0F, 3.0F, 21),
    TIN(MaterialType.TIN, BlockTags.INCORRECT_FOR_STONE_TOOL, 15, 10.0F, -1.0F, 16),
    FLINT(MaterialType.FLINT, BlockTags.INCORRECT_FOR_STONE_TOOL, 80, 4.5F, 2.0F, 8),
    ANTLER(MaterialType.ANTLER, BlockTags.INCORRECT_FOR_STONE_TOOL, 50, 5.0F, 0.0F, 1),
    STONE(MaterialType.STONE, ToolMaterial.STONE), //BlockTags.INCORRECT_FOR_STONE_TOOL, 131, 4.0F, 1.0F, 5, ItemTags.STONE_TOOL_MATERIALS
    ;

    private static final Map<MaterialType, ToolMaterial> TOOLS = new HashMap<>();

    static {
        for (YTechToolMaterials value : values()) {
            TOOLS.put(value.materialType, value.toolMaterial);
        }
    }

    public final MaterialType materialType;
    public final ToolMaterial toolMaterial;

    YTechToolMaterials(MaterialType materialType, ToolMaterial toolMaterial) {
        this.materialType = materialType;
        this.toolMaterial = toolMaterial;
    }

    YTechToolMaterials(MaterialType materialType, TagKey<Block> incorrectBlocksForDrops, int durability, float speed, float attackDamageBonus, int enchantmentValue) {
        this.materialType = materialType;
        this.toolMaterial = new ToolMaterial(incorrectBlocksForDrops, durability, speed, attackDamageBonus, enchantmentValue, YTechItemTags.INGOTS.get(materialType));
    }

    public static ToolMaterial get(MaterialType type) {
        ToolMaterial material = TOOLS.get(type);

        if (material == null) {
            throw new IllegalStateException("Undefined tool material " + type.key);
        }

        return material;
    }
}
