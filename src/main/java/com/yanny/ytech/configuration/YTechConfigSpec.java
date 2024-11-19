package com.yanny.ytech.configuration;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;

public class YTechConfigSpec {
    private final ModConfigSpec.BooleanValue noDryingDuringRain;
    private final ModConfigSpec.BooleanValue finiteWaterSource;
    private final ModConfigSpec.BooleanValue cropsNeedWateredFarmland;
    private final ModConfigSpec.BooleanValue farmlandConsumesWater;

    private final ModConfigSpec.IntValue baseFluidStoragePerBlock;
    private final ModConfigSpec.BooleanValue rainingFillAqueduct;
    private final ModConfigSpec.IntValue rainingFillAmount;
    private final ModConfigSpec.IntValue rainingFillPerNthTick;
    private final ModConfigSpec.BooleanValue validBlockForRaining;
    private final ModConfigSpec.IntValue valveFillAmount;
    private final ModConfigSpec.IntValue valveFillPerNthTick;
    private final ModConfigSpec.IntValue hydratorDrainAmount;
    private final ModConfigSpec.IntValue hydratorDrainPerNthTick;
    private final ModConfigSpec.IntValue fertilizerDuration;
    private final ModConfigSpec.IntValue applyFertilizerChance;
    private final ModConfigSpec.IntValue wellPulleyGeneration;
    private final ModConfigSpec.DoubleValue wetBiomeBonus;
    private final ModConfigSpec.DoubleValue dryBiomeBonus;

    private final ModConfigSpec.IntValue minBreedingGenerations;
    private final ModConfigSpec.DoubleValue domesticChance;
    private final ModConfigSpec.BooleanValue removeVanillaMobs;

