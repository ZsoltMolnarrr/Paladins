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
            "Increases attack speed, spell haste, and knockback resistance",
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
            "Judgement",
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

    public static final Effects.Entry ABSORPTION = add(new Effects.Entry(
            Identifier.of(PaladinsMod.ID, "priest_absorption"),
            "Absorption",
            "Increases maximum absorption",
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

        Effects.register(entries, config.effects);

        Protection.register(DIVINE_PROTECTION.entry, new Protection.Pop(
                new ParticleBatch[]{ DivineProtectionStatusEffect.particles },
                PaladinSounds.divine_protection_impact.soundEvent()
        ));
    }
}
