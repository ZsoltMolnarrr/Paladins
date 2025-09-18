package net.paladins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class PriestAbsorptionStatusEffect extends StatusEffect {
    private final int healthPerStack;

    public PriestAbsorptionStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
        this.healthPerStack = 2;
    }

    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        return entity.getAbsorptionAmount() > 0.0F || entity.getWorld().isClient;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(healthPerStack * (1 + amplifier))));
    }
}