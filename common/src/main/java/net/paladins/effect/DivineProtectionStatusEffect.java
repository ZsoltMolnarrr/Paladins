package net.paladins.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

public class DivineProtectionStatusEffect extends StatusEffect {
    public DivineProtectionStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    public static final ParticleGroup particles =
            ParticleGroupBuilder.magic(SpellEngineParticles.magic_holy, ParticleGroup.Motion.BURST, Color.HOLY)
                    .batch(b -> b.shape(ParticleGroup.Shape.PIPE).widthFactor(2F)
                            .count(25).speed(0.1F, 0.15F));
}
