package net.paladins.client.armor;

import net.minecraft.resources.Identifier;
import net.paladins.PaladinsMod;
import net.rpg_foundation.armor_api.client.GeoArmorRenderer;

public final class PaladinArmorRenderer {
    private PaladinArmorRenderer() { }

    public static GeoArmorRenderer paladin() {
        return make("paladin_armor", "paladin_armor");
    }
    public static GeoArmorRenderer crusader() {
        return make("paladin_armor", "crusader_armor");
    }
    public static GeoArmorRenderer netheriteCrusader() {
        return make("paladin_armor", "netherite_crusader_armor");
    }

    private static GeoArmorRenderer make(String modelName, String textureName) {
        return GeoArmorRenderer.of(
                Identifier.fromNamespaceAndPath(PaladinsMod.ID, "geo/" + modelName + ".geo.json"),
                Identifier.fromNamespaceAndPath(PaladinsMod.ID, "textures/armor/" + textureName + ".png"))
                .trim(Identifier.fromNamespaceAndPath(PaladinsMod.ID, "armor/trim/" + textureName + "_generic"), false);
    }
}
