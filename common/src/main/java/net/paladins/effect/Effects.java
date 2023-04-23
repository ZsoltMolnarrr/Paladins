package net.paladins.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.paladins.PaladinsMod;
import net.spell_engine.api.effect.ActionImpairing;
import net.spell_engine.api.effect.EntityActionsAllowed;
import net.spell_engine.api.effect.Synchronized;

public class Effects {
    public static StatusEffect DIVINE_PROTECTION = new DivineProtectionStatusEffect(StatusEffectCategory.BENEFICIAL, 0x66ccff);
    public static StatusEffect JUDGEMENT = new JudgementStatusEffect(StatusEffectCategory.HARMFUL, 0xffffcc);
    public static StatusEffect ABSORPTION = new PriestAbsorptionStatusEffect(StatusEffectCategory.BENEFICIAL, 0xffffcc);

    public static void register() {
        ((Synchronized) DIVINE_PROTECTION).setSynchronized(true);
        ((Synchronized) JUDGEMENT).setSynchronized(true);
        ((Synchronized) ABSORPTION).setSynchronized(true);
        ActionImpairing.configure(JUDGEMENT, EntityActionsAllowed.STUN);

        int rawId = 710;
        Registry.register(Registry.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "divine_protection").toString(), DIVINE_PROTECTION);
        Registry.register(Registry.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "judgement").toString(), JUDGEMENT);
        Registry.register(Registry.STATUS_EFFECT, rawId++, new Identifier(PaladinsMod.ID, "priest_absorption").toString(), ABSORPTION);
    }
}
