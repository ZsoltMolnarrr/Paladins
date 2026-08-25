package net.paladins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.server.world.ServerWorld;

public class PriestAbsorptionStatusEffect extends StatusEffect {
    private final int healthPerStack;

    public PriestAbsorptionStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
        this.healthPerStack = 2;
    }

    // 1.21.2+: server-side by signature; returning false removes the effect once the shield is gone.
    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        return entity.getAbsorptionAmount() > 0.0F;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        super.onApplied(entity, amplifier);
        entity.setAbsorptionAmount(Math.max(entity.getAbsorptionAmount(), (float)(healthPerStack * (1 + amplifier))));
    }
}