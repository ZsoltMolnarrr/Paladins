package net.paladins.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.paladins.PaladinsMod;

import java.util.List;
import java.util.Map;

public class SoundHelper {
    public static List<String> soundKeys = List.of(
            "flash_heal_impact",
            "divine_protection_release",
            "judgement_impact",
            "holy_shock_damage",
            "holy_shock_heal",
            "holy_beam_damage",
            "holy_beam_heal",
            "circle_of_healing_impact"
            // "frost_shield_release"
    );

    public static Map<String, Float> soundDistances = Map.of(
//            "fire_meteor_impact", Float.valueOf(48F)
    );

    public static void registerSounds() {
        for (var soundKey: soundKeys) {
            var soundId = new Identifier(PaladinsMod.ID, soundKey);
            var customTravelDistance = soundDistances.get(soundKey);
            var soundEvent = (customTravelDistance == null)
                    ? new SoundEvent(soundId)
                    : new SoundEvent(soundId, customTravelDistance);
            Registry.register(Registry.SOUND_EVENT, soundId, soundEvent);
        }

//        Registry.register(Registry.SOUND_EVENT, PaladinArmor.equipSoundId, PaladinArmor.equipSound);
//        Registry.register(Registry.SOUND_EVENT, PriestArmor.equipSoundId, PaladinArmor.equipSound);
    }

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent) {
        playSoundEvent(world, entity, soundEvent, 1, 1);
    }

    public static void playSoundEvent(World world, Entity entity, SoundEvent soundEvent, float volume, float pitch) {
        world.playSound(
                (PlayerEntity)null,
                entity.getX(),
                entity.getY(),
                entity.getZ(),
                soundEvent,
                SoundCategory.PLAYERS,
                volume,
                pitch);
    }
}
