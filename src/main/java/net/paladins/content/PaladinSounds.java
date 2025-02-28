package net.paladins.content;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.paladins.PaladinsMod;

import java.util.ArrayList;
import java.util.List;

public class PaladinSounds {
    public static final class Entry {
        private final Identifier id;
        private final SoundEvent soundEvent;
        private RegistryEntry<SoundEvent> entry;
        private int variants = 1;

        public Entry(Identifier id, SoundEvent soundEvent) {
            this.id = id;
            this.soundEvent = soundEvent;
        }

        public Entry(String name) {
            this(Identifier.of(PaladinsMod.ID, name));
        }

        public Entry(Identifier id) {
            this(id, SoundEvent.of(id));
        }

        public Entry travelDistance(float distance) {
            return new Entry(id, SoundEvent.of(id, distance));
        }

        public Entry variants(int variants) {
            this.variants = variants;
            return this;
        }

        public Identifier id() {
            return id;
        }

        public SoundEvent soundEvent() {
            return soundEvent;
        }

        public RegistryEntry<SoundEvent> entry() {
            return entry;
        }

        public int variants() {
            return variants;
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry paladin_armor_equip = add(new Entry("plate_equip").variants(3));
    public static final Entry priest_robe_equip = add(new Entry("cloth_equip").variants(3));
    public static final Entry shield_equip = add(new Entry("shield_equip"));
    public static final Entry holy_barrier_activate = add(new Entry("holy_barrier_activate"));
    public static final Entry holy_barrier_idle = add(new Entry("holy_barrier_idle"));
    public static final Entry holy_barrier_impact = add(new Entry("holy_barrier_impact"));
    public static final Entry holy_barrier_deactivate = add(new Entry("holy_barrier_deactivate"));
    public static final Entry divine_protection_release = add(new Entry("divine_protection_release"));
    public static final Entry judgement_impact = add(new Entry("judgement_impact").travelDistance(48F));
    public static final Entry divine_protection_impact = add(new Entry("divine_protection_impact"));
    public static final Entry battle_banner_release = add(new Entry("battle_banner_release"));
    public static final Entry battle_banner_presence = add(new Entry("battle_banner_presence"));
    public static final Entry holy_shock_damage = add(new Entry("holy_shock_damage"));
    public static final Entry holy_shock_heal = add(new Entry("holy_shock_heal"));
    public static final Entry holy_beam_start_casting = add(new Entry("holy_beam_start_casting"));
    public static final Entry holy_beam_casting = add(new Entry("holy_beam_casting"));
    public static final Entry holy_beam_damage = add(new Entry("holy_beam_damage"));
    public static final Entry holy_beam_heal = add(new Entry("holy_beam_heal"));
    public static final Entry holy_beam_release = add(new Entry("holy_beam_release"));

    public static void register() {
        for (var entry: entries) {
            entry.entry = Registry.registerReference(Registries.SOUND_EVENT, entry.id(), entry.soundEvent());
        }
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
