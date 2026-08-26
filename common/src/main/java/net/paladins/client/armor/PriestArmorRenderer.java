package net.paladins.client.armor;

import net.minecraft.resources.Identifier;
import net.paladins.PaladinsMod;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;

public final class PriestArmorRenderer {
    private PriestArmorRenderer() { }

    public static GeoArmorRenderer priest() {
        return make("priest_robes", "priest_robe");
    }
    public static GeoArmorRenderer prior() {
        return make("priest_robes", "prior_robe");
    }
    public static GeoArmorRenderer netheritePrior() {
        return make("priest_robes", "netherite_prior_robe");
    }

    private static GeoArmorRenderer make(String modelName, String textureName) {
        return GeoArmorRenderer.of(
                Identifier.fromNamespaceAndPath(PaladinsMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.fromNamespaceAndPath(PaladinsMod.ID, "textures/armor/" + textureName + ".png"))
                .trim(Identifier.fromNamespaceAndPath(PaladinsMod.ID, "armor/trim/" + textureName + "_generic"), false);
    }
}
