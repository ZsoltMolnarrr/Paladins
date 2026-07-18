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
import net.spell_engine.internals.target.SpellTarget;
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

    // MARK: Spell groups
    // Prefixed by the book they belong to, since a group is only ever read alongside the groups of
    // other mods, where a bare `protection` would say nothing about whose protection it is.

    public static final String RETRIBUTION = "paladin_retribution";
    public static final String PROTECTION = "paladin_protection";
    public static final String HOLY = "priest_holy";
    public static final String DISCIPLINE = "priest_discipline";

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
        spell.group = PROTECTION;

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

    public static final Entry BLESSED_STRIKES = add(blessed_strikes().book(Book.PALADIN));
    private static Entry blessed_strikes() {
        var id = Identifier.of(PaladinsMod.ID, "blessed_strikes");
        var title = "Blessed Strikes";
        var description = "Channels holy light into your weapon, blessing it up to 5 times. Each melee strike spends a blessing to deal {damage} additional spell damage.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 0;
        spell.tier = 2;
        spell.group = RETRIBUTION;



//        SpellBuilder.Casting.cast(spell, 0.5F, "spell_engine:one_handed_projectile_charge");
//        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
//        spell.active.cast.particles = new ParticleBatch[] {
//                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
//        };
//
//        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");
//        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());



        // 2.5s channel, 5 releases (one every 0.5s). Each release re-runs the STASH_EFFECT delivery,
        // and with `stacking` on that adds one seal — so the weapon lights up charge by charge as it is
        // channeled. Releasing the channel early simply yields fewer seals.
        SpellBuilder.Casting.channel(spell, 2.5F, 5);
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_projectile_charge");
        spell.active.cast.animation_pitch = false;
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        // Holy sparks gathering into the weapon while channeling. Pre-spawn travel throws them out to
        // arm's length and `invert` turns them around, so they fall back inwards — light drawn into the
        // blade, seal by seal.
        spell.active.cast.particles = new ParticleBatch[] {
                sealSparks().invert().preSpawnTravel(14)
        };

        // The same sparks let loose outwards: the gathered light flares off the weapon as the seals set.
        spell.release.particles = new ParticleBatch[] { sealSparks() };
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());
        spell.release.animation = PlayerAnimation.of("spell_engine:one_handed_area_release");

        // The seals are stashed on the caster themselves.
        spell.target.type = Spell.Target.Type.CASTER;

        // Each melee hit fires the stashed spell (the damage impact below) on the struck enemy and
        // consumes one seal. No `target_override`: the impacts land on the melee victim, not the caster.
        var meleeTrigger = new Spell.Trigger();
        meleeTrigger.type = Spell.Trigger.Type.MELEE_IMPACT;

        SpellBuilder.Deliver.stash(spell, PaladinEffects.BLESSED_STRIKES.id.toString(), 15F, meleeTrigger);
        spell.deliver.stash_effect.stacking = true; // add seals one by one (one per channel release)
        spell.deliver.stash_effect.amplifier = 4;   // cap: amplifier is "stacks - 1", so 4 => 5 seals
        spell.deliver.stash_effect.consume = 1;     // one seal spent per melee hit
        // Defer the seal decrement to next tick instead of removing it inline. Each enemy struck this
        // tick then still reads the seal as present, so a single seal sears every foe caught in one
        // multi-target swing (Better Combat sweep / cleave), yet only one seal is spent for the swing.
        spell.deliver.stash_effect.consumed_next_tick = true;

        // Same holy burst as Holy Shock's damage.
        // Hybrid power: 25% melee / 75% healing (base PHYSICAL_MELEE weighs 1, healing weighs 3).
        var damage = SpellBuilder.Impacts.damage(0.8F, 0.5F);
        damage.power_blend = List.of(SpellBuilder.Impacts.powerBlend(SpellSchools.HEALING, 3F));
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F).color(Color.HOLY.toRGBA())
        };
        damage.sound = new Sound(PaladinSounds.holy_shock_damage.id());

        spell.impacts = List.of(damage);

        SpellBuilder.Cost.cooldown(spell, 12);
        spell.cost.cooldown.proportional = true; // channel cut short => proportionally shorter cooldown
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.2F;

        return new Entry(id, spell, title, description);
    }

    /// A loose sphere of slow holy sparks at the caster's weapon hand. Flows outwards on its own;
    /// call `invert()` (paired with `preSpawnTravel`) to have it converge inwards instead.
    private static ParticleBatch sealSparks() {
        return new ParticleBatch(
                SPARKS_FLOAT.toString(),
                ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.OVER_HEAD,
                4, 0.02F, 0.1F)
                .color(Color.HOLY.toRGBA());
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
        spell.group = PROTECTION;

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
        spell.group = RETRIBUTION;

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

        // Hybrid power: 75% melee / 25% healing (base PHYSICAL_MELEE weighs 1, healing weighs 1/3).
        var damage = SpellBuilder.Impacts.damage(0.9F, 1F);
        damage.power_blend = List.of(SpellBuilder.Impacts.powerBlend(SpellSchools.HEALING, 1F / 3F));
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
        spell.group = PROTECTION;

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
        var description = "Erupts in holy fire, dealing {damage} damage to nearby enemies and setting them ablaze, while healing you and nearby allies by {heal}.";

        float range = 5;

        var spell = SpellBuilder.createSpellActive();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = range;
        spell.tier = 4;
        spell.group = RETRIBUTION;

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
        // The flames wash over allies too. Impacts are intent-filtered per target, so the DAMAGE/FIRE
        // impacts below only land on enemies and the HEAL only on allies (and the caster) — one burst
        // that both burns and mends.
        spell.target.area.include_caster = true;

        // Hybrid power: 25% melee / 75% healing (base PHYSICAL_MELEE weighs 1, healing weighs 3).
        var damage = SpellBuilder.Impacts.damage(1.2F, 1F);
        damage.power_blend = List.of(SpellBuilder.Impacts.powerBlend(SpellSchools.HEALING, 3F));
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F).color(Color.HOLY.toRGBA())
        };
        damage.sound = new Sound(PaladinSounds.holy_shock_damage.id());
        // Holy fire purges the undead: +50% power and a guaranteed critical strike against them.
        damage.target_modifiers = List.of(
                SpellBuilder.ImpactModifiers.extraDamageAgainstUndead(),
                SpellBuilder.ImpactModifiers.alwaysCritAgainstUndead());

        // Ignite: enemies caught in the eruption keep burning afterwards (HARMFUL intent, enemies only).
        var ignite = SpellBuilder.Impacts.fire(4F);

        // Mend: allies (and the caster) standing in the flames are healed instead of burned.
        // Same hybrid power split as the damage.
        var heal = SpellBuilder.Impacts.heal(0.5F);
        heal.power_blend = List.of(SpellBuilder.Impacts.powerBlend(SpellSchools.HEALING, 3F));
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        20, 0.02F, 0.15F)
                        .color(Color.NATURE.toRGBA())
        };
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_2.id());

        spell.impacts = List.of(damage, ignite, heal);

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
        spell.group = HOLY;

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
        spell.group = HOLY;
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
        spell.group = HOLY;

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
        var description = "Heals you and friendly targets around you by {heal}.";

        float range = 8;

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = range;
        spell.tier = 3;
        spell.group = HOLY;

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

        spell.impacts = List.of(heal);

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
        spell.group = DISCIPLINE;

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
        spell.group = HOLY;

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
        spell.group = HOLY;
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

    public static final Entry LEVITATE = add(levitate().book(Book.PRIEST));
    private static Entry levitate() {
        var id = Identifier.of(PaladinsMod.ID, "levitate");
        var title = "Levitate";
        var description = "Channel to rise into the air on holy light. When you stop, you keep floating and drift gently back down.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;
        spell.tier = 2;
        spell.group = DISCIPLINE;

        // Under 3s, 5 releases (one every 0.5s). Each release kicks the caster upward and refreshes
        // Floating; once the channel stops the kicks cease and only Floating lingers, so the caster
        // stops rising and drifts gently back down.
        SpellBuilder.Casting.channel(spell, 1.5F, 4);
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:one_handed_levitate_channel");
        spell.active.cast.movement_speed = 0F; // rooted horizontally; the lift is purely vertical
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        4, 0.02F, 0.12F).extent(0.5F).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        HOLY_SPELL_FLOAT.toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        2, 0.02F, 0.1F).extent(0.5F).color(Color.HOLY.toRGBA())
        };

        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        spell.target.type = Spell.Target.Type.CASTER;

        // Each channel tick launches the caster straight up. reset_velocity makes every kick land the
        // same regardless of prior motion, so the ascent is a steady climb instead of an accelerating
        // one; with the Levitate effect cancelling gravity, the ~0.2/tick kick carries them up a handful
        // of blocks over the channel.
        var lift = SpellBuilder.Impacts.velocityUp(0.15F);
        lift.action.velocity.reset_velocity = true;
        lift.action.velocity.intent = SpellTarget.Intent.HELPFUL;

        // Levitate effect (near-zero gravity) refreshed each tick so it lingers after the channel. Set
        // (not stacked): each tick just refreshes its duration.
        var levitate = SpellBuilder.Impacts.effectSet(PaladinEffects.LEVITATE.id.toString(), 6, 0);
        spell.impacts = List.of(lift, levitate);

        SpellBuilder.Cost.cooldown(spell, 18);
        spell.cost.cooldown.proportional = true; // released early => proportionally shorter cooldown
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.1F;

        return new Entry(id, spell, title, description);
    }

    public static final Entry PENANCE = add(penance().book(Book.PRIEST));
    private static Entry penance() {
        var id = Identifier.of(PaladinsMod.ID, "penance");
        var title = "Penance";
        var description = "Channel a volley of holy bolts at an enemy. Each of the three bolts deals {damage} damage and radiates an absorption shield to allies within {impact_range} blocks of your target — your offense is their protection.";

        var spell = SpellBuilder.createSpellActive();
        spell.school = SpellSchools.HEALING;
        spell.range = 20;
        spell.tier = 3;
        spell.group = DISCIPLINE;

        // Short channel: 3 bolts over 1.5s (one every 0.5s). Each release fires one homing bolt, and
        // each bolt that lands runs the impacts below.
        SpellBuilder.Casting.channel(spell, 1.5F, 3);
        spell.active.cast.animation = PlayerAnimation.of("spell_engine:off_hand_channeling");
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(SPARKS_FLOAT.toString()).color(Color.HOLY.toRGBA())
        };

        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        // Must lock an enemy — this is an offensive bolt volley. Sticky keeps the mark across the channel.
        SpellBuilder.Target.aim(spell);
        spell.target.aim.sticky = true;

        // Spin rate (degrees/tick) shared by the orbiting orb model and the spiraling spark helix, so
        // the two swirl together at the same angular speed.
        final float ORB_SPIN = 15F;

        // Delivery: one homing holy bolt per channel release, curving onto the locked target.
        spell.deliver.type = Spell.Delivery.Type.PROJECTILE;
        spell.deliver.projectile = new Spell.Delivery.ShootProjectile();
        spell.deliver.projectile.launch_properties.velocity = 0.8F;

        var projectile = new Spell.ProjectileData();
        projectile.homing_angle = 16F;
        projectile.client_data = new Spell.ProjectileData.Client();
        projectile.client_data.light_level = 12;
        // Spiraling sparks, coupled to the orbiting orb below. Magic-Arrow technique (Archers): a
        // Shape.LINE batch with Rotation.LOOK spawns each spark at the flight-path centre and casts it
        // outward along a direction that `roll` sweeps around the travel axis every tick — successive
        // sparks form a clean helical wake. Two strands 180° apart (roll_offset) make a double helix.
        // The roll rate is set to the orb's own spin (ORB_SPIN below), so the sparks spiral at the same
        // angular speed as the orb, reading as one coupled swirl around the bolt rather than two effects.
        projectile.client_data.travel_particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,
                        3, 0.12F, 0.16F, 0)
                        .roll(ORB_SPIN).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.LINE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,
                        3, 0.12F, 0.16F, 0)
                        .roll(ORB_SPIN).rollOffset(180F).color(Color.HOLY.toRGBA())
        };

        // Orbit tweak: the orb model (reused from Lightwell Orb) rides offset from the projectile centre
        // via an fx.initial translate, while the built-in per-tick spin sweeps that offset around the
        // travel axis — so the orb circles its own flight path. The renderer applies the spin *before*
        // the fx transforms (SpellProjectileRenderer), which is what turns a static offset into an orbit.
        var orb = SpellBuilder.ProjectileModels.model(
                "paladins:spell_projectile/lightwell_orb", 0.9F, LightEmission.GLOW);
        orb.rotate_degrees_per_tick = ORB_SPIN; // orbital angular speed, shared with the spark helix above
        var orbitOffset = new ModelEffect.Transform();
        orbitOffset.operation = "translate";
        orbitOffset.x = 0.6F; // orbit radius
        orb.fx.initial = List.of(orbitOffset);
        projectile.client_data.composite_model = SpellBuilder.ProjectileModels.composite(orb);

        spell.deliver.projectile.projectile = projectile;

        // Per bolt — harmful: damage the struck enemy.
        var damage = SpellBuilder.Impacts.damage(0.55F, 0.2F);
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        18, 0.2F, 0.6F).color(Color.HOLY.toRGBA())
        };
        damage.sound = new Sound(PaladinSounds.holy_shock_damage.id());

        // Per bolt — helpful: an absorption shield that stacks up as the volley lands. ADD mode adds
        // 1 + floor(0.125 * Healing power) stacks each time a bolt hits, so both spell power and the
        // number of bolts that reach an ally thicken the shield. Uncapped (cap 0): with more power the
        // shield keeps climbing rather than plateauing. It is placed by the area impact below, not on the
        // primary target: intent filtering keeps it off the struck enemy, and the splash spreads it to
        // friendlies near the impact. Particles land on each ally hit.
        var shield = SpellBuilder.Impacts.effectAdd_ScaledAmplifier(
                PaladinEffects.ABSORPTION.id.toString(), 8F, 1, 0.125F);
        shield.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        12, 0.2F, 0.25F).color(Color.HOLY.toRGBA())
        };

        spell.impacts = List.of(damage, shield);

        // Atonement: the shield ripples out from the point of impact. On a successful hit
        // (triggering_action_type DAMAGE) the splash runs only the absorption (execute_action_type
        // STATUS_EFFECT), so the bolt's damage stays single-target while friendly entities within the
        // radius are shielded — intent filtering keeps damage off allies and shields off enemies. No
        // dropoff: every ally in range gets the full stack.
        spell.area_impact = new Spell.AreaImpact();
        spell.area_impact.radius = 8;
        spell.area_impact.area.distance_dropoff = Spell.Target.Area.DropoffCurve.NONE;
        spell.area_impact.triggering_action_type = Spell.Impact.Action.Type.DAMAGE;
        spell.area_impact.execute_action_type = Spell.Impact.Action.Type.STATUS_EFFECT;
        spell.area_impact.particles = new ParticleBatch[] {
                // A golden pulse washing out from the struck enemy to the allies it shields.
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.4F, 0.6F).color(Color.HOLY.toRGBA()),
                new ParticleBatch(
                        SPARKS_FLOAT.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.4F).color(Color.HOLY.toRGBA())
        };
        spell.area_impact.sound = new Sound(PaladinSounds.holy_shock_heal.id());

        SpellBuilder.Cost.cooldown(spell, 8);
        spell.cost.cooldown.proportional = true; // released early => proportionally shorter cooldown
        SpellBuilder.Cost.item(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.2F;

        return new Entry(id, spell, title, description);
    }
}
