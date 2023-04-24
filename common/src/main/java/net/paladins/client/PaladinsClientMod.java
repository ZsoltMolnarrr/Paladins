package net.paladins.client;

import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.paladins.client.armor.PaladinArmorRenderer;
import net.paladins.client.armor.PriestArmorRenderer;
import net.paladins.client.effect.AbsorbParticleSpawner;
import net.paladins.client.effect.DivineProtectionRenderer;
import net.paladins.client.effect.StunParticleSpawner;
import net.paladins.effect.Effects;
import net.paladins.item.armor.Armors;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.List;

public class PaladinsClientMod {
    public static void initialize() {
        var paladinArmorRenderer = new PaladinArmorRenderer();
        for (var entry: Armors.paladinEntries) {
            GeoArmorRenderer.registerArmorRenderer(paladinArmorRenderer,
                    entry.armorSet().head,
                    entry.armorSet().chest,
                    entry.armorSet().legs,
                    entry.armorSet().feet);
        }
        var priestArmorRenderer = new PriestArmorRenderer();
        for (var entry: Armors.priestEntries) {
            GeoArmorRenderer.registerArmorRenderer(priestArmorRenderer,
                    entry.armorSet().head,
                    entry.armorSet().chest,
                    entry.armorSet().legs,
                    entry.armorSet().feet);
        }
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
