package net.paladins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.spell_engine.api.effect.CustomStatusEffect;

/// The Levitate "afloat" effect. Behaves like a plain {@link CustomStatusEffect} — its near-zero
/// gravity comes from the attribute modifier in its EffectConfig — but as it wears off it hands the
/// caster over to vanilla Slow Falling for a few seconds, so they always touch down softly instead of
/// dropping the instant the levitation ends.
public class LevitateStatusEffect extends CustomStatusEffect {
    /// Seconds of Slow Falling granted as this effect ends.
    private static final int SLOW_FALLING_TICKS = 3 * 20;

    public LevitateStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    // Vanilla calls applyUpdateEffect only on ticks where this returns true. We want it exactly once,
    // on the final tick: the duration counts down to 1 (checked here), applyUpdateEffect runs, then the
    // duration hits 0 and the effect is removed — so Slow Falling begins precisely as Levitate ends.
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration <= 1;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        // Server-authoritative; the applied effect syncs to the client on its own.
        if (!entity.getWorld().isClient()) {
            entity.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.SLOW_FALLING, SLOW_FALLING_TICKS, 0, false, true, true));
        }
        return true; // keep the normal lifecycle — returning false would force early removal
    }
}
