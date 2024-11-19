package com.yanny.ytech.generation;

import com.yanny.ytech.YTechMod;
import com.yanny.ytech.configuration.Utils;
import com.yanny.ytech.registration.YTechSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class YTechSoundDefinitionProvider extends SoundDefinitionsProvider {
    protected YTechSoundDefinitionProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, YTechMod.MOD_ID, helper);
    }

    @Override
    public void registerSounds() {
        add(YTechSoundEvents.SABER_TOOTH_TIGER_AMBIENT, definition()
                .subtitle("sound.ytech.saber_tooth_tiger.ambient")
                .with(
                        sound(Utils.modLoc("entity/saber_tooth_tiger/ambient"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.SABER_TOOTH_TIGER_HURT, definition()
                .subtitle("sound.ytech.saber_tooth_tiger.hurt")
                .with(
                        sound(Utils.modLoc("entity/saber_tooth_tiger/hurt"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.SABER_TOOTH_TIGER_DEATH, definition()
                .subtitle("sound.ytech.saber_tooth_tiger.death")
                .with(
                        sound(Utils.modLoc("entity/saber_tooth_tiger/death"))
                                .volume(0.5)
                )
        );

        add(YTechSoundEvents.TERROR_BIRD_AMBIENT, definition()
                .subtitle("sound.ytech.terror_bird.ambient")
                .with(
                        sound(Utils.modLoc("entity/terror_bird/ambient"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.TERROR_BIRD_HURT, definition()
                .subtitle("sound.ytech.terror_bird.hurt")
                .with(
                        sound(Utils.modLoc("entity/terror_bird/hurt"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.TERROR_BIRD_DEATH, definition()
                .subtitle("sound.ytech.terror_bird.death")
                .with(
                        sound(Utils.modLoc("entity/terror_bird/death"))
                                .volume(0.5)
                )
        );

        add(YTechSoundEvents.WOOLLY_MAMMOTH_AMBIENT, definition()
                .subtitle("sound.ytech.woolly_mammoth.ambient")
                .with(
                        sound(Utils.modLoc("entity/woolly_mammoth/ambient"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.WOOLLY_MAMMOTH_HURT, definition()
                .subtitle("sound.ytech.woolly_mammoth.hurt")
                .with(
                        sound(Utils.modLoc("entity/woolly_mammoth/hurt"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.WOOLLY_MAMMOTH_DEATH, definition()
                .subtitle("sound.ytech.woolly_mammoth.death")
                .with(
                        sound(Utils.modLoc("entity/woolly_mammoth/death"))
                                .volume(0.25)
                )
        );

        add(YTechSoundEvents.WOOLLY_RHINO_AMBIENT, definition()
                .subtitle("sound.ytech.woolly_rhino.ambient")
                .with(
                        sound(Utils.modLoc("entity/woolly_rhino/ambient1"))
                                .volume(0.5),
                        sound(Utils.modLoc("entity/woolly_rhino/ambient2"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.WOOLLY_RHINO_HURT, definition()
                .subtitle("sound.ytech.woolly_rhino.hurt")
                .with(
                        sound(Utils.modLoc("entity/woolly_rhino/hurt"))
                                .volume(0.5)
                )
        );
        add(YTechSoundEvents.WOOLLY_RHINO_DEATH, definition()
                .subtitle("sound.ytech.woolly_rhino.death")
                .with(
                        sound(Utils.modLoc("entity/woolly_rhino/death"))
                                .volume(0.5)
                )
        );

        add(YTechSoundEvents.BRONZE_ANVIL_USE, definition()
                .subtitle("sound.ytech.bronze_anvil.use")
                .with(
                        sound(SoundEvents.ANVIL_USE.getLocation(), SoundDefinition.SoundType.EVENT)
                                .pitch(2.5)
                )
        );
        add(YTechSoundEvents.TANNING_RACK_USE, definition()
                .subtitle("sound.ytech.tanning_rack.use")
                .with(
                        sound(SoundEvents.HOE_TILL.getLocation(), SoundDefinition.SoundType.EVENT)
                                .pitch(2.5)
                )
        );
        add(YTechSoundEvents.TREE_STUMP_USE, definition()
                .subtitle("sound.ytech.tree_stump.use")
                .with(
                        sound(SoundEvents.AXE_STRIP.getLocation(), SoundDefinition.SoundType.EVENT)
                                .pitch(2.0)
                )
        );
        add(YTechSoundEvents.WELL_PULLEY_USE, definition()
                .subtitle("sound.ytech.well_pulley.use")
                .with(
                        sound(SoundEvents.WATER_AMBIENT.getLocation(), SoundDefinition.SoundType.EVENT)
                )
        );
    }
}
