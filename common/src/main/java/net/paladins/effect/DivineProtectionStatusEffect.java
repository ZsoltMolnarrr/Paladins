package net.paladins.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class DivineProtectionStatusEffect extends StatusEffect {
    public static final float multiplier = 0.5F;
    public DivineProtectionStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
}
