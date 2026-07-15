package net.paladins.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.api.config.AttributeModifier;
import net.spell_engine.api.config.ConfigFile;
import net.spell_engine.api.config.EffectConfig;
import net.spell_engine.api.effect.*;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.util.Color;
import net.spell_power.api.SpellPowerMechanics;

import java.util.ArrayList;
import java.util.List;

public class PaladinEffects {
    public static final List<Effects.Entry> entries = new ArrayList<>();
    private static Effects.Entry add(Effects.Entry entry) {
        entries.add(entry);
        return entry;
    }

    public static final Effects.Entry DIVINE_PROTECTION = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "divine_protection"),
            "Divine Protection",
            "Protects you from the incoming attack",
            new DivineProtectionStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff)
    ));

    public static final Effects.Entry BATTLE_BANNER = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "battle_banner"),
            "Battle Banner",
            "Increases attack speed and knockback resistance",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            EntityAttributes.GENERIC_ATTACK_SPEED.getIdAsString(),
                            0.4F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    new AttributeModifier(
                            SpellPowerMechanics.HASTE.id.toString(),
                            0.4F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    new AttributeModifier(
                            EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE.getIdAsString(),
                            0.4F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    new AttributeModifier(
                            Identifier.of("ranged_weapon", "haste").toString(),
                            0.4F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    )
            ))
    ));

    public static final Effects.Entry JUDGEMENT = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "judgement"),
            "Stunned",
            "Prevents movement and actions",
            new JudgementStatusEffect(StatusEffectCategory.HARMFUL, 0xffffcc),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            EntityAttributes.GENERIC_JUMP_STRENGTH.getIdAsString(),
                            0,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
                )
            )
    ));

    /// Charges the wielded weapon with holy light. Applied as a stacking stash effect by
    /// {@code PaladinSpells.seal_of_righteousness}; each melee hit consumes one stack to deal bonus
    /// holy damage. Rendered by {@link GlowingItemStatusEffect} — the glow brightens as the seals are
    /// channeled on, and dims again as they are spent.
    public static final Effects.Entry SEAL_OF_RIGHTEOUSNESS = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "seal_of_righteousness"),
            "Seal of Righteousness",
            "Your weapon is charged with holy light, searing enemies you strike",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc)
    ));

    // Levitate is built from two overlapping gravity effects (see PaladinSpells.levitate). Both are
    // GENERIC_GRAVITY (default 0.08, clamped [-1, 1]) ADD_MULTIPLIED_TOTAL modifiers, so while both are
    // active their multipliers add: final = 0.08 * (1 + sum). FLOATING nearly cancels gravity on its
    // own; LEVITATING tips it the rest of the way negative when layered on top.

    /// Nearly cancels the holder's gravity, leaving them hanging in the air and drifting down very
    /// slowly. Alone: 0.08 * (1 - 0.95) = +0.004. Outlives the Levitate channel, so the caster stays
    /// afloat after releasing.
    public static final Effects.Entry FLOATING = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "floating"),
            "Floating",
            "You drift gently through the air, buoyed by holy light",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            EntityAttributes.GENERIC_GRAVITY.getIdAsString(),
                            -0.95F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            ))
    ));

    /// The extra lift that makes the caster actually ascend while channeling Levitate. On its own it
    /// barely reduces gravity, but stacked with FLOATING it pushes the total negative
    /// (0.08 * (1 - 0.10 - 0.95) = -0.004), so the caster rises. Short-lived and re-applied each channel
    /// tick, so the ascent lasts only as long as the channel keeps refreshing it.
    public static final Effects.Entry LEVITATING = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "levitating"),
            "Levitating",
            "Holy light lifts you into the air",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            EntityAttributes.GENERIC_GRAVITY.getIdAsString(),
                            -0.10F,
                            EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                    )
            ))
    ));

    public static final Effects.Entry ABSORPTION = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "priest_absorption"),
            "Absorption",
            "Absorbs some damage you would take",
            new PriestAbsorptionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            EntityAttributes.GENERIC_MAX_ABSORPTION.getIdAsString(),
                            2,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
            ))
    ));

    public static void register(ConfigFile.Effects config) {
        Synchronized.configure(DIVINE_PROTECTION.effect, true);
        Synchronized.configure(JUDGEMENT.effect, true);
        Synchronized.configure(ABSORPTION.effect, true);
        ActionImpairing.configure(JUDGEMENT.effect, EntityActionsAllowed.STUN);

        // Holy glow on the wielded weapon, brightening with each seal. 0.2 opacity per stack, so the
        // full 5 stacks land on exactly 1.0 (fully opaque) — a dark weapon at 0 seals, blazing at 5.
        // register() also marks the effect Synchronized (clients can only glow what they know about).
        GlowingItemStatusEffect.register(SEAL_OF_RIGHTEOUSNESS.effect, Color.HOLY, 0.2F);

        Effects.register(entries, config.effects);

        Protection.register(DIVINE_PROTECTION.entry, new Protection.Pop(
                new ParticleBatch[]{ DivineProtectionStatusEffect.particles },
                PaladinSounds.divine_protection_impact.soundEvent()
        ));
    }
}
