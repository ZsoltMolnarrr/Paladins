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
        private String soundFile;

        public Entry(Identifier id, SoundEvent soundEvent) {
            this.id = id;
            this.soundEvent = soundEvent;
        }

        public Entry(String name) {
            this(new Identifier(PaladinsMod.ID, name));
        }

        public Entry(Identifier id) {
            this(id, SoundEvent.of(id));
        }

        public Entry travelDistance(float distance) {
            var copy = new Entry(id, SoundEvent.of(id, distance));
            copy.variants = variants;
            copy.soundFile = soundFile;
            return copy;
        }

        public Entry variants(int variants) {
            this.variants = variants;
            return this;
        }

        /// Play the `.ogg` files of another sound, instead of files named after this entry.
        public Entry soundFile(String soundFile) {
            this.soundFile = soundFile;
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

        public String soundFile() {
            return soundFile != null ? soundFile : id.getPath();
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    public static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final Entry paladin_armor_equip = add(new Entry("plate_equip").variants(3));
    public static final Entry priest_robe_equip = add(new Entry("cloth_equip").variants(3));
    public static final Entry shield_equip = add(new Entry("shield_equip").soundFile("plate_equip").variants(3));
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
    public static final Entry holy_ward_impact = add(new Entry("holy_ward_impact"));
    public static final Entry penance_impact =  add(new Entry("penance_impact"));
    public static final Entry penance_release =  add(new Entry("penance_release").variants(3));
    public static final Entry lightwell_spawn = add(new Entry("lightwell_spawn"));
    public static final Entry lightwell_despawn = add(new Entry("lightwell_despawn"));
    public static final Entry lightwell_ambient = add(new Entry("lightwell_ambient"));
    public static final Entry immolation_release = add(new Entry("immolation_release"));
    public static final Entry blessed_strike_start =  add(new Entry("blessed_strike_start"));
    public static final Entry blessed_strike_casting = add(new Entry("blessed_strike_casting"));
    public static final Entry blessed_strike_release = add(new Entry("blessed_strike_release"));

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