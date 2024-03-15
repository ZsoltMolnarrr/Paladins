package net.paladins.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.paladins.PaladinsMod;
import net.paladins.entity.BarrierEntity;
import net.paladins.item.armor.PaladinArmor;
import net.paladins.item.armor.PriestArmor;

import java.util.List;
import java.util.Map;

public class SoundHelper {
    public static final SoundEvent divineProtectionImpact = SoundEvent.of(new Identifier(PaladinsMod.ID, "divine_protection_impact"));

    public static List<String> soundKeys = List.of(
            "divine_protection_release",
            "judgement_impact",
            "divine_protection_impact",
            "battle_banner_release",
            "battle_banner_presence",
            "holy_shock_damage",
            "holy_shock_heal",
            "holy_beam_start_casting",
            "holy_beam_casting",
            "holy_beam_damage",
            "holy_beam_heal",
            "holy_beam_release"
    );

    public static Map<String, Float> soundDistances = Map.of(
        "judgement_impact", Float.valueOf(48F)
    );

    public static void registerSounds() {
        for (var soundKey: soundKeys) {
            var soundId = new Identifier(PaladinsMod.ID, soundKey);
            var customTravelDistance = soundDistances.get(soundKey);
            var soundEvent = (customTravelDistance == null)
                    ? SoundEvent.of(soundId)
                    : SoundEvent.of(soundId, customTravelDistance);
            Registry.register(Registries.SOUND_EVENT, soundId, soundEvent);
        }

        Registry.register(Registries.SOUND_EVENT, PaladinArmor.equipSoundId, PaladinArmor.equipSound);
        Registry.register(Registries.SOUND_EVENT, PriestArmor.equipSoundId, PriestArmor.equipSound);
        Registry.register(Registries.SOUND_EVENT, BarrierEntity.activateSoundId, BarrierEntity.activateSound);
        Registry.register(Registries.SOUND_EVENT, BarrierEntity.idleSoundId, BarrierEntity.idleSound);
        Registry.register(Registries.SOUND_EVENT, BarrierEntity.impactSoundId, BarrierEntity.impactSound);
        Registry.register(Registries.SOUND_EVENT, BarrierEntity.deactivateSoundId, BarrierEntity.deactivateSound);
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
