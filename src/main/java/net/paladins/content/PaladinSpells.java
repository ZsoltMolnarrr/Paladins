package net.paladins.content;

import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.effect.PaladinEffects;
import net.paladins.entity.BarrierEntity;
import net.spell_engine.api.render.LightEmission;
import net.spell_engine.api.spell.ExternalSpellSchools;
import net.spell_engine.api.spell.Spell;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.Sound;
import net.spell_engine.api.util.TriState;
import net.spell_engine.client.gui.SpellTooltip;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_engine.fx.SpellEngineSounds;
import net.spell_power.api.SpellSchools;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PaladinSpells {

    public record Entry(Identifier id, Spell spell, String title, String description,
                        @Nullable SpellTooltip.DescriptionMutator mutator) { }
    public static final List<Entry> entries = new ArrayList<>();
    private static Entry add(Entry entry) {
        entries.add(entry);
        return entry;
    }

    private static final String GROUP_PRIMARY = "primary";

    private static Spell activeSpellBase() {
        var spell = new Spell();
        spell.type = Spell.Type.ACTIVE;
        spell.active = new Spell.Active();
        spell.active.cast = new Spell.Active.Cast();

        spell.learn = new Spell.Learn();
        spell.active.scroll = new Spell.Active.Scroll();

        return spell;
    }

    private static Spell.Impact createEffectImpact(Identifier effectId, float duration) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.STATUS_EFFECT;
        buff.action.status_effect = new Spell.Impact.Action.StatusEffect();
        buff.action.status_effect.effect_id = effectId.toString();
        buff.action.status_effect.duration = duration;
        return buff;
    }

    private static Spell.Impact createHeal(float coefficient) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.HEAL;
        buff.action.heal = new Spell.Impact.Action.Heal();
        buff.action.heal.spell_power_coefficient = coefficient;
        return buff;
    }

    private static Spell.Impact createDamage(float coefficient, float knockback) {
        var buff = new Spell.Impact();
        buff.action = new Spell.Impact.Action();
        buff.action.type = Spell.Impact.Action.Type.DAMAGE;
        buff.action.damage = new Spell.Impact.Action.Damage();
        buff.action.damage.spell_power_coefficient = coefficient;
        buff.action.damage.knockback = knockback;
        return buff;
    }

    private static Spell.Impact.TargetModifier createImpactModifier(String entityType) {
        var condition = new Spell.TargetCondition();
        condition.entity_type = entityType;
        var modifier = new Spell.Impact.TargetModifier();
        modifier.conditions = List.of(condition);
        return modifier;
    }

    private static ParticleBatch castingParticles(String particleId) {
        return new ParticleBatch(
                particleId,
                ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.FEET,
                1, 0.05F, 0.1F);
    }

    private static void configureCooldown(Spell spell, float duration) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        spell.cost.cooldown = new Spell.Cost.Cooldown();
        spell.cost.cooldown.duration = duration;
    }

    private static void configureItemCost(Spell spell, String itemId) {
        if (spell.cost == null) {
            spell.cost = new Spell.Cost();
        }
        spell.cost.item = new Spell.Cost.Item();
        spell.cost.item.id = itemId;
    }


    private static final Identifier HOLY_SPARKS = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.HOLY,
            SpellEngineParticles.MagicParticleFamily.Shape.SPARK,
            SpellEngineParticles.MagicParticleFamily.Motion.FLOAT
    ).id();

    private static final Identifier HOLY_SPARK_DECELERATE = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.HOLY,
            SpellEngineParticles.MagicParticleFamily.Shape.SPARK,
            SpellEngineParticles.MagicParticleFamily.Motion.DECELERATE
    ).id();

    private static final Identifier HOLY_IMPACT_DECELERATE = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.HOLY,
            SpellEngineParticles.MagicParticleFamily.Shape.IMPACT,
            SpellEngineParticles.MagicParticleFamily.Motion.DECELERATE
    ).id();

    private static final Identifier HOLY_IMPACT_FLOAT = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.HOLY,
            SpellEngineParticles.MagicParticleFamily.Shape.IMPACT,
            SpellEngineParticles.MagicParticleFamily.Motion.FLOAT
    ).id();

    private static final Identifier HOLY_IMPACT_BURST = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.HOLY,
            SpellEngineParticles.MagicParticleFamily.Shape.IMPACT,
            SpellEngineParticles.MagicParticleFamily.Motion.BURST
    ).id();

    private static final Identifier HEALING_PARTICLES = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.NATURE,
            SpellEngineParticles.MagicParticleFamily.Shape.IMPACT,
            SpellEngineParticles.MagicParticleFamily.Motion.ASCEND
    ).id();

    private static final Identifier HOLY_SPELL_FLOAT = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.HOLY,
            SpellEngineParticles.MagicParticleFamily.Shape.SPELL,
            SpellEngineParticles.MagicParticleFamily.Motion.FLOAT
    ).id();

    private static final Identifier HOLY_SPELL_DECELERATE = SpellEngineParticles.getMagicParticleVariant(
            SpellEngineParticles.HOLY,
            SpellEngineParticles.MagicParticleFamily.Shape.SPELL,
            SpellEngineParticles.MagicParticleFamily.Motion.DECELERATE
    ).id();

    private static Spell.Impact.TargetModifier extraDamageAgainstUndead() {
        var modifier = createImpactModifier("#minecraft:undead");
        var powerModifier = new Spell.Impact.Modifier();
        powerModifier.power_multiplier = 0.5F;
        modifier.modifier = powerModifier;
        return modifier;
    }

    private static Spell.Impact.TargetModifier extraCritAgainstUndead() {
        var modifier = createImpactModifier("#minecraft:undead");
        var powerModifier = new Spell.Impact.Modifier();
        powerModifier.critical_chance_bonus = 1F;
        modifier.modifier = powerModifier;
        return modifier;
    }

    private static void impactDeniedForMechanical(Spell.Impact impact) {
        var modifier = createImpactModifier("#spell_engine:mechanical");
        modifier.execute = TriState.DENY;
        impact.target_modifiers = List.of(modifier);
    }

    public static final Entry FLASH_HEAL = add(flash_heal());
    private static Entry flash_heal() {
        var id = Identifier.of(PaladinsMod.ID, "flash_heal");
        var title = "Flash Heal";
        var description = "";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = 16;
        spell.tier = 1;
        spell.group = GROUP_PRIMARY;

        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = "spell_engine:one_handed_healing_charge";
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(HOLY_SPARKS.toString())
        };

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_RELEASE.id(), 0);

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.use_caster_as_fallback = true;

        var heal = createHeal(1.2F);
        impactDeniedForMechanical(heal);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        30, 0.02F, 0.15F)
        };
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());

        spell.impacts = List.of(heal);

        configureCooldown(spell, 6);
        configureItemCost(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.2F;

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry DIVINE_PROTECTION = add(divine_protection());
    private static Entry divine_protection() {
        var id = Identifier.of(PaladinsMod.ID, "divine_protection");
        var title = "Divine Protection";
        var description = "";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;
        spell.tier = 2;

        spell.release.animation = "spell_engine:one_handed_area_release";

        var buff = createEffectImpact(PaladinEffects.DIVINE_PROTECTION.id, 8);
        buff.action.status_effect.amplifier = 2;
        buff.sound = new Sound(PaladinSounds.divine_protection_release.id());
        buff.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        40, 0.2F, 0.2F)
        };

        spell.impacts = List.of(buff);

        configureCooldown(spell, 30);
        configureItemCost(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry JUDGEMENT = add(judgement());
    private static Entry judgement() {
        var id = Identifier.of(PaladinsMod.ID, "judgement");
        var title = "Judgement";
        var description = "";

        var spell = activeSpellBase();
        spell.school = ExternalSpellSchools.PHYSICAL_MELEE;
        spell.range = 16;
        spell.tier = 3;

        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = "spell_engine:one_handed_projectile_charge";
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(HOLY_SPARKS.toString())
        };

        spell.release.animation = "spell_engine:one_handed_area_release";
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
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
                        5, 0, 0.1F,0),
                new ParticleBatch(
                        HOLY_SPARKS.toString(),
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER,
                        ParticleBatch.Rotation.LOOK,
                        4, 0, 0.1F,0)

        };
        var model = new Spell.ProjectileModel();
        model.light_emission = LightEmission.RADIATE;
        model.model_id = "paladins:projectile/judgement";
        model.scale = 1.2F;
        projectile.client_data.model = model;

        meteor.projectile = projectile;
        spell.deliver.meteor = meteor;

        var damage = createDamage(0.9F, 1F);
        damage.target_modifiers = List.of(extraDamageAgainstUndead());
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        25, 0.2F, 1F)
        };
        var stun = createEffectImpact(PaladinEffects.JUDGEMENT.id, 3);
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
                        100, 0.8F, 0.9F),
                new ParticleBatch(
                        HOLY_SPARKS.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        100, 0.2F, 0.4F),
                new ParticleBatch(
                        "smoke",
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        50, 0.1F, 0.3F)
        };
        spell.area_impact.sound = Sound.withVolume(PaladinSounds.judgement_impact.id(), 1.5F);

        configureCooldown(spell, 15);
        configureItemCost(spell, "runes:healing_stone");

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry BATTLE_BANNER = add(battle_banner());
    private static Entry battle_banner() {
        var id = Identifier.of(PaladinsMod.ID, "battle_banner");
        var title = "Battle Banner";
        var description = "";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = 0;
        spell.tier = 4;

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = new Sound(PaladinSounds.battle_banner_release.id());

        spell.deliver.type = Spell.Delivery.Type.CLOUD;
        var cloud = new Spell.Delivery.Cloud();
        cloud.volume.radius = 3;
        cloud.volume.extra_radius = new Spell.AreaImpact.ExtraRadius();
        cloud.volume.extra_radius.power_coefficient = 1;
        cloud.volume.extra_radius.power_cap = 4;
        cloud.volume.area.vertical_range_multiplier = 0.3F;
        cloud.presence_sound = Sound.withRandomness(PaladinSounds.battle_banner_presence.id(), 0);
        cloud.impact_tick_interval = 10;
        cloud.time_to_live_seconds = 10;
        cloud.client_data = new Spell.Delivery.Cloud.ClientData();
        cloud.client_data.light_level = 15;
        cloud.client_data.model = new Spell.ProjectileModel();
        cloud.client_data.model.model_id = "paladins:effect/battle_banner";
        cloud.client_data.model.rotate_degrees_per_tick = 0;
        cloud.client_data.model.light_emission = LightEmission.NONE;

        cloud.client_data.particles = new ParticleBatch[] {
                new ParticleBatch(HOLY_SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        null, 15, 0.1F, 0.15F, 0.0F, 0F),
                new ParticleBatch(HOLY_SPELL_FLOAT.toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        null, 3, 0.05F, 0.1F, 0.0F, 0F)
        };

        cloud.placement = new Spell.EntityPlacement();
        cloud.placement.location_offset_by_look = 2;
        cloud.placement.location_yaw_offset = 20;
        cloud.placement.apply_yaw = true;

        spell.deliver.clouds = List.of(cloud);

        var buff = createEffectImpact(PaladinEffects.BATTLE_BANNER.id, 2);
        spell.impacts = List.of(buff);

        configureCooldown(spell, 45);
        configureItemCost(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry HEAL = add(heal());
    private static Entry heal() {
        var id = Identifier.of(PaladinsMod.ID, "heal");
        var title = "Heal";
        var description = "";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.group = GROUP_PRIMARY;
        spell.range = 16;
        spell.tier = 0;

        spell.learn = null;
        spell.active.scroll = null;

        spell.active.cast.duration = 1F;
        spell.active.cast.animation = "spell_engine:one_handed_healing_charge";
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(HOLY_SPARKS.toString())
        };

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.use_caster_as_fallback = true;

        var heal = createHeal(0.75F);
        impactDeniedForMechanical(heal);
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_1.id());
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        20, 0.02F, 0.15F)
        };
        spell.impacts = List.of(heal);

        configureCooldown(spell, 4);
        configureItemCost(spell, "runes:healing_stone");

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry HOLY_SHOCK = add(holy_shock());
    private static Entry holy_shock() {
        var id = Identifier.of(PaladinsMod.ID, "holy_shock");
        var title = "Holy Shock";
        var description = "";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.group = GROUP_PRIMARY;
        spell.tier = 1;
        spell.range = 16;

        spell.active.cast.duration = 1.5F;
        spell.active.cast.animation = "spell_engine:one_handed_projectile_charge";
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(HOLY_SPARKS.toString())
        };

        spell.release.animation = "spell_engine:one_handed_healing_release";
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());

        spell.target.type = Spell.Target.Type.AIM;
        spell.target.aim = new Spell.Target.Aim();
        spell.target.aim.sticky = true;
        spell.target.aim.use_caster_as_fallback = true;

        var heal = createHeal(0.5F);
        impactDeniedForMechanical(heal);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        15, 0.02F, 0.15F),
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.25F)
        };
        heal.sound = new Sound(PaladinSounds.holy_shock_heal.id());

        var damage = createDamage(1F, 0.5F);
        damage.target_modifiers = List.of(extraCritAgainstUndead());
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.7F)
        };
        damage.sound = new Sound(PaladinSounds.holy_shock_damage.id());

        spell.impacts = List.of(heal, damage);

        configureItemCost(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.2F;

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry HOLY_BEAM = add(holy_beam());
    private static Entry holy_beam() {
        var id = Identifier.of(PaladinsMod.ID, "holy_beam");
        var title = "Holy Light";
        var description = "";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = 32;
        spell.tier = 2;

        spell.active.cast.duration = 5F;
        spell.active.cast.channel_ticks = 4;
        spell.active.cast.animation = "spell_engine:two_handed_channeling";
        spell.active.cast.start_sound = new Sound(PaladinSounds.holy_beam_start_casting.id());
        spell.active.cast.sound = Sound.withRandomness(PaladinSounds.holy_beam_casting.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_SPARKS.toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.LAUNCH_POINT, ParticleBatch.Rotation.LOOK,
                        3, 0.1F, 0.2F, 0),
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
                        1F, 0.1F, 0.2F, 0),
                new ParticleBatch(
                        "firework",
                        ParticleBatch.Shape.CIRCLE, ParticleBatch.Origin.CENTER, ParticleBatch.Rotation.LOOK,
                        1F, 0.1F, 0.2F, 0),
                new ParticleBatch(
                        HOLY_SPARKS.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        5, 0.1F, 0.2F)
        };
        spell.target.beam = beam;

        var heal = createHeal(0.5F);
        impactDeniedForMechanical(heal);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        1, 0.02F, 0.15F),
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        1, 0.2F, 0.25F)
        };
        heal.sound = new Sound(PaladinSounds.holy_beam_heal.id());

        var damage = createDamage(0.8F, 0.5F);
        damage.target_modifiers = List.of(extraCritAgainstUndead());
        damage.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_IMPACT_BURST.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        3, 0.2F, 0.7F),
                new ParticleBatch(
                        HOLY_SPARKS.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        6, 0.2F, 0.4F)
        };
        damage.sound = new Sound(PaladinSounds.holy_beam_damage.id());

        spell.impacts = List.of(heal, damage);

        configureCooldown(spell, 10);
        spell.cost.cooldown.proportional = true;
        spell.cost.exhaust = 0.2F;
        configureItemCost(spell, "runes:healing_stone");

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry CIRCLE_OF_HEALING = add(circle_of_healing());
    private static Entry circle_of_healing() {
        var id = Identifier.of(PaladinsMod.ID, "circle_of_healing");
        var title = "Circle of Healing";
        var description = "";

        float range = 8;

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = range;
        spell.tier = 3;

        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = "spell_engine:one_handed_area_charge";
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(HOLY_SPARKS.toString())
        };

        spell.release.animation = "spell_engine:one_handed_area_release";
        spell.release.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_RELEASE.id());
        spell.release.particles = new ParticleBatch[]{
                new ParticleBatch(
                        HOLY_SPARK_DECELERATE.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        100, 0.3F, 0.5F
                ).extent(range - 0.5F),
                new ParticleBatch(
                        HOLY_SPELL_DECELERATE.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        50, 0.1F, 0.5F
                ).extent(range - 0.5F),
                new ParticleBatch(
                        HOLY_IMPACT_FLOAT.toString(),
                        ParticleBatch.Shape.PIPE, ParticleBatch.Origin.FEET,
                        50, 0.1F, 0.2F
                ).extent(range)
        };

        spell.target.type = Spell.Target.Type.AREA;
        spell.target.area = new Spell.Target.Area();
        spell.target.area.vertical_range_multiplier = 0.6F;
        spell.target.area.include_caster = true;

        var heal = createHeal(0.4F);
        impactDeniedForMechanical(heal);
        heal.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HEALING_PARTICLES.toString(),
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        15, 0.02F, 0.15F),
                new ParticleBatch(
                        HOLY_IMPACT_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        15, 0.2F, 0.25F)
        };
        heal.sound = new Sound(SpellEngineSounds.GENERIC_HEALING_IMPACT_2.id());

        var buff = createEffectImpact(PaladinEffects.ABSORPTION.id, 6);
        buff.action.status_effect.apply_mode = Spell.Impact.Action.StatusEffect.ApplyMode.SET;
        buff.action.status_effect.amplifier_power_multiplier = 0.25F;

        spell.impacts = List.of(heal, buff);

        configureCooldown(spell, 10);
        configureItemCost(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.3F;

        return new Entry(id, spell, title, description, null);
    }

    public static final Entry BARRIER = add(barrier());
    private static Entry barrier() {
        var id = Identifier.of(PaladinsMod.ID, "barrier");
        var title = "Barrier";
        var description = "";

        var spell = activeSpellBase();
        spell.school = SpellSchools.HEALING;
        spell.range = 4;
        spell.tier = 4;

        spell.active.cast.duration = 0.5F;
        spell.active.cast.animation = "spell_engine:one_handed_area_charge";
        spell.active.cast.sound = Sound.withRandomness(SpellEngineSounds.GENERIC_HEALING_CASTING.id(), 0);
        spell.active.cast.particles = new ParticleBatch[] {
                castingParticles(HOLY_SPARKS.toString())
        };

        spell.release.animation = "spell_engine:one_handed_area_release";
        spell.release.sound = new Sound(PaladinSounds.holy_barrier_activate.id());
        spell.release.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_SPELL_DECELERATE.toString(),
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        50, 1F, 1F)
        };

        var spawn = new Spell.Impact();
        spawn.action = new Spell.Impact.Action();
        spawn.action.type = Spell.Impact.Action.Type.SPAWN;
        var barrier = new Spell.Impact.Action.Spawn();
        barrier.entity_type_id = BarrierEntity.TYPE.getRegistryEntry().getKey().get().getValue().toString();
        barrier.time_to_live_seconds = 10;
        spawn.action.spawns = List.of(barrier);
        spell.impacts = List.of(spawn);

        configureCooldown(spell, 40);
        configureItemCost(spell, "runes:healing_stone");
        spell.cost.exhaust = 0.4F;

        return new Entry(id, spell, title, description, null);
    }
}
