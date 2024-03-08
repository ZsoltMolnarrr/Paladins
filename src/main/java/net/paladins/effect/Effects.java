package net.paladins.effect;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;

public class Effects {
    public static StatusEffect DIVINE_PROTECTION = new DivineProtectionStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff);
    public static StatusEffect BATTLE_BANNER = new CustomStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff)
            .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED,
                    "052f3166-8a43-11ed-a1eb-0242ac120002",
                    0.3,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes_SpellPower.HASTE,
                    "052f3166-8a43-11ed-a1eb-0242ac120002",
                    0.3,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE)
            .addAttributeModifier(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE,
                    "052f3166-8a43-11ed-a1eb-0242ac120002",
                    0.5,
                    EntityAttributeModifier.Operation.MULTIPLY_BASE)
            ;

    public static StatusEffect JUDGEMENT = new JudgementStatusEffect(StatusEffectCategory.HARMFUL, 0xffffcc);
    public static StatusEffect ABSORPTION = new PriestAbsorptionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc);

    public static void register() {
        Synchronized.configure(DIVINE_PROTECTION, true);
        Synchronized.configure(JUDGEMENT, true);
        Synchronized.configure(ABSORPTION, true);
        ActionImpairing.configure(JUDGEMENT, EntityActionsAllowed.STUN);

        int rawId = 710;
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "divine_protection").toString(), DIVINE_PROTECTION);
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "battle_banner").toString(), BATTLE_BANNER);
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "judgement").toString(), JUDGEMENT);
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "priest_absorption").toString(), ABSORPTION);
    }
}
