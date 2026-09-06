package net.paladins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class PriestAbsorptionStatusEffect extends StatusEffect {
    private final int healthPerStack;

    public PriestAbsorptionStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
        this.healthPerStack = 2;
    }

    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    /// 1.20.1 `onApplied` takes the holder's `AttributeContainer` (the 1.21 2-arg overload does not exist).
    /// 1.20.1 also has no `GENERIC_MAX_ABSORPTION` attribute — absorption is uncapped, so the shield amount
    /// is simply granted here.
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        super.onApplied(entity, attributes, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(healthPerStack * (1 + amplifier))));
    }
}
