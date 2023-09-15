package net.paladins.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;

public class Effects {
    public static StatusEffect DIVINE_PROTECTION = new DivineProtectionStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff);
    public static StatusEffect JUDGEMENT = new JudgementStatusEffect(StatusEffectCategory.HARMFUL, 0xffffcc);
    public static StatusEffect ABSORPTION = new PriestAbsorptionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc);

    public static void register() {
        Synchronized.configure(DIVINE_PROTECTION, true);
        Synchronized.configure(JUDGEMENT, true);
        Synchronized.configure(ABSORPTION, true);
        ActionImpairing.configure(JUDGEMENT, EntityActionsAllowed.STUN);

        int rawId = 710;
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "divine_protection").toString(), DIVINE_PROTECTION);
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "judgement").toString(), JUDGEMENT);
        Registry.register(Registries.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "priest_absorption").toString(), ABSORPTION);
    }
}
