package net.paladins.client;

import net.paladins.client.armor.PaladinArmorRenderer;
import net.paladins.client.armor.PriestArmorRenderer;
import net.paladins.item.armor.Armors;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

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
