package net.paladins.client;

import mod.azure.azurelibarmor.common.render.armor.AzArmorRenderer;
import mod.azure.azurelibarmor.common.render.armor.AzArmorRendererRegistry;
import net.paladins.client.armor.PaladinArmorRenderer;
import net.paladins.client.armor.PriestArmorRenderer;
import net.paladins.client.effect.DivineProtectionRenderer;
import net.paladins.client.entity.BarrierEntityRenderer;
import net.paladins.effect.PaladinEffects;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.rpg_series.item.Armor;
import net.spell_engine.api.render.BuffParticleSpawner;
import net.spell_engine.api.render.StunParticleSpawner;
import net.spell_engine.api.spell.fx.ParticleGroup;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder;
import net.spell_engine.api.spell.fx.ParticleGroupBuilder.Batches;
import net.spell_engine.client.util.Color;
import net.spell_engine.fx.SpellEngineParticles;

import java.util.function.Supplier;

public class PaladinsClientMod {
    public static void init() {
        CustomModelStatusEffect.register(PaladinEffects.DIVINE_PROTECTION.effect, new DivineProtectionRenderer());
        CustomParticleStatusEffect.register(PaladinEffects.JUDGEMENT.effect, new StunParticleSpawner());
        // CustomParticleStatusEffect.register(PaladinEffects.ABSORPTION.effect, new AbsorbParticleSpawner());
        CustomParticleStatusEffect.register(
                PaladinEffects.ABSORPTION.effect,
                new BuffParticleSpawner(
                        // The V1 `aura_*` twins are gone: the same texture rendered camera-facing
                        // instead of flat on the ground is what makes it an aura now.
                        ParticleGroupBuilder.aura(SpellEngineParticles.area_effect_553)
                                .attached()
                                .scale(1.4F)
                                .color(Color.HOLY.alpha(0.75F))
                                .batch(b -> b.shape(ParticleGroup.Shape.LINE))
                ).withFrequency(30).scaleWithAmplifier(false)
        );

        // A soft ring of clouds puffing around the feet of a levitating entity — the cloud they drift on.
        CustomParticleStatusEffect.register(
                PaladinEffects.LEVITATE.effect,
                new BuffParticleSpawner(
                        ParticleGroupBuilder.of("cloud")
                                .batch(b -> b.shape(ParticleGroup.Shape.CIRCLE).count(2)
                                        .speed(0.01F, 0.05F).verticalOrigin(Batches.FEET).extent(0.45F))
                ).withFrequency(3).scaleWithAmplifier(false)
        );

        // Entity model layers + renderers are registered per-platform:
        //   Fabric   -> FabricModClient (Fabric API)
        //   NeoForge -> NeoForgeClientMod (EntityRenderersEvent.RegisterLayerDefinitions / RegisterRenderers)
        // Layer definitions MUST be contributed during the RegisterLayerDefinitions phase, which is
        // over by the time FMLClientSetupEvent (where this init runs on NeoForge) fires.
        BarrierEntityRenderer.setup();

        registerArmorRenderer(Armors.paladinArmorSet_t1, PaladinArmorRenderer::paladin);
        registerArmorRenderer(Armors.paladinArmorSet_t2, PaladinArmorRenderer::crusader);
        registerArmorRenderer(Armors.paladinArmorSet_t3, PaladinArmorRenderer::netheriteCrusader);
        registerArmorRenderer(Armors.priestArmorSet_t1, PriestArmorRenderer::priest);
        registerArmorRenderer(Armors.priestArmorSet_t2, PriestArmorRenderer::prior);
        registerArmorRenderer(Armors.priestArmorSet_t3, PriestArmorRenderer::netheritePrior);
    }

    private static void registerArmorRenderer(Armor.Set set, Supplier<AzArmorRenderer> armorRendererSupplier) {
        AzArmorRendererRegistry.register(armorRendererSupplier, set.head, set.chest, set.legs, set.feet);
    }
}
