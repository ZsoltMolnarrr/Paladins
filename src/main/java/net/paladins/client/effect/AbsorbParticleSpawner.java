package net.paladins.client.effect;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.ParticleBatch;
import net.spell_engine.particle.ParticleHelper;

public class AbsorbParticleSpawner implements CustomParticleStatusEffect.Spawner {
    public static final ParticleBatch particles = new ParticleBatch(
            "spell_engine:holy_spark_mini",
            ParticleBatch.Shape.PIPE,
            ParticleBatch.Origin.FEET,
            null,
            5,
            0.01F,
            0.05F,
            0);

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
