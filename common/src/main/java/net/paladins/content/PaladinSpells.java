package net.paladins.content;

import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.effect.PaladinEffects;
import net.paladins.entity.PaladinEntities;
import net.paladins.entity.PaladinSummons;
import net.spell_engine.api.datagen.SpellBuilder;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.fx.PlayerAnimation;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ModelEffect;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PaladinSpells {

    public enum WeaponGroup { HOLY_WAND, HOLY_STAFF }
    public enum Book {
        PALADIN("Paladin Libram", "Paladin Spell Scroll",
                "Spell Book of Paladins, using weapons and spells to attack enemies and support allies\n- Strengths: Versatile in battle, able to heal, strike, and defend\n- Weaknesses: Limited ranged attacks and mobility\n- Equipment: Heavily armored"),
        PRIEST("Holy Book", "Holy Spell Scroll",
                "Spell Book of Priests, using holy magic to heal allies and damage enemies\n- Strengths: Supporting and healing allies, destroying undead\n- Weaknesses: Low damage and poor physical defense\n- Equipment: Lightly armored");

        /** Display name of the generated spell book item. Source for {@code item.paladins.spell_book/<book>}. */
        public final String bookName;
        /** Display name of the generated spell scroll item. Source for {@code item.paladins.spell_scroll/<book>}. */
        public final String scrollName;
        /** Spell binding tooltip. Source for {@code item.paladins.spell_book/<book>.spell_binding.description}. */
        public final String bindingDescription;
        Book(String bookName, String scrollName, String bindingDescription) {
            this.bookName = bookName;
            this.scrollName = scrollName;
            this.bindingDescription = bindingDescription;
        }
    }
    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator,
                        @Nullable List<WeaponGroup> weaponGroups,
                        @Nullable Book book) {
        public Entry(Identifier id, Spell spell, String title, String description) {
            this(id, spell, title, description, null, List.of(), null);
        }
        public Entry mutator(SpellTooltip.DescriptionMutator mutator) {
            return new Entry(id, spell, title, description, mutator, weaponGroups, book);
        }
        public Entry weaponGroup(WeaponGroup weaponGroup) {
            var newGroups = new ArrayList<>(weaponGroups != null ? weaponGroups : List.of());
            newGroups.add(weaponGroup);
            return new Entry(id, spell, title, description, mutator, newGroups, book);
        }
        public Entry book(Book book) {
            return new Entry(id, spell, title, description, mutator, weaponGroups, book);
        }
    }
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static ParticleBatch castingParticles(String particleId) {
        return new ParticleBatch(
                particleId,
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                1, 0.05F, 0.1F);
    }

    private static final Identifier SPARKS_FLOAT = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPARK,
            SpellEngineParticles.MagicParticles.Motion.FLOAT).id();

    private static final Identifier SPARK_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPARK,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE).id();

    private static final Identifier HOLY_IMPACT_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HOLY,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE).id();

    private static final Identifier HOLY_IMPACT_FLOAT = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HOLY,
            SpellEngineParticles.MagicParticles.Motion.FLOAT).id();

    private static final Identifier HOLY_IMPACT_BURST = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HOLY,
            SpellEngineParticles.MagicParticles.Motion.BURST).id();

    private static final Identifier HEALING_PARTICLES = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HEAL,
            SpellEngineParticles.MagicParticles.Motion.ASCEND).id();

    private static final Identifier HOLY_SPELL_FLOAT = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPELL,
            SpellEngineParticles.MagicParticles.Motion.FLOAT).id();

    private static final Identifier HOLY_SPELL_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPELL,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE).id();

    public static final Entry FLASH_HEAL = add(flash_heal().book(Book.PALADIN));
    private static Entry flash_heal() {
        var id = Identifier.of(PaladinsMod.ID, "flash_heal");
        var title = "Flash Heal";
        var description = "Heals you or a friendly target by {heal} health points.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 16;
        spell.tier = 2;
        spell.order = 2;

        SpellBuilder.Casting.cast(spell, 0.5F, "spell_engine:one_handed_healing_charge");
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
        };

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_RELEASE.id(), 0);

        SpellBuilder.Target.aim(spell);
        spell.target.aim.use_caster_as_fallback = true;
        spell.target.aim.sticky = true;

        var heal = SpellBuilder.Impacts.heal(1.2F);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        30, 0.02F, 0.15F)
                        .color(Color.NATURE.toRGBA())
        };
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());

        spell.impacts = List.of(heal);

        SpellBuilder.Cost.cooldown(spell, 6);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.2F;

        return new Entry(id, spell, title, description);
    }

    public static final Entry DIVINE_PROTECTION = add(divine_protection().book(Book.PALADIN));
    private static Entry divine_protection() {
        var id = Identifier.of(PaladinsMod.ID, "divine_protection");
        var title = "Divine Protection";
        var description = "Protects you from the next incoming attacks (up to {effect_amplifier_cap}), for {effect_duration} seconds.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;
        spell.tier = 3;
        spell.order = 2;

        SpellBuilder.Casting.instant(spell);

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");

        var buff = SpellBuilder.Impacts.effectSet_ScaledAmplifier_Cap(
                PaladinEffects.DIVINE_PROTECTION.id.toString(), 8, 0, 0.5F, 2);
        buff.sound = new Sound(PaladinSounds.divine_protection_release.id());
        buff.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.2F, 0.2F).color(Color.HOLY.toRGBA())
        };

        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 30);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description);
    }

    public static final Entry JUDGEMENT = add(judgement().book(Book.PALADIN));
    private static Entry judgement() {
        var id = Identifier.of(PaladinsMod.ID, "judgement");
        var title = "Judgement";
        var description = "Strikes your target and enemies around it, within {impact_range} blocks, causing up to {damage} damage and stunning them for {effect_duration} seconds.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 16;
        spell.tier = 3;

        SpellBuilder.Casting.cast(spell, 0.5F, "spell_engine:one_handed_projectile_charge");
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
        };

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        SpellBuilder.Target.aim(spell);
        spell.target.aim.required = true;
        spell.target.aim.sticky = true;

        spell.deliver.type = Spell.Delivery.Type.METEOR;
        var meteor = new Spell.Delivery.Meteor();
        meteor.launch_height = 12;
        meteor.launch_properties.velocity = 1.2F;
        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 1;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 15;
        projectile.client_data.travel_particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_FLOAT.toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK,
                        5, 0, 0.1F, 0),
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK,
                        4, 0, 0.1F, 0)
                        .color(Color.HOLY.toRGBA())
        };
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single("paladins:spell_projectile/judgement", 1.2F, LightEmission.RADIATE);

        meteor.projectile = projectile;
        spell.deliver.meteor = meteor;

        var damage = SpellBuilder.Impacts.damage(0.9F, 1F);
        damage.target_modifiers = List.of(SpellBuilder.ImpactModifiers.extraDamageAgainstUndead());
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.2F, 1F).color(Color.HOLY.toRGBA())
        };

        var stun = SpellBuilder.Impacts.effectSet(PaladinEffects.JUDGEMENT.id.toString(), 3, 0);
        stun.action.status_effect.apply_limit = new Spell.Impact.Action.StatusEffect.ApplyLimit();
        stun.action.status_effect.apply_limit.health_base = 50F;
        stun.action.status_effect.apply_limit.spell_power_multiplier = 2F;

        spell.impacts = List.of(damage, stun);

        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 6;
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.SQUARED;
        spell.area_impact.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        100, 0.8F, 0.9F).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        100, 0.2F, 0.4F).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        "smoke",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        50, 0.1F, 0.3F)
        };
        spell.area_impact.sound = Sound.withVolume(PaladinSounds.judgement_impact.id(), 1.5F);

        SpellBuilder.Cost.cooldown(spell, 15);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");

        return new Entry(id, spell, title, description);
    }

    public static final Entry BATTLE_BANNER = add(battle_banner().book(Book.PALADIN));
    private static Entry battle_banner() {
        var id = Identifier.of(PaladinsMod.ID, "battle_banner");
        var title = "Battle Banner";
        var description = "Increases attack speed and knockback resistance for you and allies nearby, within {cloud_radius} blocks, for {cloud_duration} seconds.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;
        spell.tier = 4;
        spell.order = 2;

        SpellBuilder.Casting.instant(spell);

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = new Sound(PaladinSounds.battle_banner_release.id());

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.entity_type_id = PaladinEntities.BANNER_ID.toString();
        cloud.volume.radius = 3;
        cloud.volume.extra_radius = new Spell.AreaImpact.ExtraRadius();
        cloud.volume.extra_radius.power_coefficient = 1;
        cloud.volume.extra_radius.power_cap = 4;
        cloud.volume.area.vertical_range_multiplier = 0.3F;
        cloud.presence_sound = Sound.withRandomness(PaladinSounds.battle_banner_presence.id(), 0);
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 10;

        var spawnDurationTicks = 43;

        // Spawn/despawn phases sized to the `place` animation (2.15s = 43 ticks); the model
        // plays it forward while spawning and in reverse while despawning.
        cloud.spawn_ticks = spawnDurationTicks;
        cloud.despawn_ticks = spawnDurationTicks;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 15;
        cloud.client_data.particles = new ParticleBatch[] {
                new ParticleBatch(SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        null, 15, 0.1F, 0.15F, 0.0F, 0F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(HOLY_SPELL_FLOAT.toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 3, 0.05F, 0.1F, 0.0F, 0F)
                        .color(Color.HOLY.toRGBA())
        };
        cloud.placement = new Spell.EntityPlacement();
        cloud.placement.location_offset_by_look = 2;
        cloud.placement.location_yaw_offset = 20;
        cloud.placement.apply_yaw = true;

        // Dummy co-located vanilla cloud, only to emit dynamic light. LambDynLights handlers are
        // keyed by entity type (SpellCloud.ENTITY_TYPE), so the custom BannerEntity type gets no
        // dynamic light of its own. Zero radius and a beyond-lifetime impact interval make it
        // gameplay- and visually inert.
        var lightSource = new Spell.Delivery.Cloud();
        lightSource.volume.radius = 0;
        lightSource.impact_tick_interval = 10000;
        lightSource.time_to_live_seconds = 10;
        // Mirror the banner's phases so the light's total lifetime matches exactly
        lightSource.spawn_ticks = spawnDurationTicks;
        lightSource.despawn_ticks = spawnDurationTicks;
        lightSource.client_data = new Spell.Delivery.Cloud.ClientData();
        lightSource.client_data.light_level = 15;
        lightSource.placement = cloud.placement;

        spell.deliver.clouds = List.of(cloud, lightSource);

        var buff = SpellBuilder.Impacts.effectSet(PaladinEffects.BATTLE_BANNER.id.toString(), 2, 0);
        spell.impacts = List.of(buff);

        SpellBuilder.Cost.cooldown(spell, 45);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description);
    }

    public static final Entry IMMOLATION = add(immolation().book(Book.PALADIN));
    private static Entry immolation() {
        var id = Identifier.of(PaladinsMod.ID, "immolation");
        var title = "Immolation";
        var description = "Erupts in holy fire, dealing {damage} damage to all enemies around you.";

        float range = 4;

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = range;
        spell.tier = 4;

        SpellBuilder.Casting.instant(spell);

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_shout_release");
        spell.release.sound = new Sound(PaladinSounds.immolation_release.id());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        60, 0.4F, 0.5F)
                        .preSpawnTravel(1)
                        .color(Color.HOLY.toRGBA())
        };
        spell.release.particles_scaled_with_ranged = new ParticleBatch[] {
                new ParticleBatch(
                        SpellEngineParticles.area_effect_637.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        1, 0, 0)
                        .scale(0.6F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SpellEngineParticles.aura_effect_676.id().toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        1, 0, 0)
                        .scale(0.6F)
                        .color(Color.HOLY.toRGBA())
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.distance_dropoff = Spell.Target.Area.DropoffCurve.NONE;
        spell.target.area.vertical_range_multiplier = 0.5F;

        // Physical-melee spell, but this damage impact is powered by the paladin's Healing Spell Power
        // (impact-level school override; the spell's own school stays PHYSICAL_MELEE).
        var damage = SpellBuilder.Impacts.damage(1.2F, 1F);
        damage.school = SpellSchools.HEALING;
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F).color(Color.HOLY.toRGBA())
        };
        damage.sound = new Sound(PaladinSounds.holy_shock_damage.id());

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 12);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description);
    }

    public static final Entry HEAL = add(heal().weaponGroup(WeaponGroup.HOLY_WAND));
    private static Entry heal() {
        var id = Identifier.of(PaladinsMod.ID, "heal");
        var title = "Heal";
        var description = "Heals you or a friendly target by {heal} health points.";

        var spell = SpellBuilder.createWeaponSpell();
        spell.school = SpellSchools.HEALING;
        spell.range = 16;
        spell.tier = 0;

        spell.learn = null;

        SpellBuilder.Casting.cast(spell, 1F, "spell_engine:one_handed_healing_charge");
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
        };

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        SpellBuilder.Target.aim(spell);
        spell.target.aim.use_caster_as_fallback = true;
        spell.target.aim.sticky = true;

        var heal = SpellBuilder.Impacts.heal(0.5F);
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        20, 0.02F, 0.15F)
                        .color(Color.NATURE.toRGBA())
        };
        spell.impacts = List.of(heal);

        // createWeaponSpell sets cooldown group "weapon"; override with a plain 4s cooldown
        spell.cost.cooldown.group = null;
        SpellBuilder.Cost.cooldown(spell, 4);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");

        return new Entry(id, spell, title, description);
    }

    public static final Entry HOLY_SHOCK = add(holy_shock().weaponGroup(WeaponGroup.HOLY_STAFF));
    private static Entry holy_shock() {
        var id = Identifier.of(PaladinsMod.ID, "holy_shock");
        var title = "Holy Shock";
        var description = "Heals you or a friendly target by {heal}, smites an enemy dealing {damage} spell damage.";

        var spell = SpellBuilder.createWeaponSpell();
        spell.school = SpellSchools.HEALING;
        spell.tier = 1;
        spell.range = 16;

        SpellBuilder.Casting.cast(spell, 1.5F, "spell_engine:one_handed_projectile_charge");
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
        };

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        SpellBuilder.Target.aim(spell);
        spell.target.aim.sticky = true;
        spell.target.aim.use_caster_as_fallback = true;

        var heal = SpellBuilder.Impacts.heal(0.4F);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        15, 0.02F, 0.15F)
                        .color(Color.NATURE.toRGBA()),
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.25F).color(Color.HOLY.toRGBA())
        };
        heal.sound = new Sound(PaladinSounds.holy_shock_heal.id());

        var damage = SpellBuilder.Impacts.damage(0.8F, 0.5F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F).color(Color.HOLY.toRGBA())
        };
        damage.sound = new Sound(PaladinSounds.holy_shock_damage.id());

        spell.impacts = List.of(heal, damage);

        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.2F;
        SpellBuilder.Cost.cooldown(spell, 3F);

        return new Entry(id, spell, title, description);
    }

    public static final Entry HOLY_BEAM = add(holy_beam().book(Book.PRIEST));
    private static Entry holy_beam() {
        var id = Identifier.of(PaladinsMod.ID, "holy_beam");
        var title = "Holy Light";
        var description = "Channels a beam of light, healing friends by {heal}, and dealing {damage} spell damage to enemies every second.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 32;
        spell.tier = 2;

        SpellBuilder.Casting.channel(spell, 5, 25);
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:two_handed_channeling");
        spell.active.cast.start_sound = new Sound(PaladinSounds.holy_beam_start_casting.id());
        spell.active.cast.sound = Sound.withRandomness(PaladinSounds.holy_beam_casting.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT, ParticleBatch.Rotation.LOOK,
                        3, 0.1F, 0.2F, 0)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        "firework",
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT, ParticleBatch.Rotation.LOOK,
                        0.5F, 0.1F, 0.2F, 0)
        };

        spell.release.sound = new Sound(PaladinSounds.holy_beam_release.id());

        spell.target.type = Spell.Target.Type.BEAM;
        var beam = new Spell.Target.Beam();
        beam.color_rgba = 0xFFCC66FFL;
        beam.flow = 1.5F;
        beam.block_hit_particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_SPELL_FLOAT.toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,
                        1F, 0.1F, 0.2F, 0)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        "firework",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,
                        1F, 0.1F, 0.2F, 0),
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        5, 0.1F, 0.2F)
                        .color(Color.HOLY.toRGBA())
        };
        spell.target.beam = beam;

        var heal = SpellBuilder.Impacts.heal(0.4F);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        1, 0.02F, 0.15F)
                        .color(Color.NATURE.toRGBA()),
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        1, 0.2F, 0.25F)
                        .color(Color.HOLY.toRGBA())
        };
        heal.sound = new Sound(PaladinSounds.holy_beam_heal.id());

        var damage = SpellBuilder.Impacts.damage(0.8F, 0.5F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        3, 0.2F, 0.7F)
                        .color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        6, 0.2F, 0.4F)
                        .color(Color.HOLY.toRGBA())
        };
        damage.sound = new Sound(PaladinSounds.holy_beam_damage.id());

        spell.impacts = List.of(heal, damage);

        SpellBuilder.Cost.cooldown(spell, 10);
        spell.cost.cooldown.proportional = true;
        spell.cost.exhaust = 0.2F;
        SpellBuilder.Cost.item(spell, "runes:healing_stone");

        return new Entry(id, spell, title, description);
    }

    public static final Entry CIRCLE_OF_HEALING = add(circle_of_healing().book(Book.PRIEST));
    private static Entry circle_of_healing() {
        var id = Identifier.of(PaladinsMod.ID, "circle_of_healing");
        var title = "Circle of Healing";
        var description = "Heals you and friendly targets around you by {heal}, and applies some absorption for {effect_duration} seconds.";

        float range = 8;

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = range;
        spell.tier = 3;

        SpellBuilder.Casting.cast(spell, 0.5F, "spell_engine:one_handed_area_charge");
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
        };

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        100, 0.3F, 0.5F
                ).extent(range - 0.5F).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        HOLY_SPELL_DECELERATE.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        50, 0.1F, 0.5F
                ).extent(range - 0.5F).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        HOLY_IMPACT_FLOAT.toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        50, 0.1F, 0.2F
                ).extent(range).color(Color.HOLY.toRGBA())
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 0.6F;
        spell.target.area.include_caster = true;

        var heal = SpellBuilder.Impacts.heal(0.4F);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        15, 0.02F, 0.15F)
                        .color(Color.NATURE.toRGBA()),
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.25F).color(Color.HOLY.toRGBA())
        };
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_2.id());

        var buff = SpellBuilder.Impacts.effectSet_ScaledAmplifier(
                PaladinEffects.ABSORPTION.id.toString(), 6, 0, 0.25F);

        spell.impacts = List.of(heal, buff);

        SpellBuilder.Cost.cooldown(spell, 10);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description);
    }

    public static final Entry BARRIER = add(barrier().book(Book.PRIEST));
    private static Entry barrier() {
        var id = Identifier.of(PaladinsMod.ID, "barrier");
        var title = "Barrier";
        var description = "Summons a circular barrier, protecting you and allies from projectiles, magic or enemies intruding the area.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 4;
        spell.tier = 4;
        spell.order = 2;

        SpellBuilder.Casting.cast(spell, 0.5F, "spell_engine:one_handed_area_charge");
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
        };

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");
        spell.release.sound = new Sound(PaladinSounds.holy_barrier_activate.id());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_SPELL_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        50, 1F, 1F).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        50, 1F, 1F).color(Color.HOLY.toRGBA())
        };

        var spawn = new Spell.Impact();
        spawn.action = new Spell.Impact.Action();
        spawn.action.type = Spell.Impact.Action.Type.SPAWN;
        var barrier = new Spell.Impact.Action.Spawn();
        barrier.entity_type_id = PaladinEntities.BARRIER_ID.toString();
        barrier.time_to_live_seconds = 10;
        spawn.action.spawns = List.of(barrier);
        spell.impacts = List.of(spawn);

        SpellBuilder.Cost.cooldown(spell, 40);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.4F;

        return new Entry(id, spell, title, description);
    }

    public static final Entry LIGHTWELL = add(lightwell().book(Book.PRIEST));
    private static Entry lightwell() {
        var id = Identifier.of(PaladinsMod.ID, "lightwell");
        var title = "Lightwell";
        var description = "Summons a Lightwell that heals you and nearby wounded allies. It lasts "
                + SpellTooltip.placeholder(SpellTooltip.summonDurationToken) + " sec.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;
        spell.tier = 4;

        SpellBuilder.Casting.instant(spell);

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");

        spell.impacts = List.of(SpellBuilder.Impacts.summon(PaladinSummons.lightwell()));

        SpellBuilder.Cost.cooldown(spell, 45);
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description);
    }

    // Heal cast by the Lightwell summon on nearby wounded allies. Not learnable and bound to no
    // book/weapon — it exists only to be referenced by PaladinSummons.lightwell()'s SpellCast action.
    // Same heal payload as the priest's Heal, but lobbed as a holy orb (Celestial-Orbs model) that
    // arcs upward and homes back down onto the ally, bouncing once off terrain. Must be instant so the
    // summon can cast it (summons can't channel); scales off the well's own healing spell power.
    public static final Entry LIGHTWELL_ORB = add(lightwell_orb());
    private static Entry lightwell_orb() {
        var id = Identifier.of(PaladinsMod.ID, "lightwell_orb");
        var title = "Holy Mote";
        var description = "Heals a friendly target by {heal} health points.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 12;
        spell.tier = 0;
        spell.learn = null;

        SpellBuilder.Casting.instant(spell);

        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_healing_release");
        spell.release.sound = Sound.of(SpellEngineSounds.GENERIC_HEALING_RELEASE_2.id());

        SpellBuilder.Target.aim(spell);
        spell.target.aim.required = true;

        // Delivery: a holy orb lobbed 40° above the aim line, arcing back down onto the ally via homing,
        // and able to ricochet once off terrain along the way.
        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.direction_offsets = new Spell.Delivery.ShootProjectile.DirectionOffset[] {
                new Spell.Delivery.ShootProjectile.DirectionOffset(0, -30) // negative pitch = aim upwards
        };
        spell.deliver.projectile.launch_properties.velocity = 1.0F;

        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 16F;
        projectile.homing_after_relative_distance = 0.15F; // fly up first, then curve toward the ally
        projectile.perks.bounce = 1;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 12;
        projectile.client_data.travel_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK,
                        5, 0, 0.1F, 0).color(Color.HOLY.toRGBA())
        };
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.single(
                "paladins:spell_projectile/lightwell_orb", 1.0F, LightEmission.GLOW);
        spell.deliver.projectile.projectile = projectile;

        var heal = SpellBuilder.Impacts.heal(0.35F);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        15, 0.02F, 0.15F)
                        .color(Color.NATURE.toRGBA()),
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        12, 0.2F, 0.25F).color(Color.HOLY.toRGBA())
        };
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_2.id());

        spell.impacts = List.of(heal);

        // Firing cadence lives on the spell (not the summon's SpellCast action) so it runs through
        // SpellEngine's haste-aware cooldown path: the well pulses faster the more Healing Haste its
        // owner has (mirrored onto the well via attribute scaling). haste_affected is on by default;
        // set explicitly for intent. Also obeys the server's `haste_affects_cooldown` config.
        SpellBuilder.Cost.cooldown(spell, 1.5F);
        spell.cost.cooldown.haste_affected = true;

        return new Entry(id, spell, title, description);
    }
}
