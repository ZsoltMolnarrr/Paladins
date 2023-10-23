package net.paladins.client;

import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.client.effect.AbsorbParticleSpawner;
import net.paladins.client.effect.DivineProtectionRenderer;
import net.paladins.client.effect.StunParticleSpawner;
import net.paladins.effect.Effects;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;
import net.spell_engine.api.render.StunParticleSpawner;

import java.util.List;

public class PaladinsClientMod {
    public static void initialize() {
        CustomModels.registerModelIds(List.of(
                new Identifier(PaladinsMod.ID, "projectile/judgement"),
                DivineProtectionRenderer.modelId_base,
                DivineProtectionRenderer.modelId_overlay
        ));
        CustomModelStatusEffect.register(Effects.DIVINE_PROTECTION, new DivineProtectionRenderer());
        CustomParticleStatusEffect.register(Effects.JUDGEMENT, new StunParticleSpawner());
        CustomParticleStatusEffect.register(Effects.ABSORPTION, new AbsorbParticleSpawner());
    }
}