    public YTechConfigSpec(@NotNull ModConfigSpec.Builder builder) {
        builder.push("waterBehavior");
        finiteWaterSource = builder.comment("If water sources are finite")
                .worldRestart().define("finiteWaterSource", true);
        cropsNeedWateredFarmland = builder.comment("If crops need watered farmland for growing")
                .worldRestart().define("cropsNeedWateredFarmland", true);
        farmlandConsumesWater = builder.comment("If hydrating farmland causes removing water source")
                .worldRestart().define("farmlandConsumesWater", true);
        builder.pop();
        builder.push("dryingRack");
        noDryingDuringRain = builder.comment("If Drying Rack should stop working during rain")
                .worldRestart().define("noDryingDuringRain", true);
        builder.pop();
        builder.push("irrigation");
        builder.push("aqueduct");
        baseFluidStoragePerBlock = builder.comment("Base amount of fluid stored per block")
                .worldRestart().defineInRange("baseFluidStoragePerBlock", 500, 1, Integer.MAX_VALUE);
        rainingFillAqueduct = builder.comment("If raining should fill aqueduct")
                .worldRestart().define("rainingFillAqueduct", true);
        rainingFillAmount = builder.comment("Amount of which will be aqueduct filled per nth tick when raining")
                .worldRestart().defineInRange("rainingFillAmount", 1, 1, Integer.MAX_VALUE);
        rainingFillPerNthTick = builder.comment("How often should be filled aqueduct when raining (1 - every tick, 20 - every second)")
                .worldRestart().defineInRange("rainingFillPerNthTick", 10, 1, Integer.MAX_VALUE);
        validBlockForRaining = builder.comment("If aqueduct must see sky and must be in valid biome where is raining when raining for filling")
                .worldRestart().define("validBlockForRaining", true);
        builder.pop();
        builder.push("valve");
        valveFillAmount = builder.comment("Amount of which will be aqueduct filled every nth tick thru valve")
                .worldRestart().defineInRange("valveFillAmount", 1, 1, Integer.MAX_VALUE);
        valveFillPerNthTick = builder.comment("How often should be filled aqueduct thru valve (1 - every tick, 20 - every second)")
                .worldRestart().defineInRange("valveFillPerNthTick", 10, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("hydrator");
        hydratorDrainAmount = builder.comment("Amount of which will be aqueduct drained every nth tick thru hydrator")
                .worldRestart().defineInRange("hydratorDrainAmount", 100, 1, Integer.MAX_VALUE);
        hydratorDrainPerNthTick = builder.comment("How often should be drained aqueduct thru hydrator (1 - every tick, 20 - every second)")
                .worldRestart().defineInRange("hydratorDrainPerNthTick", 200, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("fertilizer");
        fertilizerDuration = builder.comment("How long last single piece of fertilizer")
                .worldRestart().defineInRange("fertilizerDuration", 600, 1, Integer.MAX_VALUE);
        applyFertilizerChance = builder.comment("How often should be applied bone meal effect (1 / n chance per tick)")
                .worldRestart().defineInRange("applyFertilizerChance", 60, 1, Integer.MAX_VALUE);
        builder.pop();
        builder.push("wellPulley");
        wellPulleyGeneration = builder.comment("How much water Well Pulley generate each 10 ticks")
                .worldRestart().defineInRange("wellPulleyGeneration", 100, 1, Integer.MAX_VALUE);
        wetBiomeBonus = builder.comment("Bonus for Well Pulley generation in wet biome")
                .worldRestart().defineInRange("wetBiomeBonus", 2.0, Double.MIN_VALUE, Double.MAX_VALUE);
        dryBiomeBonus = builder.comment("Bonus for Well Pulley generation in dry biome")
                .worldRestart().defineInRange("dryBiomeBonus", 0.5, Double.MIN_VALUE, Double.MAX_VALUE);
        builder.pop();
        builder.pop();
        builder.push("wildAnimalsBreeding");
        minBreedingGenerations = builder.comment("Minimum generations for domestic animal breeding result")
                .worldRestart().defineInRange("minBreedingGenerations", 3, 1, Integer.MAX_VALUE);
        domesticChance = builder.comment("Chance for domestic animal breeding result, multiplied by each generation after minBreedingGenerations")
                .worldRestart().defineInRange("domesticChance", 0.2, Double.MIN_NORMAL, 1.0);
        removeVanillaMobs = builder.comment("If remove vanilla mobs spawning (sheeps, cows, chickens and pigs)")
                .worldRestart().define("removeVanillaMobs", true);
        builder.pop();
    }

    public boolean hasFiniteWaterSource() {
        return finiteWaterSource.get();
    }

    public boolean cropsNeedWateredFarmland() {
        return cropsNeedWateredFarmland.get();
    }

    public boolean farmlandConsumesWater() {
        return farmlandConsumesWater.get();
    }

    public boolean noDryingDuringRain() {
        return noDryingDuringRain.get();
    }

    public int getBaseFluidStoragePerBlock() {
        return baseFluidStoragePerBlock.get();
    }

    public boolean shouldRainingFillAqueduct() {
        return rainingFillAqueduct.get();
    }

    public int getRainingFillAmount() {
        return rainingFillAmount.get();
    }

    public int getValveFillAmount() {
        return valveFillAmount.get();
    }

    public int getValveFillPerNthTick() {
        return valveFillPerNthTick.get();
    }

    public int getHydratorDrainAmount() {
        return hydratorDrainAmount.get();
    }

    public int getHydratorDrainPerNthTick() {
        return hydratorDrainPerNthTick.get();
    }

    public int getRainingFillPerNthTick() {
        return rainingFillPerNthTick.get();
    }

    public boolean isValidBlockForRaining() {
        return validBlockForRaining.get();
    }

    public int getFertilizerDuration() {
        return fertilizerDuration.get();
    }

    public int getApplyFertilizerChance() {
        return applyFertilizerChance.get();
    }

    public int getWellPulleyGeneration() {
        return wellPulleyGeneration.get();
    }

    public double getWellPulleyWetBonus() {
        return wetBiomeBonus.get();
    }

    public double getWellPulleyDryBonus() {
        return dryBiomeBonus.get();
    }

    public int getMinBreedingGenerations() {
        return minBreedingGenerations.get();
    }

    public double getDomesticChance() {
        return domesticChance.get();
    }

    public boolean removeVanillaMobs() {
        return removeVanillaMobs.get();
    }
}
