package net.paladins.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.spell_engine.api.spell.fx.ParticleBatch;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

public class DivineProtectionStatusEffect extends StatusEffect {
    public DivineProtectionStatusEffect(StatusEffectCategory statusEffectCategory, int color) {
        super(statusEffectCategory, color);
    }
    public static final ParticleBatch particles = new ParticleBatch(
            SpellEngineParticles.MagicParticles.get(
                    SpellEngineParticles.MagicParticles.Shape.HOLY,
                    SpellEngineParticles.MagicParticles.Motion.BURST).id().toString(),
            ParticleBatch.Shape.WIDE_PIPE, ParticleBatch.Origin.CENTER,
            25, 0.1F, 0.15F)
            .color(Color.HOLY.toRGBA());
}
