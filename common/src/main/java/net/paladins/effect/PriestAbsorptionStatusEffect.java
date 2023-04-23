package net.paladins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class PriestAbsorptionStatusEffect extends StatusEffect {
    private final int healthPerStack;
    public PriestAbsorptionStatusEffect(StatusEffectCategory statusEffectCategory, int i) {
        this(statusEffectCategory, i, 2);
    }

    public PriestAbsorptionStatusEffect(StatusEffectCategory statusEffectCategory, int i, int healthPerStack) {
        super(statusEffectCategory, i);
        this.healthPerStack = healthPerStack;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() - (float)(healthPerStack * (amplifier + 1)));
        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.setAbsorptionAmount(entity.getAbsorptionAmount() + (float)(healthPerStack * (amplifier + 1)));
        super.onApplied(entity, attributes, amplifier);
    }
}


