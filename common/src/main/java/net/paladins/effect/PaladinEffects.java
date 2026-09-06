package net.paladins.effect;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.registry.Registries;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.content.PaladinSounds;
import net.spell_engine.rpg_series.config.AttributeModifier;
import net.spell_engine.rpg_series.config.ConfigFile;
import net.spell_engine.rpg_series.config.EffectConfig;
import net.spell_engine.api.effect.*;
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

    /// 1.20.1 `EntityAttribute` is a raw object with no id accessor (1.21's `getIdAsString()`), so the
    /// registry has to be asked.
    private static String attributeId(EntityAttribute attribute) {
        return Registries.ATTRIBUTE.getId(attribute).toString();
    }

    public static final Effects.Entry DIVINE_PROTECTION = add(new Effects.Entry(
            new Identifier(PaladinsMod.ID, "divine_protection"),
            "Divine Protection",
            "Protects you from the incoming attack",
            new DivineProtectionStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff)
    ));

    public static final Effects.Entry BATTLE_BANNER = add(new Effects.Entry(
            new Identifier(PaladinsMod.ID, "battle_banner"),
            "Battle Banner",
            "Increases attack speed and knockback resistance",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff),
            new EffectConfig(List.of(
                    new AttributeModifier(
                            attributeId(EntityAttributes.GENERIC_ATTACK_SPEED),
                            0.4F,
                            EntityAttributeModifier.Operation.MULTIPLY_BASE
                    ),
                    new AttributeModifier(
                            SpellPowerMechanics.HASTE.id.toString(),
                            0.4F,
                            EntityAttributeModifier.Operation.MULTIPLY_BASE
                    ),
                    new AttributeModifier(
                            attributeId(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE),
                            0.4F,
                            EntityAttributeModifier.Operation.MULTIPLY_BASE
                    ),
                    new AttributeModifier(
                            new Identifier("ranged_weapon", "haste").toString(),
                            0.4F,
                            EntityAttributeModifier.Operation.MULTIPLY_BASE
                    )
            ))
    ));

    public static final Effects.Entry JUDGEMENT = add(new Effects.Entry(
            new Identifier(PaladinsMod.ID, "judgement"),
            "Stunned",
            "Prevents movement and actions",
            // 1.20.1 has no living-entity jump-strength attribute (only HORSE_JUMP_STRENGTH), so the
            // jump lock-out is carried entirely by `EntityActionsAllowed.STUN` (configured in register()).
            new JudgementStatusEffect(StatusEffectCategory.HARMFUL, 0xffffcc)
    ));

    /// Charges the wielded weapon with holy light. Applied as a stacking stash effect by
    /// {@code PaladinSpells.blessed_strikes}; each melee hit consumes one stack to deal bonus
    /// holy damage. Rendered by {@link GlowingItemStatusEffect} — the glow brightens as the blessings are
    /// channeled on, and dims again as they are spent.
    public static final Effects.Entry BLESSED_STRIKES = add(new Effects.Entry(
            new Identifier(PaladinsMod.ID, "blessed_strikes"),
            "Blessed Strikes",
            "Your weapon is charged with holy light, searing enemies you strike",
            new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc)
    ));

    /// Nearly cancels the holder's gravity (GENERIC_GRAVITY default 0.08, clamped [-1, 1]), leaving them
    /// hanging in the air and drifting down only very slowly: 0.08 * (1 - 0.95) = +0.004. Used by Levitate
    /// (see PaladinSpells.levitate): the spell's upward velocity kicks provide the ascent, while this just
    /// stops normal gravity from clawing the caster back down — and, since it outlives the channel, keeps
    /// them afloat afterwards until it fades and they settle gently to the ground.
    public static final Effects.Entry LEVITATE = add(new Effects.Entry(
            new Identifier(PaladinsMod.ID, "levitate"),
            "Levitate",
            "You drift gently through the air.",
            // 1.20.1 has no GENERIC_GRAVITY attribute; the float is carried by vanilla Slow Falling,
            // which LevitateStatusEffect maintains for as long as this effect lasts (gravity 0.08 -> 0.01).
            new LevitateStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc)
    ));

    public static final Effects.Entry ABSORPTION = add(new Effects.Entry(
            new Identifier(PaladinsMod.ID, "priest_absorption"),
            "Absorption",
            "Absorbs some damage you would take",
            // 1.20.1 has no GENERIC_MAX_ABSORPTION attribute (absorption is uncapped there); the shield
            // amount is granted directly by PriestAbsorptionStatusEffect#onApplied.
            new PriestAbsorptionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc)
    ));

    public static void register(ConfigFile.Effects config) {
        Synchronized.configure(DIVINE_PROTECTION.effect, true);
        Synchronized.configure(JUDGEMENT.effect, true);
        Synchronized.configure(ABSORPTION.effect, true);
        // Synced so its cloud particle spawner (registered client-side in PaladinsClientMod) can find it
        // on levitating entities — the visual mixin only iterates synchronized effects.
        Synchronized.configure(LEVITATE.effect, true);
        ActionImpairing.configure(JUDGEMENT.effect, EntityActionsAllowed.STUN);

        // Holy glow on the wielded weapon, brightening with each blessing. 0.2 opacity per stack, so the
        // full 5 stacks land on exactly 1.0 (fully opaque) — a dark weapon at 0 blessings, blazing at 5.
        // register() also marks the effect Synchronized (clients can only glow what they know about).
        GlowingItemStatusEffect.register(BLESSED_STRIKES.effect, Color.HOLY, 0.2F);

        Effects.register(entries, config.effects);

        Protection.register(DIVINE_PROTECTION.entry, new Protection.Pop(
                List.of(DivineProtectionStatusEffect.particles),
                PaladinSounds.divine_protection_impact.soundEvent()
        ));
    }
}
