package net.paladins.entity;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.api.datagen.SpellBuilder.Placements;
import net.spell_engine.api.spell.Spell.Impact.Action.Summon;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.api.spell.fx.VFX;
import net.spell_engine.api.spell.summon.AttributeScaling;
import net.spell_engine.api.spell.summon.SummonBehaviour;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;
import net.spell_power.api.SpellPowerMechanics;
import net.spell_power.api.SpellSchools;

import java.util.List;

/// Factory of the Paladins summon definitions. Each builder returns a {@link Summon}
/// ({@code Spell.Impact.Action.Summon}) that a spell drops into a `SUMMON` impact — the engine
/// spawns and configures it. See {@code WizardSummons} for the reference pattern.
public class PaladinSummons {

    private static final String LIGHTWELL_ORB = PaladinsMod.ID + ":lightwell_orb";

    private static final String SPARKS = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.SPARK,
            SpellEngineParticles.MagicParticles.Motion.ASCEND).id().toString();

    private static final String HOLY_DECELERATE = SpellEngineParticles.MagicParticles.get(
            SpellEngineParticles.MagicParticles.Shape.HOLY,
            SpellEngineParticles.MagicParticles.Motion.DECELERATE).id().toString();

    /// A stationary holy well that heals nearby wounded allies (and its owner). It never moves, can't
    /// be pushed or collided with, and is untargetable/invulnerable — a pure support fixture. It scales
    /// off its own Healing Spell Power, topped up from the owner via attribute scaling.
    public static Summon lightwell() {
        var b = new SummonBehaviour();
        b.lifespan.active_seconds = 12;
        // Sized to the 1.0s (=20 tick) spawn animation, played forward on spawn and reversed on despawn.
        b.lifespan.spawn_ticks = 20;
        b.lifespan.despawn_ticks = 20;

        b.is_attackable = false;

        // Movement: anchored — no motion, no gravity, no collision, not pushable.
        b.movement.can_move = false;
        b.movement.affected_by_gravity = false;
        b.movement.is_pushable = false;
        b.movement.collision = SummonBehaviour.Movement.CollisionMode.NONE;

        // Targeting: seek out nearby wounded allies (healer pet), never fight. Detection scoped to the
        // heal's own range so it only acquires allies it can actually reach.
        b.targeting.automatic_targeting = SummonBehaviour.Targeting.AutoTarget.FRIENDLY;
        b.targeting.attack_with_owner = false;
        b.targeting.revenge = false;
        b.targeting.look_around = false;
        b.targeting.detection_range.mode = SummonBehaviour.Targeting.DetectionRange.Mode.MAXIMUM_ACTION_RANGE;

        // Drop the current ally after each heal so the well re-scans and spreads its healing across all
        // wounded allies, instead of fixating on one target cast after cast.
        var clear = new SummonBehaviour.Targeting.ClearCondition();
        var afterHeal = new SummonBehaviour.Targeting.ClearCondition.OnActionCompleted();
        afterHeal.chance = 1F;
        afterHeal.action_type = SummonBehaviour.Action.Type.SPELL_CAST;
        clear.on_action_completed = List.of(afterHeal);
        b.targeting.clear_condition = clear;

        // Action: lob the healing orb at the acquired ally. No target = don't fire. The real cadence
        // comes from the spell's own haste-affected cooldown (see lightwell_orb); this action-level
        // cooldown is only the fallback used when a spell defines none, so it never applies here.
        var heal = new SummonBehaviour.Action.SpellCast(LIGHTWELL_ORB, 30);
        heal.aiming.accept_target = true;
        heal.aiming.fallback = SummonBehaviour.Action.SpellCast.Aiming.Fallback.NONE;
        heal.release_animation_variants = List.of(1);
        b.actions = List.of(SummonBehaviour.Action.spell(heal));

        // Lifecycle sounds (reused holy SFX — no new assets).
        b.sounds.spawn = PaladinSounds.lightwell_spawn.id().toString();
        b.sounds.ambient = PaladinSounds.lightwell_ambient.id().toString();
        b.sounds.despawn = PaladinSounds.lightwell_despawn.id().toString();

        // Spawn FX: a rising holy burst as the well forms.
        b.spawn_fx = new VFX();
        b.spawn_fx.particles = new ParticleBatch[] {
                new ParticleBatch(
                        HOLY_DECELERATE,
                        ParticleBatch.Shape.SPHERE, ParticleBatch.Origin.CENTER,
                        30, 0.2F, 0.4F).color(Color.HOLY.toRGBA())
        };

        // Existence FX: a gentle column of holy motes ascending from the well throughout its life.
        var aura = new SummonBehaviour.ExistenceParticles();
        aura.interval_ticks = 6;
        aura.particles = new ParticleBatch[] {
                new ParticleBatch(
                        SPARKS,
                        ParticleBatch.Shape.PILLAR, ParticleBatch.Origin.FEET,
                        2, 0.02F, 0.12F).extent(0.45F).color(Color.HOLY.toRGBA())
        };
        b.existence_particles = List.of(aura);

        // Placement: a single well ~1.5 blocks ahead of the caster, ground-snapped and facing them.
        var placement = Placements.pointAtAngle(1.5F, 0F);

        var summon = new Summon(PaladinEntities.LIGHTWELL.id.toString(), b, List.of(placement), 1);
        summon.attribute_scaling.entries = List.of(
                // Scale the well's healing power off the owner's healing spell power.
                scalingEntry(SpellSchools.HEALING.id.toString(), SpellSchools.HEALING.id.toString(), 0, 0.5),
                // Mirror the owner's Healing Haste onto the well so its (haste-affected) cooldown speeds
                // up with the caster. Haste is a percent stat where 100 = neutral (1.0x); the well seeds
                // at 100 (see PaladinEntities.lightwellDefaults), so add (owner - 100) to land it exactly
                // on the owner's value: base -100, coefficient 1.
                scalingEntry(SpellPowerMechanics.HASTE.id.toString(), SpellPowerMechanics.HASTE.id.toString(),
                        -SpellPowerMechanics.PERCENT_ATTRIBUTE_BASELINE, 1.0));
        return summon;
    }

    /// A single attribute-scaling entry: `targetAttribute += base + ownerAttribute * coefficient` (ADD_VALUE).
    private static AttributeScaling.Entry scalingEntry(String targetAttribute, String ownerAttribute,
                                                       double base, double coefficient) {
        var entry = new AttributeScaling.Entry();
        entry.attribute_id = targetAttribute;
        entry.modifiers = List.of(new AttributeScaling.Entry.OwnerModifier(
                ownerAttribute, EntityAttributeModifier.Operation.ADD_VALUE, base, coefficient));
        return entry;
    }
}
