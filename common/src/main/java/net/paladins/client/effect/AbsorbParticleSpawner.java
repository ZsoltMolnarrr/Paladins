package net.paladins.client.effect;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

public class AbsorbParticleSpawner implements CustomParticleStatusEffect.Spawner {
    public static final ParticleBatch particles = new ParticleBatch(
            SpellEngineParticles.MagicParticles.get(
                    SpellEngineParticles.MagicParticles.Shape.SPARK,
                    SpellEngineParticles.MagicParticles.Motion.FLOAT).id().toString(),
            ParticleBatch.Shape.WIDE_PIPE,
            ParticleBatch.Origin.FEET,
            null,
            5,
            0.01F,
            0.05F,
            0).color(Color.HOLY.toRGBA());

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var world = livingEntity.getWorld();
        if (world.isClient) {
            var scaledParticles = new ParticleBatch(particles);
            scaledParticles.count *= (amplifier + 1);
            scaledParticles.max_speed *= livingEntity.getScaleFactor();
            ParticleHelper.play(world, livingEntity, scaledParticles);
        }
    }
}
