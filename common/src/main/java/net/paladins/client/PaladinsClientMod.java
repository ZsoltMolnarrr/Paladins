package net.paladins.client;

import net.minecraft.util.Identifier;
import net.paladins.PaladinsMod;
import net.spell_engine.api.effect.CustomModelStatusEffect;
import net.spell_engine.api.effect.CustomParticleStatusEffect;
import net.spell_engine.api.render.CustomModels;
import net.paladins.client.armor.PaladinArmorRenderer;
import net.paladins.client.effect.FrostShieldRenderer;
import net.paladins.client.effect.FrozenRenderer;
import net.paladins.effect.Effects;
import net.paladins.item.Armors;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

import java.util.List;

public class PaladinsClientMod {
    public static void initialize() {
        var paladinArmorRenderer = new PaladinArmorRenderer();
        for (var entry: Armors.entries) {
            GeoArmorRenderer.registerArmorRenderer(paladinArmorRenderer,
                    entry.armorSet().head,
                    entry.armorSet().chest,
                    entry.armorSet().legs,
                    entry.armorSet().feet);
        }
//        CustomModels.registerModelIds(List.of(
//                new Identifier(PaladinsMod.ID, "projectile/arcane_missile"),
//                new Identifier(PaladinsMod.ID, "projectile/fireball_projectile"),
//                new Identifier(PaladinsMod.ID, "projectile/fire_meteor"),
//                new Identifier(PaladinsMod.ID, "projectile/frostbolt_projectile"),
//                FrozenRenderer.modelId,
//                FrostShieldRenderer.modelId_base,
//                FrostShieldRenderer.modelId_overlay
//        ));
//        CustomModelStatusEffect.register(Effects.frozen, new FrozenRenderer());
//        CustomParticleStatusEffect.register(Effects.frozen, new FrozenRenderer());
//        CustomModelStatusEffect.register(Effects.frostShield, new FrostShieldRenderer());
    }
}
