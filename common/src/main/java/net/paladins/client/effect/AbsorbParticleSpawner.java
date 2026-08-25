package net.paladins.client.effect;

import net.minecraft.entity.LivingEntity;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.ParticleHelper;
import net.spell_engine.fx.SpellEngineParticles;

public class AbsorbParticleSpawner implements CustomParticleStatusEffect.Spawner {
    public static final ParticleGroup particles =
            ParticleGroupBuilder.magic(SpellEngineParticles.magic_spark, ParticleGroup.Motion.FLOAT, Color.HOLY)
                    .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                            .count(5).speed(0.01F, 0.05F).verticalOrigin(Batches.FEET));

    @Override
    public void spawnParticles(LivingEntity livingEntity, int amplifier) {
        var world = livingEntity.getEntityWorld();
        if (world.isClient()) {
            var scaledParticles = particles.copy();
            scaledParticles.batch.count *= (amplifier + 1);
            scaledParticles.batch.max_speed *= livingEntity.getScaleFactor();
            ParticleHelper.play(world, livingEntity, scaledParticles);
        }
    }
}
