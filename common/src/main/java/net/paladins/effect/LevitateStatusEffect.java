package net.paladins.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.spell_engine.api.effect.CustomStatusEffect;

/// The Levitate "afloat" effect. 1.20.1 has no `GENERIC_GRAVITY` attribute (it arrives in 1.21), so the
/// float is carried by vanilla Slow Falling instead — kept topped up for as long as Levitate lasts, and
/// deliberately outliving it by {@link #SLOW_FALLING_TICKS} so the caster always touches down softly
/// instead of dropping the instant the levitation ends. Slow Falling puts gravity at 0.01 instead of
/// 0.08 (the 1.21 attribute version lands on 0.004) — the closest vanilla stand-in on this line.
public class LevitateStatusEffect extends CustomStatusEffect {
    /// Ticks of Slow Falling kept beyond the end of this effect (the soft landing).
    private static final int SLOW_FALLING_TICKS = 3 * 20;
    /// Only re-apply Slow Falling once its remaining duration drops below this, so the top-up costs at
    /// most one effect sync per second rather than one per tick.
    private static final int REFRESH_BELOW_TICKS = 20;

    public LevitateStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // ticked every tick; the refresh guard below keeps the actual work rare
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        // Server-authoritative; the applied effect syncs to the client on its own.
        if (!entity.getWorld().isClient()) {
            var levitate = entity.getStatusEffect(this);
            var remaining = levitate != null ? levitate.getDuration() : 0;
            var slowFalling = entity.getStatusEffect(StatusEffects.SLOW_FALLING);
            if (slowFalling == null || slowFalling.getDuration() < REFRESH_BELOW_TICKS) {
                entity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.SLOW_FALLING, remaining + SLOW_FALLING_TICKS, 0, false, true, true));
            }
        }
    }
}
