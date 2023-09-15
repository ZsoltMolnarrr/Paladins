package net.paladins.effect;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

public class DivineProtectionStatusEffect extends StatusEffect {
    public static final float multiplier = 0.5F;
    public DivineProtectionStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }

    private static final ParticleBatch particles = new ParticleBatch(
            "spell_engine:holy_spark",
            ParticleBatch.Shape.PILLAR,
            ParticleBatch.Origin.CENTER,
            null,
            25,
            0.01F,
            0.2F,
            0);

    public static void pop(Entity centerEntity) {
        ParticleHelper.sendBatches(centerEntity, new ParticleBatch[]{particles});
    }
}
